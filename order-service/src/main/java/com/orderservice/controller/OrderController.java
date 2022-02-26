package com.orderservice.controller;

import com.orderservice.dto.OrderDto;
import com.orderservice.entity.OrderEntity;
import com.orderservice.messagequeue.KafkaProducer;
import com.orderservice.messagequeue.OrderProducer;
import com.orderservice.service.OrderService;
import com.orderservice.vo.RequestOrder;
import com.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("order-service")
@Slf4j
public class OrderController {

  private final OrderService orderService;

  private final Environment env;

  private final KafkaProducer kafkaProducer;

  private final OrderProducer orderProducer;

  @GetMapping("/health-check")
  public String status() {
    return String.format("It`s Working in Order Service %s", env.getProperty("local.server.port"));
  }

  @PostMapping("{userId}/orders")
  public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                   @RequestBody RequestOrder order) {

    log.info("Before add orders data");
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    OrderDto orderDto = mapper.map(order, OrderDto.class);
    orderDto.setUserId(userId);

    /* jpa */
    OrderDto result = orderService.createOrder(orderDto);
    ResponseOrder responseOrder = mapper.map(result, ResponseOrder.class);

//    orderDto.setOrderId(UUID.randomUUID().toString());
//    orderDto.setTotalPrice(order.getQty() * order.getUnitPrice());
//    ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

    /* send this order to the kafka */
//    kafkaProducer.send("example-catalog-topic", orderDto);
//    orderProducer.send("orders", orderDto);

    log.info("After add orders data");
    return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
  }

  @GetMapping("{userId}/orders")
  public ResponseEntity<List<OrderDto>> getOrders(@PathVariable("userId") String userId) throws Exception {
    log.info("Before retrieve orders data");
    Iterable<OrderEntity> result = orderService.getOrdersByUserId(userId);
    List<OrderDto> orderDtos = new ArrayList<>();
    result.forEach(c -> {
      orderDtos.add(new ModelMapper().map(c, OrderDto.class));
    });

    try {
      Thread.sleep(1000);
      throw new Exception("장애 발생");
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }

    log.info("After retrieve orders data");
    return ResponseEntity.ok(orderDtos);
  }

}
