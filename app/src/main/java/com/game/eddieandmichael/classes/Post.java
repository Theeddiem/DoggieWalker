package com.game.eddieandmichael.classes;

import android.util.Log;

import java.io.Serializable;
import java.util.UUID;

public class Post implements Serializable
{
    private String _ID;
    private User postOwner;
    private String postOwner_ID;
    private String aboutThePost;
    private boolean isAWalker;
    private long timeOfPost;
    private String price;
    private String placesOfPost;
    private String postsPhotos;
    private boolean hasPhoto;

    public Post(String user_ID,boolean isAWalker)
    {
        this.postOwner_ID = user_ID;

        timeOfPost = System.currentTimeMillis();
        Log.d("Post Contractor", "Post Time: "+timeOfPost);

        this.isAWalker = isAWalker;

        this._ID = UUID.randomUUID().toString();

        hasPhoto = false;
    }

    public Post(User user, boolean isAWalker)
    {
        this.postOwner = user;
        this.isAWalker = isAWalker;
        this.postOwner_ID = user.get_ID();
        timeOfPost = System.currentTimeMillis();

        hasPhoto = false;

    }

    public Post(){}

    public String get_ID() {
        return _ID;
    }
    public void set_ID(String ID){this._ID = ID;}

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

    public boolean isAWalker() {
        return isAWalker;
    }

    public void setAWalker(boolean AWalker) {
        isAWalker = AWalker;
    }

    public User getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(User postOwner) {
        this.postOwner = postOwner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlacesOfPost() {
        return placesOfPost;
    }

    public void setPlacesOfPost(String placesOfPost) {
        this.placesOfPost = placesOfPost;
    }

    public long getTimeOfPost() {
        return timeOfPost;
    }

    public String getPostsPhotos() {
        return postsPhotos;
    }

    public void setPostsPhotos(String postsPhotos) {
        this.postsPhotos = postsPhotos;
    }

    public void setTimeOfPost(long timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public boolean isHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
}
