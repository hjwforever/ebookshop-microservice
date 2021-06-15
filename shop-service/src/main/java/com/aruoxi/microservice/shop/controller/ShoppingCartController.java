package com.aruoxi.microservice.shop.controller;

import com.aruoxi.microservice.shop.controller.dto.RequestDTO;
import com.aruoxi.microservice.shop.controller.dto.ResponseDTO;
import com.aruoxi.microservice.shop.domain.Cart;
import com.aruoxi.microservice.shop.service.CartMapper;
import com.aruoxi.microservice.shop.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/cart")
@AllArgsConstructor
public class ShoppingCartController {

	@Autowired
    private ShoppingCartService service;
    //private CartMapper mapper;

    @PostMapping
    public ResponseEntity<ResponseDTO> submit(@RequestBody RequestDTO requestDTO) {
        //Cart cart = CartMapper.INSTANCE.toModel(requestDTO);

        Cart cart = service.purchase2(requestDTO);
        //ResponseDTO responseDTO = CartMapper.INSTANCE.toResponseDTO(cart);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setId(cart.getId());
        responseDTO.setUserId(cart.getUser().getId());
        responseDTO.setUserName(cart.getUser().getName());
        responseDTO.setTotalPrice(cart.getTotalPrice());
        responseDTO.setItems(cart.getItems());
        return ResponseEntity.created(URI.create(responseDTO.getId())).body(responseDTO);
    }

}
