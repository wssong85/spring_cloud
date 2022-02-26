package com.orderservice.service;

import com.orderservice.dto.OrderDto;
import com.orderservice.entity.OrderEntity;
import com.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

  private final OrderRepository orderRepository;

  @Override
  public OrderDto createOrder(OrderDto orderDto) {
    orderDto.setOrderId(UUID.randomUUID().toString());
    orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderEntity OrderEntity = mapper.map(orderDto, OrderEntity.class);

    OrderEntity save = orderRepository.save(OrderEntity);

    OrderDto result = mapper.map(save, OrderDto.class);
    return result;
  }

  @Override
  public OrderDto getOrderByOrderId(String orderId) {
    OrderEntity result = orderRepository.findByOrderId(orderId);
    return new ModelMapper().map(result, OrderDto.class);
  }

  @Override
  public Iterable<OrderEntity> getOrdersByUserId(String userId) {
    return orderRepository.findByUserId(userId);
  }
}
