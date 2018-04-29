package com.game.eddieandmichael.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class SignupFragment extends Fragment
{
    EditText fullName_et;
    EditText userName_et;
    EditText email_et;
    EditText password_et;
    EditText rePassword_et;
    Uri photoUri;

    Button email_btn;
    Button facebook_btn;
    Button google_btn;
    Button addProfilePhoto_btn;

    TextView profilePhotoURI_tv;

    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    final int PICK_IMAGE_REQUEST = 2;
    final int GOOGLE_SIGNUP_REQUEST = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.signup_fragment,container,false);
        getReferences(view);

        google_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent signInIntent =  signInClient.getSignInIntent();
                startActivityForResult(signInIntent,GOOGLE_SIGNUP_REQUEST);
            }
        });


        email_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //TODO Add Firebase Email signUp

            }
        });


        addProfilePhoto_btn.setOnClickListener(new View.OnClickListener()
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

    private void getReferences(View view)
    {
        fullName_et = view.findViewById(R.id.signup_fullName_et);
        userName_et = view.findViewById(R.id.signUp_username_et);
        email_et = view.findViewById(R.id.signup_email_et);
        password_et = view.findViewById(R.id.signup_password_et);
        rePassword_et = view.findViewById(R.id.signup_rePassword_et);

        email_btn = view.findViewById(R.id.signup_email_btn);
        facebook_btn = view.findViewById(R.id.signup_facebook_btn);
        google_btn = view.findViewById(R.id.signup_google_btn);
        addProfilePhoto_btn = view.findViewById(R.id.signup_addPhoto_btn);

        profilePhotoURI_tv = view.findViewById(R.id.signup_profilePhoto_tv);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        signInClient = GoogleSignIn.getClient(getActivity(),signInOptions);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == GOOGLE_SIGNUP_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK)
        {
            photoUri = data.getData();
            profilePhotoURI_tv.setText(photoUri.getLastPathSegment());
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            User user = User.getInstance();
            user.set_ID(account.getId()).setEmail(account.getEmail()).
                    setFullName(account.getDisplayName()).setUserName(account.getEmail())
                    .setProfilePhoto(account.getPhotoUrl());

            getActivity().onBackPressed();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }


}
