package com.learnboot.dreamshopping.service.order;

import com.learnboot.dreamshopping.dto.OrderDto;
import com.learnboot.dreamshopping.models.Order;

import java.util.List;

public interface IOrderService {

    OrderDto placeOrder(long userId);
    OrderDto getOrder(Long id);

    List<OrderDto> getUserOrders(Long userId);
}
