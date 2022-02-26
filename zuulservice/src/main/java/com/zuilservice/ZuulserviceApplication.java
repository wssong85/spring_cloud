package com.zuilservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class ZuulserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZuulserviceApplication.class, args);
  }

}
