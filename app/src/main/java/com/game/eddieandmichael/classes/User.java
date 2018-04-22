package com.game.eddieandmichael.classes;

import android.net.Uri;

public class User
{
    private String fullName;
    private String userName;
    private String email;
    private String _ID;
    private Uri profilePhoto;

    private static User instance = null;

    private User() {
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
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

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Uri profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public static User getInstance()
    {
        if(instance == null)
        {
            instance = new User();
        }

        return instance;
    }
}
