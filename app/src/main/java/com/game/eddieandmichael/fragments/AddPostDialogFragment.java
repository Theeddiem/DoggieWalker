package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddPostDialogFragment extends DialogFragment
{

    AllThePosts allThePosts;

    ImageView profileImage;
    TextView profileName;
    Button submitBtn;
    EditText postText;
    EditText priceText;
    EditText placesText;
    Switch isWalkerSwitch;

    boolean iswalker = false;
    User currentUser;

    //Edit Post Fields
    String aboutPost;
    String pricePost;
    String locationPost;
    boolean isEdit = false;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public AddPostDialogFragment()
    {


    }

    @Nullable
    @Override



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = null;
        view = inflater.inflate(R.layout.dialog_add_post,container,false);

        isEdit = getArguments().getBoolean("edit");

        allThePosts = AllThePosts.getInstance();

        currentUser = User.getInstance();

        profileImage = view.findViewById(R.id.addPost_profileImage);
        profileName = view.findViewById(R.id.addPost_profileName);

        postText = view.findViewById(R.id.dialogPost_looking_et);

        submitBtn = view.findViewById(R.id.addPost_submitBtn);

        placesText = view.findViewById(R.id.dialogPost_places_et);
        priceText = view.findViewById(R.id.dialogPost_prices_et);

        isWalkerSwitch = view.findViewById(R.id.addPost_isWalker_switch);

        isWalkerSwitch.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                iswalker = b;
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                CollectionReference collection = firestore.collection("Posts");

                if(postText.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Cant add empty post", Toast.LENGTH_SHORT).show();
                }else
                {
                    Post post = new Post(currentUser.get_ID(),iswalker);
                    post.setPlacesOfPost(placesText.getText().toString());
                    if(priceText.getText().toString().equals(""))
                    {
                        post.setPrice("Unspecified");
                    }else
                    {
                        post.setPrice(priceText.getText().toString());
                    }

                    post.setAboutThePost(postText.getText().toString());

                    post.set_ID(UUID.randomUUID().toString());

                    allThePosts.updateList(allThePosts.getAllThePosts(),post);

                    collection.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                    {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
                            Toast.makeText(getActivity(), "Post Added", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });

                }

            }
        });

        profileName.setText(currentUser.getFullName());
        Picasso.get().load(currentUser.getProfilePhoto()).into(profileImage);


        if(isEdit)
        {
            pricePost = getArguments().getString("price");
            aboutPost = getArguments().getString("about");
            locationPost = getArguments().getString("places");

            postText.setText(aboutPost);
            priceText.setText(pricePost);
            placesText.setText(locationPost);
            submitBtn.setText("Update Post");
        }





        return view;
    }

}

//TODO finish Edit post and update firestorm with the updated Post