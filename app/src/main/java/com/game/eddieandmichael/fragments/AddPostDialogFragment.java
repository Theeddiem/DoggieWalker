package com.game.eddieandmichael.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddPostDialogFragment extends DialogFragment
{

    final static int PICK_IMAGE_REQUEST = 1;
    String photoURI;
    Uri photoFromGalleryUri;

    AllThePosts allThePosts;

    ImageView photoImageView;
    ImageView profileImage;
    TextView profileName;
    Button submitBtn;
    ImageButton addPhotoBtn;
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
    String postID;
    boolean isEdit = false;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth;

    public AddPostDialogFragment(){}

    @Nullable
    @Override



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = null;
        view = inflater.inflate(R.layout.dialog_add_post,container,false);

        if(getArguments() != null)
        {
            isEdit = getArguments().getBoolean("edit");
            pricePost = getArguments().getString("price");
            aboutPost = getArguments().getString("about");
            locationPost = getArguments().getString("places");
            postID = getArguments().getString("Id");

        }

        allThePosts = AllThePosts.getInstance();

        currentUser = User.getInstance();

        photoImageView = view.findViewById(R.id.addPost_photoImageView);
        profileImage = view.findViewById(R.id.addPost_profileImage);
        profileName = view.findViewById(R.id.addPost_profileName);

        postText = view.findViewById(R.id.dialogPost_looking_et);

        submitBtn = view.findViewById(R.id.addPost_submitBtn);
        addPhotoBtn = view.findViewById(R.id.addPost_AddPhotoBtn);

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

                final CollectionReference collection = firestore.collection("Posts");

                if(postText.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Cant add empty post", Toast.LENGTH_SHORT).show();
                }else {

                    if (isEdit)
                    {
                        collection.whereEqualTo("_ID",postID).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                                {
                                    @Override
                                    public void onSuccess(QuerySnapshot documentSnapshots)
                                    {
                                        DocumentSnapshot documentSnapshot = documentSnapshots.getDocuments().get(0);

                                        final String postfireBaseId = documentSnapshot.getId();

                                        Post oldPost = documentSnapshot.toObject(Post.class);

                                        final Post newPost = new Post();

                                        newPost.set_ID(oldPost.get_ID());
                                        newPost.setPostOwner_ID(oldPost.getPostOwner_ID());
                                        newPost.setAboutThePost(postText.getText().toString());
                                        newPost.setPlacesOfPost(placesText.getText().toString());
                                        newPost.setPrice(priceText.getText().toString());
                                        newPost.setPostsPhotos(oldPost.getPostsPhotos());
                                        newPost.setTimeOfPost(oldPost.getTimeOfPost());
                                        newPost.setHasPhoto(oldPost.isHasPhoto());

                                        collection.document(postfireBaseId).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                                {
                                                    @Override
                                                    public void onSuccess(Void aVoid)
                                                    {
                                                        collection.add(newPost)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference)
                                                                    {
                                                                        Toast.makeText(getContext(), "Post Updated", Toast.LENGTH_SHORT).show();
                                                                        dismiss();
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });


                    } else {
                        final Post post = new Post(currentUser.get_ID(), iswalker);
                        post.setPlacesOfPost(placesText.getText().toString());
                        if (priceText.getText().toString().equals("")) {
                            post.setPrice("Unspecified");
                        } else {
                            post.setPrice(priceText.getText().toString());
                        }


                        post.setAboutThePost(postText.getText().toString());

                        post.set_ID(UUID.randomUUID().toString());

                        if(photoFromGalleryUri != null)
                        {

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            String firebasePath = ("postsPhotos/"+post.get_ID()+"/"+post.get_ID());

                            StorageReference reference = storage.getReference(firebasePath);

                            reference.putFile(photoFromGalleryUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                    {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            Uri uri = taskSnapshot.getDownloadUrl();
                                            post.setPostsPhotos(uri.toString());
                                            post.setHasPhoto(true);

                                            collection.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getActivity(), "Post Added", Toast.LENGTH_SHORT).show();
                                                    dismiss();
                                                }
                                            });

                                        }
                                    });



                        }else
                        {
                            post.setPostsPhotos(null);
                            collection.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(), "Post Added", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });

                        }
                    }
                }



            }
        });

        profileName.setText(currentUser.getFullName());
        Picasso.get().load(currentUser.getProfilePhoto()).into(profileImage);


        if(isEdit)
        {
            postText.setText(aboutPost);
            priceText.setText(pricePost);
            placesText.setText(locationPost);
            submitBtn.setText("Update Post");

        }


        addPhotoBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK)
        {
            photoFromGalleryUri = data.getData();
            photoImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(photoFromGalleryUri).into(photoImageView);

        }
    }

}

//TODO set new post by the currentUser to the top of the page