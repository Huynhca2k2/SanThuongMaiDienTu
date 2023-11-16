package com.chh.shoponline.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String id_chat;
    private String id;
    private String address;
    private String credit;
    private String name;
    private String role;
    private int numCart = 0;
    private String picUrl;

    public User(){
    }

//    public User(String id_chat, String name, String picUrl){
//        this.id_chat = id_chat;
//        this.name = name;
//        this.picUrl = picUrl;
//    }

    public User(String id, String name, String picUrl, String address){
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.address = address;
    }

    public User(String id, String address, String credit, String name, String role, String picUrl) {
        this.id = id;
        this.address = address;
        this.credit = credit;
        this.name = name;
        this.role = role;
        this.picUrl = picUrl;
    }

    public String getId_chat() {
        return id_chat;
    }

    public void setId_chat(String id_chat) {
        this.id_chat = id_chat;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNumCart() {
        return numCart;
    }

    public void setNumCart(int numCart) {
        this.numCart = numCart;
    }

}
