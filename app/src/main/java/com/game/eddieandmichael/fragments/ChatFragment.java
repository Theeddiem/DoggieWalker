package com.game.eddieandmichael.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;



public class ChatFragment extends android.support.v4.app.Fragment {

    View thisView;
    User currentUser;
    ImageView sendImageButton;
    EditText msgInput;
    ScrollView scrollView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.chatwindow_fragment,container,false);
        getReferences();
        currentUser = User.getInstance();
        return thisView;
    }





    public void getReferences()
    {

         sendImageButton = thisView.findViewById(R.id.sendButton);
         msgInput = thisView.findViewById(R.id.messageArea);
         scrollView = thisView.findViewById(R.id.scrollView);

    }
}

//TODO let the currentUser edit his personal information

