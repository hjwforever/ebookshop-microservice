package com.aruoxi.microservice.shop;

import feign.RequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ShopServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopServiceApplication.class, args);
  }

  @Bean
  public RequestInterceptor requestTokenBearerInterceptor() {
    return requestTemplate -> {
      JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext()
          .getAuthentication();

      requestTemplate.header("Authorization", "Bearer " + token.getToken().getTokenValue());
    };
  }

//    @Bean
//    public ExecutorService traceableExecutorService() {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        return new TraceableExecutorService(beanFactory, executorService);
//    }
}

