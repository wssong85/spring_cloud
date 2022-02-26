package com.orderservice.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name = "orderEntity1")
@Table(name = "orders")
public class OrderEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "product_id")
  private String productId;
  @Column(nullable = false)
  private Integer qty;
  @Column(nullable = false)
  private Integer unitPrice;
  @Column(nullable = false)
  private Integer totalPrice;
  @Column(nullable = false)
  private String userId;
  @Column(nullable = false)
  private String orderId;

  @Column(nullable = false, updatable = false, insertable = false)
//  @ColumnDefault(value = "CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;
}
