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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SyncWithFirebaseService extends Service
{

    FirebaseFirestore firestore;
    AllThePosts allThePosts;
    User currentUser;

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
        CollectionReference collection;

        public SyncDatabases()
        {
            collection = firestore.collection("Posts");
        }

        @Override
        public void run()
        {
            while(true)
            {
                collection.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                        {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots)
                            {
                                ArrayList<Post> postsFromDatabase = (ArrayList<Post>) documentSnapshots.toObjects(Post.class);

                                for (Post post: postsFromDatabase)
                                {
                                    allThePosts.updateList(allThePosts.getAllThePosts(), post);
                                }

                            }
                        });

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent localIntent = new Intent("Refresh_Adapter");
                localBroadcastManager.sendBroadcast(localIntent);

            }

        }
    }
}
