package com.game.eddieandmichael.classes;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AllThePosts
{
    private static AllThePosts instance = null;

    private ArrayList<Post> allThePosts;
    private ArrayList<Post> walkersOnlyPosts;
    private ArrayList<Post> searchingOnlyPosts;

    private HashMap<String,User> userCache;

    PostComparator postComparator;


    private AllThePosts()
    {

        allThePosts = new ArrayList<>();
        walkersOnlyPosts = new ArrayList<>();
        searchingOnlyPosts = new ArrayList<>();

        userCache = new HashMap<>(30);

        postComparator = new PostComparator();
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

    public void updateList(ArrayList<Post> list, Post post)
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

        Collections.sort(list,postComparator);
    }

    public boolean addUserToCache(User user)
    {
        userCache.put(user.get_ID(),user);

        if(userCache.size() > 20)
        {
            return false;
        }

        return true;

    }

    public HashMap<String, User> getUserCache()
    {
        return userCache;
    }

    public synchronized User findUserById(final String id)
    {
        final User[] returnUser = {null};
        boolean foundInCache = false;

        returnUser[0] = userCache.get(id);

        if(returnUser[0] == null)
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
            if(returnUser[0] != null)
            {
                userCache.put(returnUser[0].get_ID(),returnUser[0]);
            }
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


    private class PostComparator implements Comparator<Post>
    {

        @Override
        public int compare(Post post1, Post post2)
        {
            if(post1.getTimeOfPost() < post2.getTimeOfPost())
            {
                return 1;
            }
            if(post1.getTimeOfPost() > post2.getTimeOfPost())
            {
                return -1;
            }

            return 0;
        }
    }


}
