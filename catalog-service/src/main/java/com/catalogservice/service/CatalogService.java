package com.catalogservice.service;

import com.catalogservice.entity.CatalogEntity;

public interface CatalogService {
  Iterable<CatalogEntity> getCatalogs();

}
