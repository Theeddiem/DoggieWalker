package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.eddieandmichael.classes.ChatMessage;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.ArrayList;

public class MessageRecycleAdapter  extends RecyclerView.Adapter<MessageRecycleAdapter.ViewHolder> {
    User currentUser = User.getInstance();
    Calendar calendar;
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
    public void onBindViewHolder(MessageRecycleAdapter.ViewHolder holder, int position)
    {
        holder.messageInput.setText(ChatMessageList.get(position).getMessageText());
        int hour, minute ;
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
       //     calendar.setTimeInMillis(ChatMessageList.get(position).getMessageTime());
      //      hour = calendar.get(Calendar.HOUR_OF_DAY);
        //    minute = calendar.get(Calendar.MINUTE);
       // } else {
            hour = java.util.Calendar.HOUR;
            minute = java.util.Calendar.MINUTE;

       // }

        holder.messageTime.setText(hour + ":" + minute);

       if(currentUser.get_ID().equals(ChatMessageList.get(position).getCurrentUserID()))
       {
           holder.messageRelativeLayout.setBackgroundResource(R.drawable.rect_mycolor);

           RelativeLayout.LayoutParams layoutParams =
                   new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);




         //  FrameLayout frameLayout
         //  setLayoutParams(new FrameLayout.LayoutParams(100,100);
          // this.layout


       }
       else {
           holder.messageRelativeLayout.setBackgroundResource(R.drawable.rect_hiscolor);

       }



    }



    @Override
    public int getItemCount() {
        return ChatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
                TextView messageInput ;
                TextView messageTime;
                RelativeLayout messageRelativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            messageRelativeLayout=itemView.findViewById(R.id.messageRelativeLayout);
            messageInput= itemView.findViewById(R.id.messageText);
            messageTime =itemView.findViewById(R.id.messageTime);
        }
    }

  }



