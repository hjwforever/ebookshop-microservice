package com.aruoxi.microservice.shop.service;

import com.aruoxi.microservice.shop.controller.dto.ItemDTO;
import com.aruoxi.microservice.shop.controller.dto.RequestDTO;
import com.aruoxi.microservice.shop.controller.dto.ResponseDTO;
import com.aruoxi.microservice.shop.domain.Cart;
import com.aruoxi.microservice.shop.domain.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel="spring")
public interface CartMapper {

	CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
	
    @Mapping(source = "userId", target = "user.id")
    Cart toModel(RequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    ResponseDTO toResponseDTO(Cart model);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "productName", target = "product.name")
    Item toModel(ItemDTO dto);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "productName", target = "product.name")
    List<Item> toModel(List<ItemDTO> dto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ItemDTO toDTO(Item model);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    List<ItemDTO> toDTO(List<Item> model);

}
