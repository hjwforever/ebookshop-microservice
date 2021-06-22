package com.aruoxi.ebookshop.controller;

import com.aruoxi.ebookshop.config.CommonResult;
import com.aruoxi.ebookshop.controller.dto.*;
import com.aruoxi.ebookshop.domain.Book;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestController
public class consumerController {

    private static final Logger log = LoggerFactory.getLogger(consumerController.class);
    private static final String REST_URL_BOOKSERVICE="http://BookService";
    private static final String REST_URL_USERSERVICE="http://UserService";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/api/books")
    public CommonResult findAll(BookSearchDto search) {
        StringBuffer queryStr = objectToQueryStr(search);
        Map<String, Object> paramMap = objectToMap(search);

        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books"+queryStr,CommonResult.class, paramMap);
    }

    @GetMapping(value = "/api/books/{id}")
    public CommonResult findById(@PathVariable("id") Long id) {
        System.out.println(REST_URL_BOOKSERVICE+"/api/books/"+id);
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/"+id,CommonResult.class);
    }

    @GetMapping(value = "/api/books/{bookID}/pages")
    public CommonResult bookAllContents(@PathVariable @Parameter(description = "书籍id") Long bookID){
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/"+bookID+"/pages",CommonResult.class);
    }

    @GetMapping(value = "/api/books/{bookID}/pages/{bookPagesSearch}")
    public CommonResult bookContents(@PathVariable @Parameter(description = "书籍id") Long bookID, @PathVariable @Parameter(description = "书籍页码", example = "1, 2-3") String bookPagesSearch){
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"api/books/"+bookID+"/pages/"+bookPagesSearch,CommonResult.class);
    }

    @PostMapping("/api/books")
    public CommonResult add(@RequestBody Book book) {
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books",book,CommonResult.class);
    }

    @PutMapping("/api/books")
    void update(@RequestBody Book book) {
        restTemplate.put(REST_URL_BOOKSERVICE+"/api/books",book);
    }

    @DeleteMapping("/api/books/{id}")
    void delete(@PathVariable("id") Long id){
        restTemplate.delete(REST_URL_BOOKSERVICE+"/api/books/"+id,id);
    }

    @GetMapping(value = "/api/books/content")
    public CommonResult findContent(BookSearchDto search) {
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/content/"+search.getPageNum()+"/"+search.getBookId(),CommonResult.class);
    }

    /**
     * 登录
     * @param loginRequest 用户登录信息
     * @return
     */
    @PostMapping(value = {"/api/auth/signin","/api/auth/login"})
    public CommonResult authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/login",loginRequest,CommonResult.class);
    }

    /**
     * 注册
     * @param registrationDto 用户注册信息
     * @return
     */
    @PostMapping(value = {"/api/auth/signup","/api/auth/register"})
    public CommonResult registerUser(@RequestBody RestRegistrationDto registrationDto){
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/signup",registrationDto,CommonResult.class);
    }

    /**
     * 权限测试
     * @param auth all, user, seller, admin
     * @return
     */
    @GetMapping("/api/test/{auth}")
    public CommonResult publicTest(@PathVariable("auth") String auth) {
        log.info("auth = " + auth);
        return restTemplate.getForObject(REST_URL_USERSERVICE+"/api/test/"+auth,CommonResult.class);
    }

    @PostMapping("/api/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request){
        return restTemplate.postForObject(REST_URL_USERSERVICE+"/api/auth/refreshtoken",request,ResponseEntity.class);
    }

    @GetMapping("/api/user/info")
    private CommonResult getUserInfo(@RequestParam String token){
        return restTemplate.getForObject(REST_URL_USERSERVICE+"/api/user/info?token="+token,CommonResult.class);
    }

    @PostMapping(value = "/api/books/upload")
    public CommonResult upload(HttpServletRequest request,@RequestPart("uploadFile") MultipartFile uploadFile,
                               BookUploadDto uploadBookInfo) throws Exception {
//        FileDto fileDto = new FileDto();
//        File file = multipartFileToFile(uploadFile, "ebookshop");
//
//        fileDto.setUploadFile(file);
//        fileDto.setUploadBookInfo(uploadBookInfo);
//        log.info("uploadBookInfo = " + uploadBookInfo);
//        log.info("fileDto = " + fileDto);
        log.info("file = " + uploadFile);
        log.info("fileName = " + uploadFile.getOriginalFilename());
        log.info("FileSize = " + uploadFile.getSize());
//
//        log.info("file1 = " + file);
//        log.info("fileName1 = " + file.getName());
//        log.info("FileSize1 = " + file.length());
//        log.info("getPath = " + file.getPath());
//        log.info("getCanonicalPath = " + file.getCanonicalPath());
//        log.info("getAbsolutePath = " + file.getAbsolutePath());

        String bookName = uploadBookInfo.getBookName();
        String author = uploadBookInfo.getAuthor();
        Float price = uploadBookInfo.getPrice();

        log.info("path = " + REST_URL_BOOKSERVICE+"/api/books/upload?bookName="+bookName+"&author="+author+"&price="+price);
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books/upload?bookName="+bookName+"&author="+author+"&price="+price,uploadFile,CommonResult.class);
    }

