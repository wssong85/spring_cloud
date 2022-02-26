package com.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

  public LoggingFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    GatewayFilter filter = new OrderedGatewayFilter(((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();

      log.info("Logging Filter baseMessage -> {}", config.getBaseMessage());
      if (config.isPreLogger()) {
        log.info("Logging PRE Filter: request id -> {}", request.getId());
      }

      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        if (config.isPreLogger()) {
          log.info("Logging filter: response code -> {}", response.getStatusCode());
        }
      }));
      // 가정 먼저
//      Ordered.HIGHEST_PRECEDENCE
    }), Ordered.LOWEST_PRECEDENCE);
    return filter;
  }

  @Data
  static class Config {
    // Put the configuration properties
    private String baseMessage;
    private boolean preLogger;
    private boolean postLogger;
  }
}
