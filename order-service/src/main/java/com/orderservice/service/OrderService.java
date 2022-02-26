package com.orderservice.service;

import com.orderservice.dto.OrderDto;
import com.orderservice.entity.OrderEntity;

public interface OrderService {

  OrderDto createOrder(OrderDto orderDetails);

  OrderDto getOrderByOrderId(String orderId);

  Iterable<OrderEntity> getOrdersByUserId(String userId);

}
