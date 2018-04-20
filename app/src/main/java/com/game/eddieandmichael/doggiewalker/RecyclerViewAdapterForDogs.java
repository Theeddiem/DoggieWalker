package com.game.eddieandmichael.doggiewalker;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapterForDogs extends RecyclerView.Adapter<RecyclerViewAdapterForDogs.MyViewHolder>{

    Context mContext;
    List<Dog> mData;

    public RecyclerViewAdapterForDogs(Context context, List<Dog> lstDogs) {
        this.mContext=context;
        this.mData=lstDogs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.dog_row,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getOwnerName());
        holder.photo.setImageBitmap(mData.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView photo;
        public MyViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.dog_name);
            photo=itemView.findViewById(R.id.img_dog);
        }
    }

}
