package com.game.eddieandmichael.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.eddieandmichael.adapters.ContactsRecycleAdapter;
import com.game.eddieandmichael.adapters.MessageRecycleAdapter;
import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.ArrayList;

public class MessengerFragment extends android.support.v4.app.Fragment {
    View thisView;
    User currentUser;

    RecyclerView myRecyclerView ;
    ArrayList<User> contacts=new ArrayList<>();
    ContactsRecycleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        currentUser = User.getInstance();

        myRecyclerView=thisView.findViewById(R.id.RecyclerContactsView);
        adapter = new ContactsRecycleAdapter(getActivity(),contacts);
        myRecyclerView.setAdapter(adapter);
        //Contacts.add(currentUser);
        thisView = inflater.inflate(R.layout.messenger_fragment,container,false);
        return thisView;


    }




}





