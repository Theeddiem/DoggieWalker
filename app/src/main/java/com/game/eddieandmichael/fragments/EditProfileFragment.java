package com.game.eddieandmichael.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends Fragment
{

    ImageView profilePhoto_IV;
    EditText fullName_ET;
    EditText userName_ET;
    EditText about_ET;
    FloatingActionButton floatingActionButton;

    String profilePhotoUrl;
    String fullName;
    String userName;
    String about;
    Uri newPhotoUri;
    int toolBarColor;

    User currentUser;

    final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle bundle)
    {
        setHasOptionsMenu(true);
        View view = null;

        currentUser = User.getInstance();

        view = inflater.inflate(R.layout.edit_profile_fragment,container,false);
        getReferences(view);

        Toolbar toolbar = view.findViewById(R.id.editProfile_toolBar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        profilePhotoUrl = getArguments().getString("photoUrl");
        fullName = getArguments().getString("fullName");
        userName = getArguments().getString("userName");
        about = getArguments().getString("about");
        toolBarColor = getArguments().getInt("toolBarColor");

        toolbar.setBackgroundColor(toolBarColor);

        updateUI();

        floatingActionButton.setOnClickListener(new View.OnClickListener()
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

    private void updateUI()
    {
        Picasso.get().load(profilePhotoUrl).into(profilePhoto_IV);
        fullName_ET.setText(fullName);
        userName_ET.setText(userName);


    }

    private void getReferences(View view)
    {
        profilePhoto_IV = view.findViewById(R.id.editProfile_photo);
        fullName_ET = view.findViewById(R.id.editProfile_Name);
        userName_ET = view.findViewById(R.id.editProfile_userName);
        about_ET = view.findViewById(R.id.editProfile_about);
        floatingActionButton = view.findViewById(R.id.editProfile_fab);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.editprofile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.editProfile_done)
        {
            updateUserInFirestore();
        }

        return false;
    }

    private void updateUserInFirestore()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final CollectionReference collection = firestore.collection("users");



        collection.whereEqualTo("_ID",currentUser.get_ID()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots)
                    {
                        DocumentSnapshot userInFirebase = documentSnapshots.getDocuments().get(0);
                        final String firebaseID = userInFirebase.getId();

                        currentUser.setFullName(fullName_ET.getText().toString());
                        currentUser.setUserName(userName_ET.getText().toString());
                        currentUser.setAboutUser(about_ET.getText().toString());

                        if(newPhotoUri != null)
                        {
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            final StorageReference storageRef = firebaseStorage.getReference();

                            storageRef.child("postsPhotos/"+currentUser.get_ID()).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task)
                                        {
                                                storageRef.child("profilePhotos/"+currentUser.get_ID()).putFile(newPhotoUri)
                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                            {

                                                                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                                                currentUser.setProfilePhoto(downloadUrl);
                                                                collection.document(firebaseID).delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>()
                                                                        {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid)
                                                                            {
                                                                                collection.add(currentUser)
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                                                                        {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentReference documentReference)
                                                                                            {
                                                                                                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                                                                                getActivity().onBackPressed();
                                                                                            }
                                                                                        });

                                                                            }
                                                                        });

                                                            }
                                                        });

                                        }
                                    });


                        }else
                        {
                            collection.document(firebaseID).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            collection.add(currentUser)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                                    {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference)
                                                        {
                                                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                                            getActivity().onBackPressed();
                                                        }
                                                    });

                                        }
                                    });
                        }



                    }
                });

    }

    private void uploadNewProfilePhoto()
    {
        final boolean[] isFinished = {false};
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReference();

        storageRef.child("postsPhotos/"+currentUser.get_ID()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        storageRef.child("postsPhotos/"+currentUser.get_ID()).putFile(newPhotoUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                        currentUser.setProfilePhoto(downloadUrl);
                                        isFinished[0] = true;

                                    }
                                });

                    }
                });

        while(!isFinished[0])
        {
            return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       if(requestCode == PICK_IMAGE_REQUEST)
       {
           if(resultCode == getActivity().RESULT_OK)
           {
               newPhotoUri = data.getData();
               Picasso.get().load(newPhotoUri).into(profilePhoto_IV);
           }
       }

    }
}
