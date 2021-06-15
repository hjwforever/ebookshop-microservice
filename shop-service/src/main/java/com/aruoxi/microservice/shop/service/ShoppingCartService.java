package com.aruoxi.microservice.shop.service;

import com.aruoxi.microservice.shop.controller.dto.RequestDTO;
import com.aruoxi.microservice.shop.domain.Cart;
//import com.aruoxi.microservice.shop.domain.Item;
//import com.aruoxi.microservice.shop.domain.ProductOverview;
//import com.aruoxi.microservice.shop.domain.UserInfo;
//import lombok.AllArgsConstructor;
//import org.springframework.web.client.RestTemplate;
//import java.util.List;
import com.aruoxi.microservice.shop.domain.Item;
import com.aruoxi.microservice.shop.domain.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
//@AllArgsConstructor
public class ShoppingCartService {
    @Autowired
    private IntegrationService integrationService;


    public Cart purchase(Cart shoppingCart) {
        String uuid = UUID.randomUUID().toString();
        shoppingCart.setId(uuid);

        UserInfo user = integrationService.getRemoteUserInfo(shoppingCart.getUser().getId());
        shoppingCart.setUser(user);

        List<Item> items = integrationService.getRemoteProductItemsInfo(shoppingCart.getItems());
        shoppingCart.setItems(items);

        integrationService.submitToBilling(shoppingCart);
        integrationService.notifyToDelivery(shoppingCart);
        integrationService.askForUserReview(shoppingCart);

        return shoppingCart;
    }

    public Cart purchase2(RequestDTO requestDTO) {
        String uuid = UUID.randomUUID().toString();
        Cart shoppingCart = new Cart();
        shoppingCart.setId(uuid);

        UserInfo user = integrationService.getRemoteUserInfo(requestDTO.getUserId());
        shoppingCart.setUser(user);

        List<Item> items = integrationService.getRemoteProductItemsInfo1(requestDTO.getItems());
        shoppingCart.setItems(items);

        integrationService.submitToBilling(shoppingCart);
        integrationService.notifyToDelivery(shoppingCart);
        integrationService.askForUserReview(shoppingCart);

        return shoppingCart;
    }

}
