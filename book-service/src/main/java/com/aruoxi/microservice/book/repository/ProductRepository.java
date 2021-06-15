package com.aruoxi.microservice.book.repository;


import com.aruoxi.microservice.book.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "jpa", collectionResourceRel = "books", itemResourceRel = "book")
public interface ProductRepository extends JpaRepository<Product, Long> {

}
