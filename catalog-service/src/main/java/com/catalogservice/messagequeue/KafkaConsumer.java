package com.catalogservice.messagequeue;

import com.catalogservice.entity.CatalogEntity;
import com.catalogservice.repository.CatalogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class KafkaConsumer {

  private final CatalogRepository catalogRepository;

  @KafkaListener(topics = "example-catalog-topic")
  public void updateQty(String kafkaMessage) {
    log.info("Kafka Message: {}", kafkaMessage);

    Map<Object, Object> map = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    try {
      map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
    } catch (Exception e) {
      e.printStackTrace();
    }

    String productId = (String) map.get("productId");
    CatalogEntity entity = catalogRepository.findByProductId(productId);

    if (entity != null) {
      entity.setStock(entity.getStock() - (Integer)map.get("qty"));
//      catalogRepository.save(entity);
    }
  }
}
