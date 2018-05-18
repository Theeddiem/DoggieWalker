package com.game.eddieandmichael.classes;

public class ChatMessage {

    private String messageText;
    private String currentUserID;
    private String messageUserID;
    private long messageTime;


    public ChatMessage(){//public NO -ARG CONSTRUCTOR NEEDED
                 }

    public ChatMessage(String messageText, String currentUserID, String messageUserID) {
        this.messageText = messageText;
        this.currentUserID = currentUserID;
        this.messageUserID = messageUserID;
        messageTime = System.currentTimeMillis();
     }

    public String getMessageText() {
        return messageText;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public long getMessageTime() {
        return messageTime;
    }
}