package com.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  private Environment env;

  public AuthorizationHeaderFilter(Environment env) {
    super(Config.class);
    this.env = env;
  }

  // login -> otken -> users (with token) -> header(include token)
  @Override
  public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
      }

      String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String jwt = authorizationHeader.replace("Bearer", "");

      if (!isJwtValid(jwt)) {
        return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
      }

      return chain.filter(exchange);
    });
  }

  // Mono, Flux -> Spring WebFlux
  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    log.error(err);
    return response.setComplete();
  }

  private boolean isJwtValid(String jwt) {
    boolean returnValue = true;

    String subject = null;

    String token = env.getProperty("token.secret");

    try {
      subject = Jwts.parser().setSigningKey(token)
              .parseClaimsJws(jwt).getBody()
              .getSubject();
    } catch (Exception ex) {
      returnValue = false;
      log.info(ex.getMessage());
    }

    // Certificate stored in file <trustServer.cer>

    if (subject == null || subject.isEmpty()) {
      returnValue = false;
    }

    return returnValue;
  }

  @Data
  static class Config {

  }
}
