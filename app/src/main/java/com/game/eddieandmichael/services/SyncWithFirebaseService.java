package com.game.eddieandmichael.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SyncWithFirebaseService extends Service
{

    FirebaseFirestore firestore;
    AllThePosts allThePosts;
    User currentUser;

    private int postsLimit = 30;

    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate()
    {
        allThePosts = AllThePosts.getInstance();

        firestore = FirebaseFirestore.getInstance();

        currentUser = User.getInstance();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

//        AddUsersToDatabase addUsers = new AddUsersToDatabase();
//        addUsers.start();

        SyncDatabases syncDatabases = new SyncDatabases();
        syncDatabases.start();

        CollectionReference collection = firestore.collection("Posts");

        collection.addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots,FirebaseFirestoreException e)
            {


            }
        });


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class SyncDatabases extends Thread
    {
        CollectionReference postCollection;
        CollectionReference userCollection;

        public SyncDatabases()
        {
            postCollection = firestore.collection("Posts");
            userCollection = firestore.collection("users");
        }

        @Override
        public void run()
        {
            while((allThePosts.getAllThePosts().size() <= postsLimit))
            {
                postCollection.orderBy("timeOfPost", Query.Direction.DESCENDING)
                        .limit(postsLimit)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                        {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots)
                            {
                                ArrayList<Post> postsFromDatabase = (ArrayList<Post>) documentSnapshots.toObjects(Post.class);

                                for (Post post: postsFromDatabase)
                                {
                                    if(!allThePosts.getUserCache().containsKey(post.get_ID()))
                                    {
                                        addUserToCache(post.getPostOwner_ID());
                                    }
                                    allThePosts.updateList(allThePosts.getAllThePosts(), post);
                                    if(post.isAWalker())
                                    {
                                        allThePosts.updateList(allThePosts.getWalkersOnlyPosts(),post);
                                    }else
                                    {
                                        allThePosts.updateList(allThePosts.getSearchingOnlyPosts(),post);

                                    }
                                }

                            }
                        });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent localIntent = new Intent("Refresh_Adapter");
                localBroadcastManager.sendBroadcast(localIntent);

            }



        }

        void addUserToCache(String id)
        {
            userCollection.whereEqualTo("_ID",id).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                    {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots)
                        {
                            List<User> users = documentSnapshots.toObjects(User.class);
                            allThePosts.addUserToCache(users.get(0));
                        }
                    });

        }
    }

    private class AddUsersToDatabase extends Thread
    {
        CollectionReference collection;

        public AddUsersToDatabase()
        {
            collection = firestore.collection("users");
        }

        @Override
        public void run()
        {
            collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
            {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots)
                {
                    List<User> users = documentSnapshots.toObjects(User.class);


                    for(User user: users)
                    {
                        if(!allThePosts.addUserToCache(user))
                        {
                            break;
                        }
                    }

                }
            });

        }
    }
}


//TODO enable service again if user want more posts
//TODO Remove removed posts from other devices