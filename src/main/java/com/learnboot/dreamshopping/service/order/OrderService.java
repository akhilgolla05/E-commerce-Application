package com.learnboot.dreamshopping.service.order;

import com.learnboot.dreamshopping.dto.OrderDto;
import com.learnboot.dreamshopping.enums.OrderStatus;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Cart;
import com.learnboot.dreamshopping.models.Order;
import com.learnboot.dreamshopping.models.OrderItem;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.repository.OrderRepository;
import com.learnboot.dreamshopping.repository.ProductRepository;
import com.learnboot.dreamshopping.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    private final ModelMapper modelMapper;

    @Override
    public OrderDto placeOrder(long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder =  orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        //set the user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {

        return cart.getCartItems()
                .stream()
                .map(item->{
                    Product product = item.getProduct();
                    product.setInventory(product.getInventory() - item.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(
                            order,
                            item.getQuantity(),
                            product,
                            item.getUnitPrice()
                    );
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item->item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Order Not Found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
       return orderRepository.findByUserId(userId)
               .stream().map(this::convertToDto).toList();
    }

    private OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }


}
