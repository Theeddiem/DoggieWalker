package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactsRecycleAdapter extends RecyclerView.Adapter<ContactsRecycleAdapter.ViewHolder> {
    User currentUser = User.getInstance();
    Calendar calendar;
    ArrayList<User> ContactsArraylist;
    Context context;

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
    public void onBindViewHolder(@NonNull ContactsRecycleAdapter.ViewHolder holder, int position)
    {
        User user = ContactsArraylist.get(position);

        holder.fullname.setText(user.getFullName());

        Picasso.get().load(user.getProfilePhoto()).into(holder.profilePhoto);

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
