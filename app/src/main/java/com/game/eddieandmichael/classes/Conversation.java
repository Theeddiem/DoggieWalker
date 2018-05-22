package com.game.eddieandmichael.classes;

import java.util.ArrayList;
import java.util.UUID;

public class Conversation
{
    private String firstUserID;
    private String secondUserID;

    ArrayList<ChatMessage> chatMessages;

    private String _ID;

    public Conversation(){}

    public Conversation(String firstUserID, String secondUserID)
    {
        this._ID = UUID.randomUUID().toString();

        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;

        this.chatMessages = new ArrayList<>();
    }



}
