package com.chh.shoponline.Domain;

public class MessagesList {
    private String name, idUser, lastMessage, profilePic;
    private Long chatKey, timeChat;
    private int unseenMessages;

    public MessagesList(String name, String idUser, String lastMessage, String profilePic, int unseenMessages, Long chatKey, Long timeChat) {
        this.name = name;
        this.idUser = idUser;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
        this.timeChat = timeChat;
    }

    public Long getChatKey() {
        return chatKey;
    }

    public void setChatKey(Long chatKey) {
        this.chatKey = chatKey;
    }

    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    public Long getTimeChat() {
        return timeChat;
    }

    public void setTimeChat(Long timeChat) {
        this.timeChat = timeChat;
    }

}
