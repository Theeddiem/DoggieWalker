package com.game.eddieandmichael.classes;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class User implements Serializable
{
    private String fullName;
    private String userName;
    private String email;
    private String _ID;
    private String profilePhoto;
    private String aboutUser;
    private List<String> chatWithUser;


    private static User instance = null;

    private User()
    {
        aboutUser = "";
        this.chatWithUser = new ArrayList<>();
    }

    public static User getInstance()
    {
        if(instance == null)
        {
            instance = new User();

        }

        return instance;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String get_ID() {
        return _ID;
    }

    public User set_ID(String _ID) {
        this._ID = _ID;
        return this;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }

    public List<String> getChatWithUser() {
        return chatWithUser;
    }

    public void setChatWithUser(List<String> chatWithUser) {
        this.chatWithUser = chatWithUser;
    }

   public void addUserToChat(String id)
   {
       if (!chatWithUser.contains(id))
       {
             chatWithUser.add(id);
       }
       else
           Log.i(TAG, "fail");


   }
}
