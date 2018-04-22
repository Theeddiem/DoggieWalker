package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.game.eddieandmichael.doggiewalker.R;

import org.w3c.dom.Text;

public class SignupFragment extends Fragment
{
    Text fullName_et;
    Text userName_et;
    Text email_et;
    Text password_et;
    Text rePassword_et;

    Button email_btn;
    Button facebook_btn;
    Button google_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.signup_fragment,container,false);
        getReferences(view);


        return view;
    }

    private void getReferences(View view)
    {
        fullName_et = view.findViewById(R.id.signup_fullName_et);
        userName_et = view.findViewById(R.id.signUp_username_et);

    }
}
