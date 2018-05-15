package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment
{
    View thisView;
    ImageView profile_image;
    TextView profileName;
    FloatingActionButton floatingActionButton;

    AllThePosts allThePosts;
    User currentUser;


    String userId;
    String userName;
    String profilePhotoUri;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.profile_fragment,container,false);

        getReferences();

        allThePosts = AllThePosts.getInstance();

        currentUser = User.getInstance();
        if(getArguments() != null)
        {
            userId = getArguments().getString("id");
            User userById = allThePosts.findUserById(userId);

            profilePhotoUri = userById.getProfilePhoto();
            userName = userById.getFullName();

            //Use the "userId" veriable to get all the data we need from the post's currentUser

            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //TODO Start Chat Fragment
                    Toast.makeText(getContext(), "Fab Click!", Toast.LENGTH_SHORT).show();
                }
            });

        }else
        {
            profilePhotoUri = currentUser.getProfilePhoto();
            userName = currentUser.getUserName();
            floatingActionButton.setVisibility(View.GONE);
        }


        updateUI();

        return thisView;
    }

    private void updateUI() {
        profileName.setText(userName);

        Picasso.get()
                .load(profilePhotoUri)
                .into(profile_image);


    }


    public void getReferences()
    {
        profile_image = thisView.findViewById(R.id.profile_frag_imageProfile);
        profileName = thisView.findViewById(R.id.profile_frag_fullName);
        floatingActionButton = thisView.findViewById(R.id.profile_Fab);

    }
}

//TODO let the currentUser edit his personal information