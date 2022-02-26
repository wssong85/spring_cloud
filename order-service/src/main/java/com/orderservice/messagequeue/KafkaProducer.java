package com.orderservice.messagequeue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public OrderDto send(String topic, OrderDto orderDto) {

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = "";
    try {
      jsonInString = mapper.writeValueAsString(orderDto);
    } catch (Exception e) {
      e.printStackTrace();
    }

    kafkaTemplate.send(topic, jsonInString);
    log.info("Kafka Producer sendt data from the ORder microservice: {}", orderDto);

    return orderDto;
  }
}