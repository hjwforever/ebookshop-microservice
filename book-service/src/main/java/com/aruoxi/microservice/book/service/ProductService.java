package com.aruoxi.microservice.book.service;

import com.aruoxi.microservice.book.entity.Product;
import com.aruoxi.microservice.book.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	
    @Autowired
    ProductRepository productRepository;
	
	   /* BOOK */
    public List<Product> getProducts(){
        return productRepository.findAll();
    }
    public Optional<Product> getProduct(long id){
        return productRepository.findById(id);
    }
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }


}
