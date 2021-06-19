package com.aruoxi.ebookshop.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


public class Book implements Serializable {

  private static final long SERIAL_VERSION_UID = 4048798961366546485L;

  private Long bookId;


  private String bookName;


  private Float starts;

  private String author;

  private String bookIntro;

  private String bookUri;

  private String bookUrl;

  private String bookCoverImg;

  private Float originalPrice;

  private Float sellingPrice;

  private Long bookCategoryId;

  private String tag;

  private String bookSellStatus;

  private Date createTime;

  private Date updateTime;

  public Book() {
    this.starts = 3f;
    this.originalPrice = 0f;
    this.sellingPrice = 0f;
    this.bookSellStatus = "热销";
    this.bookCoverImg = "https://cdn.jsdelivr.net/gh/hjwforever/images@main/img/defaultCoverImg.png";
    this.bookCategoryId = 1L;
    this.tag = "经典好书";
    this.author = "E-Book Shop";
    this.bookIntro = "暂无介绍";
  }

  public Book(Long bookId, @NotBlank @Size(max = 100) String bookName, @Min(value = 0) @Max(value = 5) Float starts, @Size(max = 100) String author, @Size(max = 200) String bookIntro, String bookUri, String bookCoverImg, Float originalPrice, Float sellingPrice, Long bookCategoryId, String tag, String bookSellStatus, Date createTime, Date updateTime) {
    this();
    this.bookId = bookId;
    this.bookName = bookName;
    this.starts = starts;
    this.author = author;
    this.bookIntro = bookIntro;
    this.bookUri = bookUri;
    this.bookCoverImg = bookCoverImg;
    this.originalPrice = originalPrice;
    this.sellingPrice = sellingPrice;
    this.bookCategoryId = bookCategoryId;
    this.tag = tag;
    this.bookSellStatus = bookSellStatus;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }
}