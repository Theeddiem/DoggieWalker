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

import com.game.eddieandmichael.classes.Walker;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.adapters.RecyclerViewAdapterForWalkers;

import java.util.ArrayList;
import java.util.List;

public class FragmentWalkers extends Fragment {
    View view;

    private RecyclerView myRecyclerView;
    private List<Walker> lstWalkers;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.walkers_fragment, container, false);
        myRecyclerView = view.findViewById(R.id.walkers_recyclerview);
        RecyclerViewAdapterForWalkers recyclerViewAdapterForWalkers = new RecyclerViewAdapterForWalkers(getContext(),lstWalkers);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       myRecyclerView.setAdapter(recyclerViewAdapterForWalkers);
        lstWalkers= new ArrayList<>();
        return view;
        }
   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstWalkers= new ArrayList<>();
        lstWalkers.add(new Walker("Putin", BitmapFactory.decodeResource(getResources(), R.drawable.walker1)));
        lstWalkers.add(new Walker("Natasha", BitmapFactory.decodeResource(getResources(), R.drawable.walker2)));
        lstWalkers.add(new Walker("Moshe", BitmapFactory.decodeResource(getResources(), R.drawable.walker3)));
        lstWalkers.add(new Walker("Anna", BitmapFactory.decodeResource(getResources(), R.drawable.walker4)));



    }
}
