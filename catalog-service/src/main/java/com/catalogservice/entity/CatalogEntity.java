package com.catalogservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "product_id")
  private String productId;
  @Column(nullable = false)
  private String productName;
  @Column(nullable = false)
  private Integer stock;
  @Column(nullable = false)
  private Integer unitPrice;

  @Column(nullable = false, updatable = false, insertable = false)
  @ColumnDefault(value = "CURRENT_TIMESTAMP")
  private Date createdAt;
}
