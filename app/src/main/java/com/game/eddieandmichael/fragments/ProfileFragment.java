package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment
{
    View thisView;
    CollapsingToolbarLayout collapsingToolbar;

    ImageView profile_image;
    TextView profileName;

    User user;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        user = User.getInstance();

        thisView = inflater.inflate(R.layout.profile_fragment,container,false);

        profile_image = thisView.findViewById(R.id.profile_frag_imageProfile);
        profileName = thisView.findViewById(R.id.profile_frag_fullName);


        if(user != null)
        {
            updateUI();
        }

        return thisView;
    }

    private void updateUI()
    {
        profileName.setText(user.getFullName());

        Picasso.get()
                .load(user.getProfilePhoto())
                .into(profile_image);

    }


}
