package com.aruoxi.ebookshop.domain;


import java.time.Instant;

public class RefreshToken {

  private long id;

  private User user;

  private String token;

  private Instant expiryDate;
}