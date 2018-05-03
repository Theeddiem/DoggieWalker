package com.game.eddieandmichael.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class SyncWithFirebaseService extends Service
{

    FirebaseFirestore firestore;
    AllThePosts allThePosts;
    User currentUser;

    @Override
    public void onCreate()
    {
        allThePosts = AllThePosts.getInstance();

        firestore = FirebaseFirestore.getInstance();

        currentUser = User.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class SyncDatabases extends Thread
    {


        public SyncDatabases()
        {

        }


    }
}
