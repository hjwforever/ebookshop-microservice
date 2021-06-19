package com.aruoxi.ebookshop.domain;

public class Role {

    private Long id;

    private String name;

    private String note;

    public Role() {
    }

    public Role(String name) {
        this(name, "");
    }

    public Role(String name, String note) {
        this.name = name;
        this.note = note;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + 
                ", notes='" + note +
                '\'' +
                '}';
    }


}
