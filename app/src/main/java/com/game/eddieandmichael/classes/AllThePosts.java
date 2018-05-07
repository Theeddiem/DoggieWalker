package com.game.eddieandmichael.classes;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllThePosts
{
    private static AllThePosts instance = null;

    private ArrayList<Post> allThePosts;
    private ArrayList<Post> walkersOnlyPosts;
    private ArrayList<Post> searchingOnlyPosts;

    private ArrayList<User> userCache;



    private AllThePosts()
    {

        allThePosts = new ArrayList<>();
        walkersOnlyPosts = new ArrayList<>();
        searchingOnlyPosts = new ArrayList<>();

        userCache = new ArrayList<>(30);

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
        boolean found = false;

        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).get_ID().equals(post.get_ID()))
            {
                found = true;
            }

        }

        if(!found)
        {
            list.add(post);
        }

    }

    public void addUserToCache(User user)
    {
        userCache.add(user);
    }

    public synchronized ArrayList<User> getUserCache() {
        return userCache;
    }

    public synchronized User findUserById(final String id)
    {
        final User[] returnUser = {null};
        boolean foundInCache = false;

        if(userCache.size() > 0)
        {
            for(User user: userCache)
            {
                if(user.get_ID().equals(id))
                {
                    returnUser[0] =  user;
                    foundInCache = true;
                    break;
                }
            }
        }
        if(!foundInCache)
        {
            Thread thread = new Thread(new Runnable()
            {
                String runId = id;
                @Override
                public void run()
                {
                    returnUser[0] = findAndAddUser(runId);
                }
            });

            thread.start();
            try {
                thread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            userCache.add(returnUser[0]);
        }

        return returnUser[0];
    }

    private User findAndAddUser(String id)
    {
        final User[] user = new User[1];

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference collection = firestore.collection("users");

        collection.whereEqualTo("_ID", id)
                .addSnapshotListener(new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(
                            QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                    {
                        user[0] = documentSnapshots.toObjects(User.class).get(0);
                    }
                });

        return user[0];
    }
}
