package com.chh.shoponline.Domain;

public class Order {
    private String id;
    private String id_user;
    private String id_shop;
    private String date;
    private int id_product;
    private boolean status;
    private int quantity;
    private String address_user;
    private String address_shop;
    private String credit;
    private Double price;

    public Order(){

    }

    public Order(String id_user, String id_shop, String date, int id_product, boolean status, int quantity, String address_user, String address_shop, String credit, Double price) {
        this.id_user = id_user;
        this.id_shop = id_shop;
        this.date = date;
        this.id_product = id_product;
        this.status = status;
        this.quantity = quantity;
        this.address_user = address_user;
        this.address_shop = address_shop;
        this.credit = credit;
        this.price = price;
    }

    public Order(String id, String id_user, String id_shop, String date, int id_product, boolean status, int quantity, String address_user, String address_shop, String credit, Double price) {
        this.id = id;
        this.id_user = id_user;
        this.id_shop = id_shop;
        this.date = date;
        this.id_product = id_product;
        this.status = status;
        this.quantity = quantity;
        this.address_user = address_user;
        this.address_shop = address_shop;
        this.credit = credit;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
    }

    public String getAddress_shop() {
        return address_shop;
    }

    public void setAddress_shop(String address_shop) {
        this.address_shop = address_shop;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
