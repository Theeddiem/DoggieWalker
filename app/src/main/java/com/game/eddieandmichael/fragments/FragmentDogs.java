package com.game.eddieandmichael.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.eddieandmichael.classes.Dog;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.adapters.RecyclerViewAdapterForDogs;

import java.util.ArrayList;
import java.util.List;

public class FragmentDogs extends Fragment {

    View view;
    private RecyclerView myRecyclerView;
    private List<Dog> lstDogs;


    public FragmentDogs() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dogs_fragment,container,false);
        myRecyclerView = view.findViewById(R.id.dogs_recyclerview);
        RecyclerViewAdapterForDogs recyclerViewAdapterForDogs = new RecyclerViewAdapterForDogs(getContext(),lstDogs);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapterForDogs);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstDogs= new ArrayList<>();
        lstDogs.add(new Dog("Yossi", BitmapFactory.decodeResource(getResources(), R.drawable.dog1)));
        lstDogs.add(new Dog("Nir", BitmapFactory.decodeResource(getResources(), R.drawable.dog2)));
        lstDogs.add(new Dog("Max", BitmapFactory.decodeResource(getResources(), R.drawable.dog3)));
        lstDogs.add(new Dog("Michael", BitmapFactory.decodeResource(getResources(), R.drawable.dog4)));

    }

}
