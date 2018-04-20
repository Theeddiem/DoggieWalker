package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.eddieandmichael.classes.Walker;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.List;

public class RecyclerViewAdapterForWalkers  extends RecyclerView.Adapter<RecyclerViewAdapterForWalkers.MyViewHolder>{

    Context mContext;
    List<Walker> mData;

    public RecyclerViewAdapterForWalkers(Context context, List<Walker> lstWalkers) {
        this.mContext=context;
        this.mData=lstWalkers;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterForWalkers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.walker_row,parent,false);
        RecyclerViewAdapterForWalkers.MyViewHolder viewHolder = new RecyclerViewAdapterForWalkers.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterForWalkers.MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getwalkerName());
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

            name=itemView.findViewById(R.id.walker_name);
            photo=itemView.findViewById(R.id.img_walker);
        }
    }

}