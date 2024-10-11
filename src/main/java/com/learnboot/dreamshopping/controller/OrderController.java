package com.learnboot.dreamshopping.controller;

import com.learnboot.dreamshopping.dto.OrderDto;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Order;
import com.learnboot.dreamshopping.response.ApiResponse;
import com.learnboot.dreamshopping.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam long userId){

        try{
            OrderDto order = orderService.placeOrder(userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Order Placed Successfully", order));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable long orderId){

        try {
            OrderDto orderDto = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order Retrieved Successfully", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable long userId){

        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Order Retrieved Successfully", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), HttpStatus.NOT_FOUND));
        }
    }

}
