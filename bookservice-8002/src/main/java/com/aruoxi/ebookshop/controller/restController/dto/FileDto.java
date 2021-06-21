package com.aruoxi.ebookshop.controller.restController.dto;

import com.aruoxi.ebookshop.controller.dto.BookUploadDto;
import org.springframework.web.multipart.MultipartFile;

public class FileDto {

    private MultipartFile uploadFile;
    private BookUploadDto uploadBookInfo;
    private String userAgent;
    private Integer bookId;

    public MultipartFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public BookUploadDto getUploadBookInfo() {
        return uploadBookInfo;
    }

    public void setUploadBookInfo(BookUploadDto uploadBookInfo) {
        this.uploadBookInfo = uploadBookInfo;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
}
