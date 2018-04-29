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

import com.game.eddieandmichael.adapters.PostRecycleAdapter;
import com.game.eddieandmichael.classes.Dog;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.game.eddieandmichael.adapters.RecyclerViewAdapterForDogs;

import java.util.ArrayList;
import java.util.List;

public class FragmentDogs extends Fragment {

    View view;
    private RecyclerView myRecyclerView;
    private List<Dog> lstDogs;

    private ArrayList<Post> allThePosts;

    public FragmentDogs() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.dogs_fragment,container,false);

        myRecyclerView = view.findViewById(R.id.dogs_recyclerview);

        allThePosts = new ArrayList<>();
        lstDogs= new ArrayList<>();
        updateAdapter();

        PostRecycleAdapter postAdapter = new PostRecycleAdapter(allThePosts,getActivity());
        RecyclerViewAdapterForDogs recyclerViewAdapterForDogs = new RecyclerViewAdapterForDogs(getContext(),lstDogs);

        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myRecyclerView.setAdapter(postAdapter);

        return view;

    }

    private void updateAdapter()
    {
        allThePosts.add(new Post(User.getInstance(),true));
        allThePosts.add(new Post(User.getInstance(),true));
        allThePosts.add(new Post(User.getInstance(),true));
        allThePosts.add(new Post(User.getInstance(),true));
        allThePosts.add(new Post(User.getInstance(),true));
        allThePosts.add(new Post(User.getInstance(),true));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }


}
