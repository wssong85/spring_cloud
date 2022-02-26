package com.catalogservice.controller;

import com.catalogservice.entity.CatalogEntity;
import com.catalogservice.service.CatalogService;
import com.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalog-service")
public class CatalogController {

  private final CatalogService catalogService;

  private final Environment env;

  @GetMapping("/health-check")
  public String status() {
    return String.format("It`s Working in Catalog Service %s", env.getProperty("local.server.port"));
  }

  @GetMapping("catalogs")
  public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
    Iterable<CatalogEntity> catalogs = catalogService.getCatalogs();
    List<ResponseCatalog> result = new ArrayList<>();
    catalogs.forEach(c -> result.add(new ModelMapper().map(c, ResponseCatalog.class)));
    return ResponseEntity.ok(result);
  }
}
