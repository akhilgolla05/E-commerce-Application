package com.learnboot.dreamshopping.controller;

import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Cart;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.repository.UserRepository;
import com.learnboot.dreamshopping.response.ApiResponse;
import com.learnboot.dreamshopping.service.cart.CartItemService;
import com.learnboot.dreamshopping.service.cart.ICartService;
import com.learnboot.dreamshopping.service.user.IUserService;
import com.learnboot.dreamshopping.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/cartItems")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final ICartService cartService;

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {

//           User user = userRepository.findById(4L).get();
            User user = userService.getAuthenticatedUser(); //get loggedIn user

            Cart cart =cartService.initializeNewCart(user);

//            if(cartId == null){
//                cartId =cartService.initializeNewCart();
//            }
            cartItemService.addCartItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Item Created Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Item Not Found", null));
        }catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long productId){

        try {
            cartItemService.removeCartItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Item Removed Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam Integer quantity) {

        try {
            cartItemService.updateCartItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item Updated Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Item Not Found", null));
        }
    }
}
