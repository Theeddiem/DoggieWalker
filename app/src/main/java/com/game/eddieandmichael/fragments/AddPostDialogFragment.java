package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    User currentUser;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        view = inflater.inflate(R.layout.dialog_add_post,container,false);

        allThePosts = AllThePosts.getInstance();

        currentUser = User.getInstance();

        profileImage = view.findViewById(R.id.addPost_profileImage);
        profileName = view.findViewById(R.id.addPost_profileName);

        postText = view.findViewById(R.id.addPost_postEt);

        submitBtn = view.findViewById(R.id.addPost_submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //TODO Add create a new post instance and add it to the Firestore Database

                CollectionReference collection = firestore.collection("Posts");

                if(postText.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Cant add empty post", Toast.LENGTH_SHORT).show();
                }else
                {
                    Post post = new Post(currentUser,true);

                    post.setAboutThePost(postText.getText().toString());

                    post.set_ID(UUID.randomUUID().toString());

                    allThePosts.updateList(allThePosts.getAllThePosts(),post);
                    Toast.makeText(getActivity(), "Post Added", Toast.LENGTH_SHORT).show();

                    collection.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                    {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
                            dismiss();
                        }
                    });

                }

            }
        });

        profileName.setText(currentUser.getFullName());
        Picasso.get().load(currentUser.getProfilePhoto()).into(profileImage);

        return view;
    }

}