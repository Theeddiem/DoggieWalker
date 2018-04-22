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

    Button email_btn;
    Button facebook_btn;
    Button google_btn;

    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;


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
                startActivityForResult(signInIntent,1);
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

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        signInClient = GoogleSignIn.getClient(getActivity(),signInOptions);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            User user = User.getInstance();
            user.set_ID(account.getId()).setEmail(account.getEmail()).
                    setFullName(account.getDisplayName()).setUserName(account.getEmail())
                    .setProfilePhoto(account.getPhotoUrl());


            Toast.makeText(getActivity(), ""+account.getPhotoUrl(), Toast.LENGTH_SHORT).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }


}
