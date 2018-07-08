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
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


public class MyService  extends Service {

    User currentUser;
    FirebaseFirestore db;
    AllThePosts allThePosts;
    int OthermsgCounter;


    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        allThePosts = AllThePosts.getInstance();


        db = FirebaseFirestore.getInstance();
        currentUser = User.getInstance();
 /*       db.collection("Chats").document(currentUser.get_ID()+ " " +"QFlArXBKZyNJRMjVJzp6FHTYcJZ2").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String temp=documentSnapshot.getString("otherUserAmount");
                if(temp!=null)
                OthermsgCounter = Integer.parseInt(temp);
                Log.i(TAG, "FirstEvent: "+ temp);
            }
        });*/


        Log.i("CounterService", "EventCreated");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("CounterService", "on destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       /* db.collection("Chats").document(currentUser.get_ID()+ " " +"QFlArXBKZyNJRMjVJzp6FHTYcJZ2").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String otherUserAmountSTR=documentSnapshot.getString("otherUserAmount");
                Log.i(TAG, "MyServiceonEvent: "+ otherUserAmountSTR);
            }
        });*/

        Runnable r = new Runnable() {
            @Override
            public void run() {
                    for(int i=0;i<2000;i++){
                        long futureTime=System.currentTimeMillis() +2000;
                        while (System.currentTimeMillis()<futureTime)
                        {
                            synchronized (this)
                            {
                                try {
                                    wait(futureTime-System.currentTimeMillis());
                                    db.collection("Chats").document(currentUser.get_ID() + " QFlArXBKZyNJRMjVJzp6FHTYcJZ2").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists())
                                            {
                                                String otherUserAmountSTR=documentSnapshot.getString("otherUserAmount");
                                                Log.i(TAG, "run=  " +otherUserAmountSTR);

                                            }
                                            else
                                                Log.i(TAG, "onfail: ");
                                        }
                                    });
                                    Log.i(TAG, "run: run "+ i);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

            }
        };

        Thread eddieThread=new Thread(r);
        eddieThread.start();
        return START_STICKY;
    }

/*    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        db.collection("Chats").document(currentUser.get_ID()+ " " +"QFlArXBKZyNJRMjVJzp6FHTYcJZ2").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String otherUserAmountSTR=documentSnapshot.getString("otherUserAmount");
                Log.i(TAG, "onEvent: "+ otherUserAmountSTR);
            }
        });




    }*/

}









