package com.game.eddieandmichael.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.game.eddieandmichael.activities.MainActivity;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.fragments.MessengerFragment;
import com.google.android.gms.tasks.OnFailureListener;
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
     NotificationManager manager;

    String TAG="MyService";
    private static final String CHANNEL_1_ID= "msg";
    final int NOTIF_ID = 1;
    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentUser = User.getInstance();
        allThePosts = AllThePosts.getInstance();
        db = FirebaseFirestore.getInstance();
         manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.star_on);
        builder.setContentTitle("New msg");

        builder.setPriority(Notification.PRIORITY_MAX);
            if(currentUser.get_ID()!=null)
            createNotificationChannels();


    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "chat", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("this is that chat notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }

    }

    private void notificaionPrint(String OtherUserFullnameSTR,String OtherUserIdSTR,String OtherUserMsgTextSTR)
    {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.presence_online);
        builder.setContentTitle(OtherUserFullnameSTR);

        Log.i(TAG, OtherUserMsgTextSTR);
        builder.setContentText(OtherUserMsgTextSTR);
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        intent.putExtra("From", "notifyFrag");
        intent.putExtra("id",OtherUserIdSTR);
        intent.putExtra("fullname",OtherUserFullnameSTR);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_ALL;

        manager.notify(NOTIF_ID,notification);

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


        Runnable r = new Runnable() {
            @Override
            public void run()
            {

                try {
                    Thread.sleep(3500);
                    if(currentUser.get_ID()!=null)
                    db.collection("Chats").document(currentUser.get_ID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                String otherUserAmountSTR=documentSnapshot.getString("otherUserAmount");
                                OthermsgCounter =Integer.parseInt(otherUserAmountSTR);
                                Log.i(TAG, "Counter = " + OthermsgCounter);

                            }
                            else
                                OthermsgCounter=0;
                            Log.i(TAG, "Counter  = " + OthermsgCounter);

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            
             for(int i=0;i<2000;i++){

                        long futureTime=System.currentTimeMillis() +2000;
                        while (System.currentTimeMillis()<futureTime)
                        {
                            synchronized (this)
                            {

                                try {
                                    wait(futureTime-System.currentTimeMillis());
                                    if(currentUser.get_ID()!=null)
                                    db.collection("Chats").document(currentUser.get_ID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists())
                                            {
                                                String otherUserAmountSTR=documentSnapshot.getString("otherUserAmount");
                                                String OtherUserFullnameSTR=documentSnapshot.getString("otherUserFullName");
                                                String OtherUserIdSTR=documentSnapshot.getString("otherUserID");
                                                String OtherUserMsgTextSTR=documentSnapshot.getString("otherUserMessage");
                                                Log.i(TAG, "hola= " + otherUserAmountSTR +"byo = "+ String.valueOf(OthermsgCounter));
                                                if(OthermsgCounter<Integer.parseInt(otherUserAmountSTR))
                                                {
                                                    Log.i(TAG, "new msg " +otherUserAmountSTR);
                                                    OthermsgCounter=Integer.parseInt(otherUserAmountSTR);
                                                    notificaionPrint(OtherUserFullnameSTR,OtherUserIdSTR,OtherUserMsgTextSTR);
                                                 }

                                            }
                                            else
                                                Log.i(TAG, "onfail: ");
                                        }
                                    });

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
        return START_NOT_STICKY;
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









