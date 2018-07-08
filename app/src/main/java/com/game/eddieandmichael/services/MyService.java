package com.game.eddieandmichael.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MyService  extends Service {

    User currentUser;
    FirebaseFirestore db;
    AllThePosts allThePosts;
    CollectionReference collectionReference;

    @Override
    public void onCreate() {
        super.onCreate();
        allThePosts = AllThePosts.getInstance();

        db = FirebaseFirestore.getInstance();

        currentUser = User.getInstance();
        Toast.makeText(this, "on create", Toast.LENGTH_SHORT).show();
        Log.i("CounterService","on create");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentUser = User.getInstance();
        if(currentUser.getFullName()!=null)
            Log.i("ss", "onStartCommand: startService"+currentUser.getFullName());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "on destroy", Toast.LENGTH_SHORT).show();
        Log.i("CounterService","on destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SyncDatabases extends Thread{

        @Override
        public void run() {

        }


    }
}

