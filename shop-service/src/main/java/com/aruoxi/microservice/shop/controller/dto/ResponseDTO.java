package com.aruoxi.microservice.shop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import com.aruoxi.microservice.shop.domain.Item;

@AllArgsConstructor  @NoArgsConstructor
@Getter  @Setter
public class ResponseDTO {

    private String id;
    private Long userId;
    private String userName;
    private List<Item> items;
    private BigDecimal totalPrice;

}
