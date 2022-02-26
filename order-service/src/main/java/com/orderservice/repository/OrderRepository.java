package com.orderservice.repository;

import com.orderservice.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
  OrderEntity findByOrderId(String orderId);
  Iterable<OrderEntity> findByUserId(String userId);
}
