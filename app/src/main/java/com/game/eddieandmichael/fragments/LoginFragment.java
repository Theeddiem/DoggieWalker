package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.eddieandmichael.doggiewalker.R;

public class LoginFragment extends Fragment
{
    Button login_btn;
    TextView signUp_tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.login_fragment,container,false);

        signUp_tv = view.findViewById(R.id.frag_signUpText);
        login_btn = view.findViewById(R.id.login_login_btn);


        signUp_tv.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment,new SignupFragment());
                transaction.commit();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //TODO Add firebase emailLogin

            }
        });


        return view;

    }
}
