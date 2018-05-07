package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
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

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.PostViewHolder>
{
    AllThePosts AllThePostsSingleton;
    ArrayList<Post> allThePosts;
    Context context;
    Calendar calendar;

    public PostRecycleAdapter(ArrayList<Post> allThePosts, Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = new GregorianCalendar();
        }
        this.allThePosts = allThePosts;
        this.context = context;
        AllThePostsSingleton = AllThePosts.getInstance();
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
        final User user = AllThePostsSingleton.findUserById(post.getPostOwner_ID());

        String uri = user.getProfilePhoto();

        if(uri != null)
        {
            Uri photoUri = Uri.parse(uri);
            Picasso.get().load(photoUri).into(holder.profileImage);

        }


        holder.profileName.setText(user.getFullName());
        holder.aboutThePost.setText(post.getAboutThePost());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.setTimeInMillis(post.getTimeOfPost());


        day =  calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year =calendar.get(Calendar.YEAR);
        }else
        {
            day = java.util.Calendar.DAY_OF_MONTH;
            month = java.util.Calendar.MONTH;
            year = java.util.Calendar.YEAR;
        }

        holder.postDate.setText(day+"/"+month+"/"+year);



        holder.profileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, ""+user.getFullName()
                        +" For the profile!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.places.setText(post.getPlacesOfPost());
        holder.prices.setText(post.getPrice());

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
        TextView prices;
        TextView places;

        public PostViewHolder(View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.post_profileImage);
            profileName = itemView.findViewById(R.id.post_userName);
            aboutThePost = itemView.findViewById(R.id.post_aboutJob);
            postDate = itemView.findViewById(R.id.post_postDate);
            prices = itemView.findViewById(R.id.post_price);
            places = itemView.findViewById(R.id.post_places);

        }
    }


}


//TODO different colors for seekers and walkers