package com.chh.shoponline.Domain;

public class MessagesList {
    private String name, idUser, last_msg, picUrl;
    private Long id_chat, time_chat;
    private int un_seen_msg;

    public MessagesList(Long id_chat, String last_msg, String name, String picUrl, Long time_chat, int un_seen_msg){
        this.id_chat = id_chat;
        this.last_msg = last_msg;
        this.name = name;
        this.picUrl = picUrl;
        this.time_chat = time_chat;
        this.un_seen_msg = un_seen_msg;
    }

    public MessagesList(String name, String idUser, String last_msg, String picUrl, int un_seen_msg, Long id_chat, Long time_chat) {
        this.name = name;
        this.idUser = idUser;
        this.last_msg = last_msg;
        this.picUrl = picUrl;
        this.un_seen_msg = un_seen_msg;
        this.id_chat = id_chat;
        this.time_chat = time_chat;
    }

    public Long getId_chat() {
        return id_chat;
    }

    public void setId_chat(Long id_chat) {
        this.id_chat = id_chat;
    }

    public String getPicUrl() {
        return picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setMobile(String mobile) {
        this.idUser = mobile;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public int getUn_seen_msg() {
        return un_seen_msg;
    }

    public void setUn_seen_msg(int un_seen_msg) {
        this.un_seen_msg = un_seen_msg;
    }

    public Long getTime_chat() {
        return time_chat;
    }

    public void setTime_chat(Long time_chat) {
        this.time_chat = time_chat;
    }

}
