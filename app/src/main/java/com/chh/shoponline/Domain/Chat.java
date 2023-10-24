package com.chh.shoponline.Domain;

public class Chat {
    private int id;
    private User user;
    private String contentChat;
    private String timeChat;

    public Chat(){

    }
    public Chat(int id, User user, String contentChat, String timeChat) {
        this.id = id;
        this.user = user;
        this.contentChat = contentChat;
        this.timeChat = timeChat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContentChat() {
        return contentChat;
    }

    public void setContentChat(String contentChat) {
        this.contentChat = contentChat;
    }

    public String getTimeChat() {
        return timeChat;
    }

    public void setTimeChat(String timeChat) {
        this.timeChat = timeChat;
    }
}
