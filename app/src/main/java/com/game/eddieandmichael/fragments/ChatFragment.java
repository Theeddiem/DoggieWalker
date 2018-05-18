package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.game.eddieandmichael.adapters.MessageRecycleAdapter;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ChatFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    View thisView;
    User currentUser;
    ////////////////////////////////////////////////////
    ImageView sendMessege;
    EditText messegeInput;
    Button testBtn;
    ////////////////////////////////////////////////////
    String OtherUserID;
    String OtherFullName;
    private static final String TAG = "ChatFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ////////////////////////////////////////////////////
    RecyclerView myRecyclerView ;
    ArrayList <ChatMessage> conversation=new ArrayList<>();
    MessageRecycleAdapter adapter;

////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {

        OtherUserID= getArguments().getString("UserID");
        OtherFullName = getArguments().getString("UserFullName");
        Toast.makeText(getActivity(), OtherUserID, Toast.LENGTH_SHORT).show();

        thisView = inflater.inflate(R.layout.chatwindow_fragmnet,container,false);
       // conversation.clear();

        getReferences();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(OtherFullName);
        currentUser = User.getInstance();
        sendMessege.setOnClickListener(this);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conversation.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "yo", Toast.LENGTH_SHORT).show();
            }
        });


        return thisView;
    }



    public void getReferences()
    {

        sendMessege = thisView.findViewById(R.id.sendButton);
        messegeInput = thisView.findViewById(R.id.messageArea);
        testBtn=thisView.findViewById(R.id.testBtn);

        myRecyclerView=thisView.findViewById(R.id.RecyclerListView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessageRecycleAdapter(getActivity(),conversation);
        myRecyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {

        String msgInput = messegeInput.getText().toString();
        ChatMessage message = new ChatMessage(msgInput,currentUser.get_ID(),OtherUserID);
        messegeInput.getText().clear();
        //my SideBackUp
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

        //His SideBackUp
        db.collection("Chats").document(OtherUserID+ " " + currentUser.get_ID()).
                collection(OtherFullName+ "  with " + currentUser.getFullName()).add(message)
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




    }

    @Override
    public void onStart() {
        super.onStart();


        db.collection("Chats").document(currentUser.get_ID() + " " + OtherUserID).
                collection(currentUser.getFullName() + "  with " + OtherFullName).orderBy("messageTime", Query.Direction.ASCENDING).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                    return;
                conversation.clear();
                adapter.notifyDataSetChanged();;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatMessage message = documentSnapshot.toObject(ChatMessage.class);
                    String text;
                    if(message.getCurrentUserID().equals(currentUser.get_ID()))
                    {
                        text = "                         " + message.getMessageText();
                        conversation.add(new ChatMessage(text,message.getCurrentUserID(),message.getMessageUserID()));
                    }
                    else
                        conversation.add(message);
                    adapter.notifyDataSetChanged();
                     }


            }

        });


    }





    public void load(){

        String msgInput = messegeInput.getText().toString();
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
                    conversation.add(message);
                    adapter.notifyDataSetChanged();

                }

                // Toast.makeText(getActivity(),data, Toast.LENGTH_SHORT).show();
            }
        });
    }




}

//TODO let the currentUser edit his personal information

