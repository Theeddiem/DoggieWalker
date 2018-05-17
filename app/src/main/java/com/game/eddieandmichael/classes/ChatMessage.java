package com.game.eddieandmichael.classes;

public class ChatMessage {

    private String messageText;
    private String messageUserID;
    private String currentUserID;
    private long messageTime;

    public ChatMessage(String messageText, String currentUserID,String messageUserID) {
        this.messageText = messageText;
        this.messageUserID = messageUserID;
        this.currentUserID=currentUserID;
        // Initialize to current time
        messageTime = System.currentTimeMillis();
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public void setMessageUserID(String messageUserID) {
        this.messageUserID = messageUserID;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getmessageUserID() {
        return messageUserID;
    }

    public void setmessageUserID(String messageUserID) {
        this.messageUserID = messageUserID;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}