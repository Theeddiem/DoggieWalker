package com.game.eddieandmichael.classes;

import java.util.ArrayList;

public class AllThePosts
{
    private static AllThePosts instance = null;

    ArrayList<Post> allThePosts;
    ArrayList<Post> walkersOnlyPosts;
    ArrayList<Post> searchingOnlyPosts;



    private AllThePosts()
    {
        allThePosts = new ArrayList<>();
        walkersOnlyPosts = new ArrayList<>();
        searchingOnlyPosts = new ArrayList<>();

    }

    public static AllThePosts getInstance()
    {
        if(instance == null)
        {
            instance = new AllThePosts();
        }

        return instance;
    }

    public ArrayList<Post> getAllThePosts() {
        return allThePosts;
    }

    public ArrayList<Post> getWalkersOnlyPosts() {
        return walkersOnlyPosts;
    }

    public ArrayList<Post> getSearchingOnlyPosts() {
        return searchingOnlyPosts;
    }

    public synchronized void updateList(ArrayList<Post> list, Post post)
    {
        list.add(post);
    }
}
