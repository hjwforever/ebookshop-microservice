package com.aruoxi.ebookshop.controller;

import com.aruoxi.ebookshop.config.CommonResult;
import com.aruoxi.ebookshop.controller.dto.*;
import com.aruoxi.ebookshop.domain.Book;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

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


    @PostMapping("/consumer/books")
    public CommonResult add(@RequestBody Book book) {
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books",book,CommonResult.class);
    }

    @PutMapping("/consumer/books")
    void update(@RequestBody Book book) {
        restTemplate.put(REST_URL_BOOKSERVICE+"/api/books",book);
    }

    @DeleteMapping("/consumer/books/{id}")
    void delete(@PathVariable("id") Long id){
        restTemplate.delete(REST_URL_BOOKSERVICE+"/api/books/"+id,id);
    }

    @GetMapping(value = "/consumer/books/content")
    public CommonResult findContent(BookSearchDto search) {
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/consumer/books/content/"+search.getPageNum()+"/"+search.getBookId(),CommonResult.class);
    }

    @PostMapping(value = {"/consumer/auth/signin","/consumer/auth/login"})
    public CommonResult authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/login",loginRequest,CommonResult.class);
    }

    @PostMapping(value = {"/consumer/auth/signup","/register"})
    public CommonResult registerUser(@Valid @RequestBody RestRegistrationDto registrationDto, BindingResult result){
        registerUserDto registerUserdto = new registerUserDto();
        registerUserdto.result=result;
        registerUserdto.registrationDto=registrationDto;
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/signup",registerUserdto,CommonResult.class);
    }

    @PostMapping("/consumer/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request){
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/refreshtoken",request,ResponseEntity.class);
    }

    @GetMapping("/consumer/info")
    private CommonResult getUserInfo(@RequestParam String token){
        return restTemplate.getForObject(REST_URL_USERSERVICE+"/info/"+token,CommonResult.class);
    }


    @PostMapping(value = "/consumer/books/upload")
    public CommonResult upload(HttpServletRequest request, MultipartFile uploadFile,
                               BookUploadDto uploadBookInfo) {
        FileDto fileDto = new FileDto();
        fileDto.setUploadFile(uploadFile);
        fileDto.setUploadBookInfo(uploadBookInfo);
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books/upload",fileDto,CommonResult.class);
    }


    @RequestMapping(value = "/consumer/books/download")
    public ResponseEntity download1(HttpServletRequest request,
                                            @RequestHeader("User-Agent") String userAgent,
                                            @RequestParam("bookId") Integer bookId) {
        FileDto fileDto = new FileDto();
        fileDto.setUserAgent(userAgent);
        fileDto.setBookId(bookId);
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books/download",fileDto,ResponseEntity.class);
    }
}
