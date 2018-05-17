package com.game.eddieandmichael.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class ChatFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    View thisView;
    User currentUser;
    ImageView sendMessege;
    EditText messegeInput;
    ScrollView scrollView;

    private static final String TAG = "ChatFragment";
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference chatsCollectionRef=db.collection("Chats");
    private DocumentReference newMsgRef = db.collection("Chats").document();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.chatwindow_fragment,container,false);
        getReferences();
        currentUser = User.getInstance();
        sendMessege.setOnClickListener(this);
        return thisView;
    }






    public void getReferences()
    {

         sendMessege = thisView.findViewById(R.id.sendButton);
         messegeInput = thisView.findViewById(R.id.messageArea);
         scrollView = thisView.findViewById(R.id.scrollView);


    }

    @Override
    public void onClick(View v) {

        String msgInput = messegeInput.getText().toString();

        Map<String, Object> messageMap = new HashMap<>();

        ChatMessage message = new ChatMessage(msgInput,currentUser.get_ID(),"110400685642351129051"); //to do change it to the user your chating with.

        messageMap.put("essageText",message.getMessageText());
        messageMap.put("currentUserID",message.getCurrentUserID());
        messageMap.put("messageUserID",message.getMessageUserID());
        messageMap.put("timeStamp",message.getMessageTime());

        db.collection("Chats").document()
                .set(messageMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Msg sent ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Msg failed", e);
                    }
                });
    }
}

//TODO let the currentUser edit his personal information

