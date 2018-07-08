package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.eddieandmichael.adapters.ContactsRecycleAdapter;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MessengerFragment extends Fragment {
    View thisView;
    User currentUser;
    AllThePosts allThePosts;

    RecyclerView myRecyclerView ;
    ArrayList<User> contacts = new ArrayList<>();
    ContactsRecycleAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.messenger_fragment,container,false);

        currentUser = User.getInstance();
        allThePosts = AllThePosts.getInstance();

        myRecyclerView=thisView.findViewById(R.id.RecyclerContactsView);
        List<String> chatWithUser = currentUser.getChatWithUser();


        for(String id: chatWithUser)
        {
            contacts.add(allThePosts.findUserById(id));
            Log.i(TAG, " this =  " +allThePosts.findUserById(id).get_ID());
        }

        adapter = new ContactsRecycleAdapter(getActivity(),contacts);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();

        Toolbar toolbar = thisView.findViewById(R.id.toolbar_messenger);
      /*  toolbar.setTitle("Chats");*/





        return thisView;


    }





}





