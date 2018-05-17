package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    View thisView;
    User currentUser;

    String otherUserID;
    AllThePosts allThePosts;


    ImageView sendMessege;
    EditText messegeInput;
    ListView listView;
    ArrayList <String> conversation =new ArrayList<>();
    ArrayAdapter<String> listviewAdapater ;

    private static final String TAG = "ChatFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatsCollectionRef = db.collection("Chats");
    private DocumentReference newMsgRef = db.collection("Chats").document();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {


        String OtherUserID = getArguments().getString("UserID");
        String OtherFullName = getArguments().getString("UserFullName");

        thisView = inflater.inflate(R.layout.chatwindow_fragment,container,false);
        listviewAdapater = new ArrayAdapter<>( getActivity(), android.R.layout.simple_list_item_1,conversation);;
        getReferences();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(OtherFullName);
        currentUser = User.getInstance();
        sendMessege.setOnClickListener(this);


        return thisView;
    }


    public void getReferences()
    {

        sendMessege = thisView.findViewById(R.id.sendButton);
        messegeInput = thisView.findViewById(R.id.messageArea);
        listView = thisView.findViewById(R.id.ListView);
        listView.setAdapter(listviewAdapater);


    }


     public void load(){
        String msgInput = messegeInput.getText().toString();
        conversation.add(msgInput);
         listviewAdapater.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {

        String msgInput = messegeInput.getText().toString();
        Map<String, Object> messageMap = new HashMap<>();
        ChatMessage message = new ChatMessage(msgInput,currentUser.get_ID(),"110400685642351129051"); //to do change it to the user your chating with.
        load();
        messageMap.put("messageText",message.getMessageText());
        messageMap.put("currentUserID",message.getCurrentUserID());
        messageMap.put("messageUserID",message.getMessageUserID());
        messageMap.put("timeStamp",message.getMessageTime());
        db.collection("Chats").document()
                .set(messageMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { Log.d(TAG, "Msg sent ");     }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {  Log.w(TAG, "Msg failed", e);
                    }
                });

    }


}

//TODO let the currentUser edit his personal information

