package com.aruoxi.microservice.book.controller;

import com.aruoxi.microservice.book.entity.Product;
import com.aruoxi.microservice.book.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/books")
    public List<Product> index() {
        List<Product> res = productService.getProducts();
        return res;
    }

    @PostMapping("/books")
    public Product create(@RequestBody @Valid Product product){
        return productService.saveProduct(product);
    }

    @GetMapping("/books/{id}")
    public Product view(@PathVariable("id") long id){
        Product p = productService.getProduct(id).get();
        return p;
    }

    @PostMapping(value = "/books/{id}")
    public Product edit(@PathVariable("id") long id, @RequestBody @Valid Product product){

        //Optional<Product> updatedProduct 
        Product p= productService.getProduct(id).get();
        
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setDescription(product.getDescription());

        return productService.saveProduct(p);
    }

}
