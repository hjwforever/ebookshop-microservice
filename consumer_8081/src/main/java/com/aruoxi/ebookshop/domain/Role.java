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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
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
