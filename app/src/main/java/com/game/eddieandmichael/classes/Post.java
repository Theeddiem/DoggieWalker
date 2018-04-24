package com.game.eddieandmichael.classes;


import android.icu.util.Calendar;
import android.os.Build;

public class Post
{
    private User postOwner;
    private String aboutThePost;
    private Calendar timeOfPost;
    private Calendar timeForService;
    private boolean isAWalker;

    public Post(User user,boolean isAWalker)
    {
        this.postOwner = user;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            timeOfPost = Calendar.getInstance();
        }
        this.isAWalker = isAWalker;
    }

    public User getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(User postOwner) {
        this.postOwner = postOwner;
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
