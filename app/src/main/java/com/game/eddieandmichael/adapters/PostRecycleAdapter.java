package com.game.eddieandmichael.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.activities.MainActivity;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.fragments.AddPostDialogFragment;
import com.game.eddieandmichael.fragments.ViewPhotoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.PostViewHolder> {
    AllThePosts AllThePostsSingleton;
    ArrayList<Post> allThePosts;
    Context context;
    Calendar calendar;
    User currentUser;

    public PostRecycleAdapter(ArrayList<Post> allThePosts, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = new GregorianCalendar();
        }
        this.allThePosts = allThePosts;
        this.context = context;
        AllThePostsSingleton = AllThePosts.getInstance();
        currentUser = User.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);

        PostViewHolder post = new PostViewHolder(view);

        return post;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final Post post = allThePosts.get(position);
        int day, month, year, hour, minute;
        final User user = AllThePostsSingleton.findUserById(post.getPostOwner_ID());

        String profilePhotoUri = user.getProfilePhoto();
        final String postPhotoUri = post.getPostsPhotos();

        if (profilePhotoUri != null) {
            Uri photoUri = Uri.parse(profilePhotoUri);
            Picasso.get().load(photoUri).into(holder.profileImage);
        }

        if (postPhotoUri != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(postPhotoUri).resize(200, 200)
                    .into(holder.postImage);


            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uri", postPhotoUri);

                    MainActivity mainActivity = (MainActivity) context;

                    ViewPhotoFragment viewPhoto = new ViewPhotoFragment();

                    viewPhoto.setArguments(bundle);

                    android.app.FragmentTransaction transaction = mainActivity.getFragmentManager()
                            .beginTransaction();

                    viewPhoto.show(transaction, "ViewPhoto");

                }
            });
        }

        holder.profileName.setText(user.getFullName());
        holder.aboutThePost.setText(post.getAboutThePost());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.setTimeInMillis(post.getTimeOfPost());


            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            day = java.util.Calendar.DAY_OF_MONTH;
            month = java.util.Calendar.MONTH;
            year = java.util.Calendar.YEAR;
            hour = java.util.Calendar.HOUR;
            minute = java.util.Calendar.MINUTE;

        }

        holder.postDate.setText(day + "/" + month + "/" + year + " | " + hour + ":" + minute);


        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + user.getFullName()
                        + " For the profile!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.places.setText(post.getPlacesOfPost());
        holder.prices.setText(post.getPrice());

        if (post.getPostOwner_ID().equals(currentUser.get_ID())) {
            holder.moreBtn.setVisibility(View.VISIBLE);
        } else {
            holder.moreBtn.setVisibility(View.GONE);
        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.moreBtn);
                popup.inflate(R.menu.post_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.postMenu_editPost: {
                                editPost(post.get_ID(), position);

                                return true;
                            }
                            case R.id.postMenu_removePost: {
                                removePost(post.get_ID(), position);

                                return true;
                            }
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

    }

    private void editPost(String id, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("about", allThePosts.get(position).getAboutThePost());
        bundle.putString("price", allThePosts.get(position).getPrice());
        bundle.putString("places", allThePosts.get(position).getPlacesOfPost());
        bundle.putString("Id", allThePosts.get(position).get_ID());
        bundle.putBoolean("edit", true);

        AddPostDialogFragment addPost = new AddPostDialogFragment();
        addPost.setArguments(bundle);

        MainActivity mainActivity = (MainActivity) context;

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager()
                .beginTransaction();

        android.app.Fragment prev = mainActivity.getFragmentManager().findFragmentByTag("postDialog");

        addPost.show(transaction, "PostDialog");


    }

    private void removePost(final String id, final int position) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final CollectionReference collection = firestore.collection("Posts");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Alert!");
        builder.setMessage("Are you sure you want to delete this post?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                final StorageReference storageRef = firebaseStorage.getReference();

                storageRef.child("postsPhotos/" + id + "/"+id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    collection.whereEqualTo("_ID", id).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot documentSnapshots) {
                                                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                                                    String firestoreId = documents.get(0).getId();

                                                    collection.document(firestoreId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid)
                                                                {
                                                                    Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                                                                    notifyDataSetChanged();
                                                                    allThePosts.remove(position);
                                                                }
                                                            });
                                                }
                                            });

                                } else
                                    {
                                        collection.whereEqualTo("_ID", id).get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot documentSnapshots) {
                                                        List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                                                        String firestoreId = documents.get(0).getId();

                                                        collection.document(firestoreId).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid)
                                                                    {
                                                                        Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                                                                        notifyDataSetChanged();
                                                                        allThePosts.remove(position);
                                                                    }
                                                                });
                                                    }
                                                });

                                }

                            }
                        });


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    @Override
    public int getItemCount() {
        return allThePosts.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        ImageView postImage;
        TextView profileName;
        TextView aboutThePost;
        TextView postDate;
        TextView prices;
        TextView places;
        ImageButton moreBtn;

        public PostViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.post_profileImage);
            profileName = itemView.findViewById(R.id.post_userName);
            aboutThePost = itemView.findViewById(R.id.post_aboutJob);
            postDate = itemView.findViewById(R.id.post_postDate);
            prices = itemView.findViewById(R.id.post_price);
            places = itemView.findViewById(R.id.post_places);
            moreBtn = itemView.findViewById(R.id.post_more_btn);
            postImage = itemView.findViewById(R.id.post_ImageView);

        }
    }


}


//TODO different colors for seekers and walkers
//TODO Add pictures to photos