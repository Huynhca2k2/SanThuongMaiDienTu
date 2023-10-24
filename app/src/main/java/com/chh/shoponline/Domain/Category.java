package com.chh.shoponline.Domain;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String nameCate;
    private int idRes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    public Category(){
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }

    public Category(int id, String nameCate, int idRes) {
        this.id = id;
        this.nameCate = nameCate;
        this.idRes = idRes;
    }
}
