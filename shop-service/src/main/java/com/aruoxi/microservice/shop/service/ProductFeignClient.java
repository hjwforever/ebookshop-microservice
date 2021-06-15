package com.aruoxi.microservice.shop.service;

import com.aruoxi.microservice.shop.domain.ProductOverview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(url = "http://localhost:8083/api/product/", name = "ProductCatalogService")
@FeignClient("PRODUCT-SERVICE")
public interface ProductFeignClient {

    @GetMapping("/api/product/{id}")
    ProductOverview findById(@PathVariable("id") Long id);

}
