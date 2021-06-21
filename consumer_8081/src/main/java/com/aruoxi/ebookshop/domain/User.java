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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
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
