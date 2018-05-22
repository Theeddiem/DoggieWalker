package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.game.eddieandmichael.doggiewalker.R;

public class LottieAnimation extends DialogFragment
{

    LottieAnimationView animationView;
    int animationToPlay;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = null;

        view = inflater.inflate(R.layout.lottie_animation_fragment,container,false);

        animationToPlay = getArguments().getInt("animation");

        animationView = view.findViewById(R.id.lottie_Animation);

        this.setCancelable(false);

        animationView.setAnimation(animationToPlay);

        animationView.playAnimation();

        return view;
    }


    @Override
    public void onStop()
    {
        animationView.cancelAnimation();
        super.onStop();
    }
}