//    @PostMapping(value = "/api/books/upload")
//    public CommonResult upload(HttpServletRequest request,@RequestPart("uploadFile") MultipartFile uploadFile,
//                               BookUploadDto uploadBookInfo) throws Exception {
//        FileDto fileDto = new FileDto();
//        File file = multipartFileToFile(uploadFile, "ebookshop");
//
//        fileDto.setUploadFile(file);
//        fileDto.setUploadBookInfo(uploadBookInfo);
//        log.info("uploadBookInfo = " + uploadBookInfo);
//        log.info("fileDto = " + fileDto);
//        log.info("file = " + uploadFile);
//        log.info("fileName = " + uploadFile.getOriginalFilename());
//        log.info("FileSize = " + uploadFile.getSize());
////        File file1 = new File("src/main/resources/targetFile.tmp");
////        uploadFile.transferTo(file1);
//        log.info("file1 = " + file);
//        log.info("fileName1 = " + file.getName());
//        log.info("FileSize1 = " + file.length());
//        log.info("getPath = " + file.getPath());
//        log.info("getCanonicalPath = " + file.getCanonicalPath());
//        log.info("getAbsolutePath = " + file.getAbsolutePath());
//
//        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books/upload",fileDto,CommonResult.class);
//    }

    @RequestMapping(value = "/api/books/download")
    public ResponseEntity download1(HttpServletRequest request,
                                            @RequestHeader("User-Agent") String userAgent,
                                            @RequestParam("bookId") Integer bookId) {
        FileDto fileDto = new FileDto();
        fileDto.setUserAgent(userAgent);
        fileDto.setBookId(bookId);
        return restTemplate.postForObject(REST_URL_BOOKSERVICE+"/api/books/download",fileDto,ResponseEntity.class);
    }

    @RequestMapping(value = "/api/books/downloadUrl", method = RequestMethod.GET)
    public CommonResult download1(@RequestParam("bookId") Long bookId) {
        return restTemplate.getForObject(REST_URL_BOOKSERVICE+"/api/books/downloadUrl?bookId="+bookId,CommonResult.class);
    }

    /**
     * 将一个类查询方式加入map
     */
    public static StringBuffer objectToQueryStr(Object obj) {
        StringBuffer url = new StringBuffer("?");
        if (obj == null) {
            return url;
        }
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                url.append( field.getName()+"={"+field.getName()+"}&");
            }
        } catch (Exception e) {

        }
        return url;
    }

    /**
     * 将一个类查询方式加入map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {

        }

        return map;
    }

    public static File multipartFileToFile(MultipartFile file, String bh) throws Exception {
        if (file.getSize() <= 0) {
            return null;
        }
        File toFile = null;
        // 用户主目录
        String userHome = System.getProperties().getProperty("user.home");
        StringBuilder filepath = new StringBuilder();
        filepath.append(userHome).append(File.separator).append("files").append(File.separator).append(bh).append(File.separator);

        //创建文件夹
        toFile = new File(filepath.toString());
        FileUtils.forceMkdir(toFile);

        //创建文件，此时文件为空
        filepath.append(file.getOriginalFilename());
        toFile = new File(filepath.toString());

        //为文件添加流信息
        file.transferTo(toFile);
        return toFile;
    }

}
