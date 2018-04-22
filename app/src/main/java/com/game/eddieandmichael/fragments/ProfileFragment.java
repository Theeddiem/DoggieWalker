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

        collapsingToolbar = thisView.findViewById(R.id.fragment_profile_collapsingToolBar);

        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.holo_blue_dark));

        profile_image = thisView.findViewById(R.id.profile_frag_imageProfile);

        //Testing for setting up the profile Image
        profile_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));

        profileName = thisView.findViewById(R.id.profile_userFirstName);
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
