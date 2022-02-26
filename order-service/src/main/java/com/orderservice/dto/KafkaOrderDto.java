package com.orderservice.dto;

//import org.springframework.messaging.handler.annotation.Payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.List;

//@Data
//@Builder
public class KafkaOrderDto {


  @Getter
  @Builder
  public static class KafkaOrder implements Serializable {
    private Schema schema;
    private Payload payload;
  }

  @Getter
  @Builder
  public static class Schema {
    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
  }

  @Getter
  @Builder
  public static class Payload {
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int unit_price;
    private int total_price;
  }

  @Getter
  @Builder
  public static class Field {
    private String type;
    private boolean optional;
    private String field;
  }
}
