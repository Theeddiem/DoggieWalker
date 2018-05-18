package com.game.eddieandmichael.fragments;

import android.nfc.Tag;
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
import android.widget.Toast;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;


public class ChatFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    View thisView;
    User currentUser;

    ImageView sendMessege;
    EditText messegeInput;

    ListView listView;
    ArrayList <String> conversation =new ArrayList<>();
    ArrayAdapter<String> listviewAdapater ;

    String OtherUserID;
    String OtherFullName;
    private static final String TAG = "ChatFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //CollectionReference ChatsRef = db.collection("Chats");




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {

        OtherUserID= getArguments().getString("UserID");
        OtherFullName = getArguments().getString("UserFullName");
        Toast.makeText(getActivity(), OtherUserID, Toast.LENGTH_SHORT).show();
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
         db.collection("Chats").document(currentUser.get_ID()+ " " + OtherUserID).
                 collection(currentUser.getFullName()+ "  with " + OtherFullName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String data= "";
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        ChatMessage message = documentSnapshot.toObject(ChatMessage.class);

                        String text = message.getMessageText();
                        String from = message.getCurrentUserID();
                        String to = message.getMessageUserID();

                        data+=text+" " + "from " + from +"\n" + "to " + to;
                        conversation.add(text);
                        listviewAdapater.notifyDataSetChanged();

                    }

                // Toast.makeText(getActivity(),data, Toast.LENGTH_SHORT).show();
             }
         });
    }


    @Override
    public void onClick(View v) {

        String msgInput = messegeInput.getText().toString();
        ChatMessage message = new ChatMessage(msgInput,currentUser.get_ID(),OtherUserID);


        db.collection("Chats").document(currentUser.get_ID()+ " " + OtherUserID).
                collection(currentUser.getFullName()+ "  with " + OtherFullName).add(message)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: WORKS ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
        load();
           }




}

//TODO let the currentUser edit his personal information

