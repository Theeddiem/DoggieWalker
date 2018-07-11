package com.game.eddieandmichael.fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.activities.MainActivity;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;


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

    int toolBarColor = 0;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();


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

                    if(currentUser.get_ID() == null)
                    {
                        Toast.makeText(getActivity(), "Login to send message", Toast.LENGTH_SHORT).show();
                    }else if(currentUser.get_ID()!=userById.get_ID())
                    {

                        String Uid= userById.get_ID();
                        String UserfullName=userById.getFullName();

                   /*     currentUser.addUserToChat(Uid);
                        Log.i(TAG, currentUser.get_ID()+"  other =   " + Uid);

                        db.collection("users").document(currentUser.get_ID()).set(currentUser); // and this chat room


                        userById.addUserToChat(currentUser.get_ID());
                        db.collection("users").document(userById.get_ID()).set(userById);*/   //update other user chat room

                        Log.i(TAG, userById.get_ID()+"  me =   " + currentUser.get_ID());


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
                    b.putInt("toolBarColor",toolBarColor);

                    editProfileFragment.setArguments(b);

                    editProfileFragment.setSharedElementEnterTransition(new AutoTransition());
                    editProfileFragment.setEnterTransition(new AutoTransition());

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addSharedElement(profile_image,"transition_profilePhoto");
                    transaction.replace(R.id.main_fragment,editProfileFragment).addToBackStack(null);
                    transaction.commit();
                }
            });
        }

        updateUI();

      //  new changeColor().execute(); //cause bug that crush app and deletes user.

        return thisView;
    }

    private void updateUI()
    {
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



  /*  private class changeColor extends AsyncTask<Void,Void,Void>
    {
        int color = 0;
        int defaultColor;
        int defultFabColor;
        Palette p;

        ActionBar supportActionBar;

        public changeColor()
        {
            supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
            defaultColor = getResources().getColor(R.color.colorPrimaryDark);
            defultFabColor = getResources().getColor(R.color.deep_orange_500);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            if(p.getDarkVibrantColor(defaultColor) != defaultColor)
            {

                supportActionBar.setBackgroundDrawable(new ColorDrawable(p.getDarkVibrantColor(defaultColor)));
                getActivity().getWindow().setStatusBarColor(p.getDarkVibrantColor(defaultColor));
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(p.getMutedColor(defultFabColor)));

                toolBarColor = p.getDarkVibrantColor(defaultColor);

            }

            if(p.getDarkMutedColor(defaultColor) != defaultColor)
            {
                supportActionBar.setBackgroundDrawable(new ColorDrawable(p.getDarkMutedColor(defaultColor)));
                getActivity().getWindow().setStatusBarColor(p.getDarkMutedColor(defaultColor));
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(p.getMutedColor(defultFabColor)));

            }


        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            Bitmap bitmap = null;
            try {
                bitmap = Picasso.get().load(profilePhotoUri).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            p = Palette.from(bitmap).generate();
            return null;
        }
    }*/


}

