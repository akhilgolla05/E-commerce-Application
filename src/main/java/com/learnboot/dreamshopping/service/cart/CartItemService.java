package com.learnboot.dreamshopping.service.cart;

import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Cart;
import com.learnboot.dreamshopping.models.CartItem;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.repository.CartItemRepository;
import com.learnboot.dreamshopping.repository.CartRepository;
import com.learnboot.dreamshopping.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addCartItemToCart(Long cartId, Long productId, int quantity) {

        //get the cart
        //get the product
        //check if product exists?
        //if yes, update the quantity, if No, initiate a new Cart Item Entry
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductBYId(productId);
        CartItem cartItem = cart.getCartItems().stream().filter(item->item.getProduct().getId() == productId)
                .findFirst().orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setCart(cart);
        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeCartItemFromCart(Long cartId, Long productId) {

        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item->item.getProduct().getId() == productId)
                    .findFirst().orElseThrow(()-> new ResourceNotFoundException("CartItem Not Found"));
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item->item.getProduct().getId() == productId)
                .findFirst().ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        cart.setTotalAmount(
                cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        cartRepository.save(cart);

    }

    public CartItem getCartItem(Long cartId, Long productId) {

        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream()
                .filter(item->item.getProduct().getId() == productId)
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("CartItem Not Found"));
    }
}
