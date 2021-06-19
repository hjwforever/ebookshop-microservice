package com.aruoxi.ebookshop.domain;

import java.util.Collection;


public class User {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String password;

    private String avatar;

    private String introduction;

    private Collection<Role> roles;

    public User() {
        this.avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
        this.introduction = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.nickname = username;
        this.email = email;
        this.password = password;
        this.avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
        this.introduction = "";
    }

    public User(String username, String email, String password, Collection<Role> roles) {
        this(username, email, password);
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "{id:" + id +
                ", username:" + username +
                ", nickname:" + nickname +
                ", email:" + email +
                ", password:" + "*********" +
                ", roles:" + roles.toString() +
                "}";
    }
}
