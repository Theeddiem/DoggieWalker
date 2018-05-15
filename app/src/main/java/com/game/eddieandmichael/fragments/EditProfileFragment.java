package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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

    User currentUser;


    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle bundle)
    {
        setHasOptionsMenu(true);
        View view = null;

        currentUser = User.getInstance();

        view = inflater.inflate(R.layout.edit_profile_fragment,container,false);
        getReferences(view);

        profilePhotoUrl = getArguments().getString("photoUrl");
        fullName = getArguments().getString("fullName");
        userName = getArguments().getString("userName");
        about = getArguments().getString("about");

        updateUI();


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
                        String firebaseID = userInFirebase.getId();

                        currentUser.setFullName(fullName_ET.getText().toString());
                        currentUser.setUserName(userName_ET.getText().toString());
                        currentUser.setAboutUser(about_ET.getText().toString());

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
}
