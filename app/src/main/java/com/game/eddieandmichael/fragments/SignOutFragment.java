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
    GoogleApiClient mGoogleApiClient;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view = null;
        view = inflater.inflate(R.layout.signout_fragment,container,false);

        final User user = User.getInstance();

        profileImage = view.findViewById(R.id.signout_profileImage);
        profileName = view.findViewById(R.id.signout_userName);
        signOut_tv = view.findViewById(R.id.signout_signout_tv);

        Picasso.get().load(user.getProfilePhoto()).into(profileImage);
        profileName.setText(user.getFullName());

        signOut_tv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                        .setResultCallback(new ResultCallback<Status>()
                        {
                            @Override
                            public void onResult(@NonNull Status status)
                            {
                                if (status.isSuccess())
                                {
                                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                    user.setUserName(null).setFullName(null).setEmail(null)
                                            .set_ID(null).setProfilePhoto(null);

                                    getActivity().onBackPressed();

                                }else
                                {
                                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });



        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);


    }
}
