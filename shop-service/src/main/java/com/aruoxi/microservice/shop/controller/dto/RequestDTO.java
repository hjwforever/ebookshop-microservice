package com.aruoxi.microservice.shop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor  @NoArgsConstructor
@Getter  @Setter
public class RequestDTO {

    private Long userId;
    private List<ItemDTO> items;

}