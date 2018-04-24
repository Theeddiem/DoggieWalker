package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.PostViewHolder>
{

    ArrayList<Post> allThePosts;
    Context context;

    public PostRecycleAdapter(ArrayList<Post> allThePosts, Context context)
    {
        this.allThePosts = allThePosts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);

        PostViewHolder post = new PostViewHolder(view);

        return post;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
        final Post post = allThePosts.get(position);
        int day,month,year;
        day = month = year = 0;

        Uri photoUri = post.getPostOwner().getProfilePhoto();
        Picasso.get().load(photoUri).into(holder.profileImage);

        holder.profileName.setText(post.getPostOwner().getFullName());
        holder.aboutThePost.setText(post.getAboutThePost());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            day =  post.getTimeOfPost().get(Calendar.DAY_OF_MONTH);
            month = post.getTimeOfPost().get(Calendar.MONTH);
            year = post.getTimeOfPost().get(Calendar.YEAR);
        }

        holder.postDate.setText(day+"/"+month+"/"+year);

        holder.viewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, ""+post.getPostOwner().getFullName()
                        +" Is the owner of the post", Toast.LENGTH_SHORT).show();
            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, ""+post.getPostOwner().getFullName()
                        +" For the profile!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return allThePosts.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder
    {
        ImageView profileImage;
        TextView profileName;
        TextView aboutThePost;
        TextView postDate;
        Button viewPost;

        public PostViewHolder(View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.post_profileImage);
            profileName = itemView.findViewById(R.id.post_userName);
            aboutThePost = itemView.findViewById(R.id.post_aboutJob);
            postDate = itemView.findViewById(R.id.post_postDate);
            viewPost = itemView.findViewById(R.id.post_viewPost);

        }
    }


}
