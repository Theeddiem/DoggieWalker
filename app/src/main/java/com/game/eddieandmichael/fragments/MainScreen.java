package com.game.eddieandmichael.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.adapters.PostRecycleAdapter;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;

import java.util.ArrayList;

public class MainScreen extends Fragment
{
    User user;
    AllThePosts allThePosts;

    RecyclerView recyclerView;
    PostRecycleAdapter allPostsAdapter;
    PostRecycleAdapter walkersPostsAdapter;
    PostRecycleAdapter searchingPostsAdapter;
    ArrayList<Post> listOfPosts;

    TextView filterTv;

    int textViewCounter = 0;

    FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();


        View view = null;
        view = inflater.inflate(R.layout.main_screen, container,false);

        Toolbar toolbar = view.findViewById(R.id.appbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryDark));
        }
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));


        user = User.getInstance();

        allThePosts = AllThePosts.getInstance();

        filterTv = view.findViewById(R.id.mainScreen_textViewFilter);


        filterTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textViewCounter++;
                textViewCounter = textViewCounter%3;

                if(textViewCounter == 0)
                {
                    filterTv.setText("All Posts");
                   recyclerView.swapAdapter(allPostsAdapter,false);
                }else if(textViewCounter == 1)
                {
                    filterTv.setText("Walkers Only");
                    recyclerView.swapAdapter(walkersPostsAdapter,false);
                }else
                {
                    filterTv.setText("Searching Only");
                    recyclerView.swapAdapter(searchingPostsAdapter,false);
                }

            }
        });


        recyclerView = view.findViewById(R.id.mainscreen_RecyclerViewPost);

        walkersPostsAdapter = new PostRecycleAdapter(allThePosts.getWalkersOnlyPosts(),getContext());
        allPostsAdapter = new PostRecycleAdapter(allThePosts.getAllThePosts(),getContext());
        searchingPostsAdapter = new PostRecycleAdapter(allThePosts.getSearchingOnlyPosts(),getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(allPostsAdapter);

        floatingActionButton = view.findViewById(R.id.mainScreen_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(user.get_ID() == null)
                {
                    Toast.makeText(getActivity(), "Login to add posts", Toast.LENGTH_SHORT).show();
                }else
                {
                    showAddPostDialog();
                }
            }
        });


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(adapterReceiver, new IntentFilter("Refresh_Adapter"));


        return view;
    }


    private void showAddPostDialog()
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag("postDialog");

        if (prev != null)
        {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);

        AddPostDialogFragment postDialog = new AddPostDialogFragment();

        postDialog.show(transaction,"postDialog");


    }


    private BroadcastReceiver adapterReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            allPostsAdapter.notifyDataSetChanged();

            for(int i = 0; i < allPostsAdapter.getItemCount(); i++)
            {
                allPostsAdapter.notifyItemChanged(i,null);
            }
        }
    };


}

