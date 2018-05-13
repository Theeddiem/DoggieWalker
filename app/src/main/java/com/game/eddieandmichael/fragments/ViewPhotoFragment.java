package com.game.eddieandmichael.fragments;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.eddieandmichael.doggiewalker.R;
import com.squareup.picasso.Picasso;

public class ViewPhotoFragment extends DialogFragment
{
    ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view;

        view = inflater.inflate(R.layout.viewphoto_fragment,container,false);

        image = view.findViewById(R.id.view_photoFragment);

        LinearLayout linearLayout = view.findViewById(R.id.view_photoFragment_layout);

        String photoSrc = getArguments().getString("uri");

        Picasso.get().load(photoSrc).resize(1000,1000)
                .into(image);


        return view;

    }
}
