package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.ArrayList;

public class MessageRecycleAdapter  extends RecyclerView.Adapter<MessageRecycleAdapter.ViewHolder> {

    ArrayList<ChatMessage> ChatMessageList;
    Context context;

    public MessageRecycleAdapter(Context context,  ArrayList<ChatMessage> ChatMessageList) {
        this.ChatMessageList = ChatMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout,parent,false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecycleAdapter.ViewHolder holder, int position) {
        holder.messageInput.setText(ChatMessageList.get(position).getMessageText());
    }

    @Override
    public int getItemCount() {
        return ChatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
                EditText messageInput ;

        public ViewHolder(View itemView) {
            super(itemView);
            messageInput= itemView.findViewById(R.id.messageText);
        }
    }

  }



