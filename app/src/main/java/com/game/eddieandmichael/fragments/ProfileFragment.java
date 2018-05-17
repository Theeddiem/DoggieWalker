package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView aboutUser;

    AllThePosts allThePosts;
    User currentUser;


    String userId;
    String userName;
    String profilePhotoUri;
    String aboutUserString;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle bundle)
    {
        thisView = inflater.inflate(R.layout.profile_fragment,container,false);

        getReferences();

        allThePosts = AllThePosts.getInstance();

        currentUser = User.getInstance();
        if(getArguments() != null)
        {
            userId = getArguments().getString("id");
            final User userById = allThePosts.findUserById(userId);
            Toast.makeText(getActivity(), userById.get_ID(), Toast.LENGTH_SHORT).show();
            profilePhotoUri = userById.getProfilePhoto();
            userName = userById.getFullName();
            aboutUserString = userById.getAboutUser();

            //Use the "userId" veriable to get all the data we need from the post's currentUser

            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //TODO Start Chat Fragment
                    String Uid= userById.get_ID();
                    String UserfullName=userById.getFullName();
                    Fragment fr=new ChatFragment();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("UserID", Uid);
                    args.putString("UserFullName",UserfullName);
                    fr.setArguments(args);
                    ft.replace(R.id.main_fragment, fr,"ChatScreen").addToBackStack(null).
                    commit();
                }
            });

        }else
        {
            profilePhotoUri = currentUser.getProfilePhoto();
            userName = currentUser.getUserName();
            aboutUserString = currentUser.getAboutUser();

            floatingActionButton.setImageResource(R.drawable.ic_create);
            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle b = new Bundle();
                    EditProfileFragment editProfileFragment = new EditProfileFragment();

                    b.putString("userName",currentUser.getUserName());
                    b.putString("fullName",currentUser.getFullName());
                    b.putString("about",currentUser.getAboutUser());
                    b.putString("photoUrl",currentUser.getProfilePhoto());

                    editProfileFragment.setArguments(b);

                    editProfileFragment.setSharedElementEnterTransition(new AutoTransition());
                    editProfileFragment.setEnterTransition(new AutoTransition());

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addSharedElement(profile_image,"transition_profilePhoto");
                    transaction.replace(R.id.main_fragment,editProfileFragment);
                    transaction.commit();
                }
            });
        }


        updateUI();

        return thisView;
    }

    private void updateUI() {
        profileName.setText(userName);

        aboutUser.setText(aboutUserString);

        Picasso.get()
                .load(profilePhotoUri)
                .into(profile_image);


    }


    public void getReferences()
    {
        profile_image = thisView.findViewById(R.id.profile_frag_imageProfile);
        profileName = thisView.findViewById(R.id.profile_frag_fullName);
        floatingActionButton = thisView.findViewById(R.id.profile_Fab);
        aboutUser = thisView.findViewById(R.id.profile_aboutUser);

    }
}



//TODO let the currentUser edit his personal information