package com.aruoxi.ebookshop.controller;

import com.aruoxi.ebookshop.config.CommonResult;
import com.aruoxi.ebookshop.controller.dto.BookContent;
import com.aruoxi.ebookshop.controller.dto.BookSearchDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class consumerController {

    private static final String REST_URL_BOOKSERVICE="http://BookService";
    private static final String REST_URL_USERSERVICE="http://UserService";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/books")
    public CommonResult findAll(BookSearchDto search) {
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books",CommonResult.class);
    }

    @GetMapping(value = "/consumer/books/{id}")
    public CommonResult findById(@PathVariable("id") Long id) {
        System.out.println(REST_URL_BOOKSERVICE+"/api/books/"+id);
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/"+id,CommonResult.class);
    }

    @GetMapping(value = "/consumer/books/{bookID}/pages")
    public CommonResult bookAllContents(@PathVariable @Parameter(description = "书籍id") Long bookID){
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/"+bookID+"/pages",CommonResult.class);
    }

    @GetMapping(value = "/consumer/books/{bookID}/pages/{bookPagesSearch}")
    public CommonResult bookContents(@PathVariable @Parameter(description = "书籍id") Long bookID, @PathVariable @Parameter(description = "书籍页码", example = "1, 2-3") String bookPagesSearch){
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"api/books/"+bookID+"/pages/"+bookPagesSearch,CommonResult.class);
    }
}
