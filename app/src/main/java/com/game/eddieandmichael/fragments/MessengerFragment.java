package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.eddieandmichael.adapters.ContactsRecycleAdapter;
import com.game.eddieandmichael.adapters.RecyclerItemClickListener;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.ArrayList;
import java.util.List;

public class MessengerFragment extends Fragment {
    View thisView;
    User currentUser;
    AllThePosts allThePosts;

    RecyclerView myRecyclerView ;
    ArrayList<User> contacts = new ArrayList<>();
    ContactsRecycleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.messenger_fragment,container,false);

        currentUser = User.getInstance();
        allThePosts = AllThePosts.getInstance();

        myRecyclerView=thisView.findViewById(R.id.RecyclerContactsView);
        final List<String> chatWithUser = currentUser.getChatWithUser();

        adapter = new ContactsRecycleAdapter(getActivity(),contacts);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Toast.makeText(getContext(), ""+chatWithUser.size(), Toast.LENGTH_SHORT).show();
        for(String id: chatWithUser)
        {
            contacts.add(allThePosts.findUserById(id));
        }


        adapter.notifyDataSetChanged();


       /* myRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String id = chatWithUser.get(position);
                        allThePosts.findUserById(id);
                                User userById=allThePosts.findUserById(id);
                        String Uid= userById.get_ID();
                        String UserfullName=userById.getFullName();



                        Fragment fr=new ChatFragment();
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("UserID", Uid);
                        args.putString("UserFullName",UserfullName);
                        fr.setArguments(args);

                        ft.replace(R.id.main_fragment, fr,"ChatScreen").addToBackStack(null).
                                commit();
                    }
                })
        );*/

        return thisView;


    }




}





