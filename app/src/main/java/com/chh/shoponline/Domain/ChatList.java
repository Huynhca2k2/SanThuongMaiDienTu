package com.chh.shoponline.Domain;

public class ChatList {
    private String idUser, name, message, date, time;

    public ChatList(){

    }
    public ChatList(String idUser, String message){
        this.idUser = idUser;
        this.message = message;
    }

    public ChatList(String idUser, String name, String message, String date, String time) {
        this.idUser = idUser;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String mobile) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
