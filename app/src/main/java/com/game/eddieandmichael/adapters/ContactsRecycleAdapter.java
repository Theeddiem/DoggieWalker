package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.activities.MainActivity;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.fragments.ChatFragment;
import com.game.eddieandmichael.fragments.MainScreen;
import com.game.eddieandmichael.fragments.MessengerFragment;
import com.game.eddieandmichael.fragments.ProfileFragment;
import com.game.eddieandmichael.fragments.ViewPhotoFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactsRecycleAdapter extends RecyclerView.Adapter<ContactsRecycleAdapter.ViewHolder>  {
    User currentUser = User.getInstance();
    Calendar calendar;
    ArrayList<User> ContactsArraylist;
    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    public ContactsRecycleAdapter(Context context,  ArrayList<User> ContactsArraylist) {
        this.ContactsArraylist = ContactsArraylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactsRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messenger_row_layout,parent,false);
        ContactsRecycleAdapter.ViewHolder viewholder = new ContactsRecycleAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRecycleAdapter.ViewHolder holder, final int position)
    {
        User user = ContactsArraylist.get(position);

        holder.fullname.setText(user.getFullName());

        Picasso.get().load(user.getProfilePhoto()).into(holder.profilePhoto);

        holder.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userById=ContactsArraylist.get(position);
                String Uid= userById.get_ID();
                MainActivity mainActivity = (MainActivity) context;


                Bundle bundle = new Bundle();
                bundle.putString("id",Uid);

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, profileFragment, "profileFragment").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                User userById=ContactsArraylist.get(position);

                String Uid= userById.get_ID();
                String UserfullName=userById.getFullName();

                Bundle args = new Bundle();
                args.putString("UserID", Uid);
                args.putString("UserFullName",UserfullName);

                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(args);

                 fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, chatFragment, "ChatScreen").addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

    }

    @Override
    public int getItemCount() {
        return ContactsArraylist.size();
    }



    public class ViewHolder    extends RecyclerView.ViewHolder{
        TextView fullname;
        ImageView profilePhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.messenger_fullname);
            profilePhoto=itemView.findViewById(R.id.messenger_prophoto_row);
        }



    }
}
