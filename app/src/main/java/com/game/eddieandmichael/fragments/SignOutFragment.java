package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class SignOutFragment extends Fragment
{

    ImageView profileImage;
    TextView profileName;
    TextView signOut_tv;
    GoogleSignInOptions signInOptions;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view = null;
        view = inflater.inflate(R.layout.signout_fragment,container,false);

        User user = User.getInstance();

        profileImage = view.findViewById(R.id.signout_profileImage);
        profileName = view.findViewById(R.id.signout_userName);
        signOut_tv = view.findViewById(R.id.signout_signout_tv);

        Picasso.get().load(user.getProfilePhoto()).into(profileImage);
        profileName.setText(user.getFullName());

        signOut_tv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail().build();

                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                        .build();

                mGoogleApiClient.connect();

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).addStatusListener(new PendingResult.StatusListener()
                {
                    @Override
                    public void onComplete(Status status)
                    {
                        if(status.isSuccess())
                        {
                            Toast.makeText(getContext(), "SignOut Succsesfuly", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



        return view;
    }
}
