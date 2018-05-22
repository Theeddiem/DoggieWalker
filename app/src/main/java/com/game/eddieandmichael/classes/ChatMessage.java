package com.game.eddieandmichael.classes;

import java.util.UUID;

public class ChatMessage
{
    private String message_ID;
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
        this.message_ID = UUID.randomUUID().toString();
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

    public String getMessage_ID() {
        return message_ID;
    }
}