package com.catalogservice.service;

import com.catalogservice.entity.CatalogEntity;
import com.catalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService{

  private final CatalogRepository catalogRepository;

  @Override
  public Iterable<CatalogEntity> getCatalogs() {
    return catalogRepository.findAll();
  }
}
