package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.inflate;

public class MessengerFragment extends Fragment {
    View thisView;
    User currentUser;
    AllThePosts allThePosts;

    RecyclerView myRecyclerView ;
    ArrayList<User> contacts;
    ContactsRecycleAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.messenger_fragment,container,false);

        currentUser = User.getInstance();
        allThePosts = AllThePosts.getInstance();

        myRecyclerView=thisView.findViewById(R.id.RecyclerContactsView);

        Toolbar toolbar = thisView.findViewById(R.id.toolbar_messenger);

        return thisView;


    }

    @Override
    public void onStart() {

        super.onStart();


        db.collection("users").document(currentUser.get_ID()).addSnapshotListener(getActivity(),new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                contacts = new ArrayList<>();

                User tempUser = documentSnapshot.toObject(User.class);
                Log.i(TAG, "ss bgoi =  " +allThePosts.findUserById(tempUser.get_ID()).get_ID()+ " "+tempUser.getChatWithUser().size());
                for(String id: tempUser.getChatWithUser())
                {
                    contacts.add(allThePosts.findUserById(id));
                    Log.i(TAG, "ss this =  " +allThePosts.findUserById(id).get_ID());
                }

                adapter = new ContactsRecycleAdapter(getActivity(),contacts);
                myRecyclerView.setAdapter(adapter);
                myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }
        });


    }
}





