package com.learnboot.dreamshopping.service.cart;

import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Cart;
import com.learnboot.dreamshopping.models.CartItem;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.repository.CartItemRepository;
import com.learnboot.dreamshopping.repository.CartRepository;
import com.learnboot.dreamshopping.repository.OrderRepository;
import com.learnboot.dreamshopping.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    private IProductService productService;

    //since we don't have a User Now, so we are creating Cart-Id Manually
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(long id) {
      Cart cart =  cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

      //cart.getCartItems().stream().map(item->productService.convertToProductDto(item.getProduct()));

      BigDecimal totalAmount = cart.getTotalAmount();

      cart.setTotalAmount(totalAmount);
      return cart;
    }

    @Override
    public void clearCart(long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(cart.getId());
        cart.getCartItems().clear();
        cartRepository.delete(cart);
    }

    @Override
    public BigDecimal getTotalPrice(long id) {
        Cart cart = getCart(id);
        return cart.getCartItems().stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Cart initializeNewCart(User user){
       return  Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
//        Cart newCart = new Cart();
//        long newCartId = cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);
//        return cartRepository.save(newCart).getId();
    }

    @Override
    public Cart getCartByUserId(long userId) {
        return cartRepository.findByUserId(userId);
    }
}
