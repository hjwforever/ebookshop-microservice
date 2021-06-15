package com.aruoxi.microservice.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor  @AllArgsConstructor
public class ProductOverview {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;


    public BigDecimal getPrice() {
        if (price == null)
            price = BigDecimal.ZERO;
        return price;
    }

}
