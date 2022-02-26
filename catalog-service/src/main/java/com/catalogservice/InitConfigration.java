package com.catalogservice;

import com.catalogservice.entity.CatalogEntity;
import com.catalogservice.repository.CatalogRepository;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


@Slf4j
@Component
@Transactional
public class InitConfigration {

  private CatalogRepository catalogRepository;

  public InitConfigration(CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @PostConstruct
  public void initData() {
    new PostInsert(catalogRepository);
  }

  static class PostInsert {

    private final CatalogRepository catalogRepository;

    public PostInsert(CatalogRepository catalogRepository) {
      this.catalogRepository = catalogRepository;
      createCatalog();
    }

    public void createCatalog() {
      Iterable<CatalogEntity> entities = catalogRepository.findAll();

      if (StreamSupport.stream(entities.spliterator(), false).count() == 0) {
        List<CatalogEntity> catalogEntities = new ArrayList<>();
        catalogEntities.add(extracted("CATALOG-001", "Berlin", 100, 1500));
        catalogEntities.add(extracted("CATALOG-002", "Tokyo", 110, 1000));
        catalogEntities.add(extracted("CATALOG-003", "StockHolm", 120, 2000));
        catalogRepository.saveAll(catalogEntities);
        return;
      }

      entities.forEach(c -> {
        if (c.getProductId().equals("CATALOG-001")) {
          c.setStock(100);
        } else if (c.getProductId().equals("CATALOG-001")) {
          c.setStock(110);
        } else if (c.getProductId().equals("CATALOG-001")) {
          c.setStock(120);
        }
      });

    }

    private CatalogEntity extracted(String productId, String berlin, int stock, int unitPrice) {
      CatalogEntity catalogEntity = new CatalogEntity();
      catalogEntity.setProductId(productId);
      catalogEntity.setProductName(berlin);
      catalogEntity.setStock(stock);
      catalogEntity.setUnitPrice(unitPrice);
      return catalogEntity;
    }
  }
}
