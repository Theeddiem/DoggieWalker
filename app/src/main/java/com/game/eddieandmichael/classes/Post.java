package com.game.eddieandmichael.classes;


import android.icu.util.Calendar;
import android.os.Build;

import java.util.UUID;

public class Post
{
    private String _ID;
    private String postOwner_ID;
    private String aboutThePost;
    private Calendar timeOfPost;
    private Calendar timeForService;
    private boolean isAWalker;

    public Post(String user_ID,boolean isAWalker)
    {
        this.postOwner_ID = user_ID;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            timeOfPost = Calendar.getInstance();
        }
        this.isAWalker = isAWalker;

        this._ID = UUID.randomUUID().toString();
    }

    public String get_ID() {
        return _ID;
    }

    public String getPostOwner_ID() {
        return postOwner_ID;
    }

    public void setPostOwner_ID(String postOwner_ID) {
        this.postOwner_ID = postOwner_ID;
    }

    public String getAboutThePost() {
        return aboutThePost;
    }

    public void setAboutThePost(String aboutThePost) {
        this.aboutThePost = aboutThePost;
    }

    public Calendar getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(Calendar timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public Calendar getTimeForService() {
        return timeForService;
    }

    public void setTimeForService(Calendar timeForService) {
        this.timeForService = timeForService;
    }

    public boolean isAWalker() {
        return isAWalker;
    }

    public void setAWalker(boolean AWalker) {
        isAWalker = AWalker;
    }
}
