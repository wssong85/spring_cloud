package com.orderservice.messagequeue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.KafkaOrderDto;
import com.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;


  private List<KafkaOrderDto.Field> fields = Arrays.asList(
          KafkaOrderDto.Field.builder().type("string").optional(true).field("order_id").build(),
          KafkaOrderDto.Field.builder().type("string").optional(true).field("user_id").build(),
          KafkaOrderDto.Field.builder().type("string").optional(true).field("product_id").build(),
          KafkaOrderDto.Field.builder().type("int32").optional(true).field("qty").build(),
          KafkaOrderDto.Field.builder().type("int32").optional(true).field("unit_price").build(),
          KafkaOrderDto.Field.builder().type("int32").optional(true).field("total_price").build());

  private KafkaOrderDto.Schema schema = KafkaOrderDto.Schema.builder()
          .type("struct")
          .fields(fields)
          .optional(false)
          .name("orders")
          .build();

  public OrderDto send(String topic, OrderDto orderDto) {

    KafkaOrderDto.Payload payload = KafkaOrderDto.Payload.builder()
            .order_id(orderDto.getOrderId())
            .user_id(orderDto.getUserId())
            .product_id(orderDto.getProductId())
            .qty(orderDto.getQty())
            .unit_price(orderDto.getUnitPrice())
            .total_price(orderDto.getTotalPrice())
            .build();

    KafkaOrderDto.KafkaOrder kafkaOrder = KafkaOrderDto.KafkaOrder.builder()
            .schema(schema)
            .payload(payload)
            .build();

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = "";
    try {
      jsonInString = mapper.writeValueAsString(kafkaOrder);
    } catch (Exception e) {
      e.printStackTrace();
    }

    kafkaTemplate.send(topic, jsonInString);
    log.info("Order Producer sent data from the Order microservice: {}", kafkaOrder);

    return orderDto;
  }
}
