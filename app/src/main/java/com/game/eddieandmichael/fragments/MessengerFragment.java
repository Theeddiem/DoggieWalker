package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        adapter = new ContactsRecycleAdapter(getActivity(),contacts);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


      //  Toast.makeText(getContext(), ""+chatWithUser.size(), Toast.LENGTH_SHORT).show();
        for(String id: chatWithUser)
        {
            contacts.add(allThePosts.findUserById(id));
        }
        adapter.notifyDataSetChanged();
        return thisView;


    }

  /*  @Override
    public void onStart() {
        super.onStart();


        db.collection("Chats").document(currentUser.get_ID()).
                collection(currentUser.get_ID() + "  with " + otherUser.get_ID()).orderBy("messageTime", Query.Direction.ASCENDING)
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)
                            return;
                        conversation.clear();
                        adapter.notifyDataSetChanged();;

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            ChatMessage message = documentSnapshot.toObject(ChatMessage.class);

                            conversation.add(message);
                            adapter.notifyDataSetChanged();

                        }
                        int lastpos = myRecyclerView.getAdapter().getItemCount() - 1; /// scroll to last item
                        if (lastpos < 0)
                            return;
                        else /// scroll to last item
                            myRecyclerView.smoothScrollToPosition(lastpos);


                    }

                });


    }*/




}





