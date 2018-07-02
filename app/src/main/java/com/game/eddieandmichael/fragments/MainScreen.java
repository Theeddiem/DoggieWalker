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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.adapters.PostRecycleAdapter;
import com.game.eddieandmichael.classes.AllThePosts;
import com.game.eddieandmichael.classes.Post;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

    EditText searchPostET;
    PostRecycleAdapter filterPostsAdapter;
    ArrayList<Post> filterPosts;

    int textViewCounter = 0;

    FloatingActionButton floatingActionButton;

    int searchOption = 0;

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

        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.appbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         ActionBar actionBar= ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryDark));
        }
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));


        user = User.getInstance();

        allThePosts = AllThePosts.getInstance();

        filterTv = view.findViewById(R.id.mainScreen_textViewFilter);
        searchPostET = view.findViewById(R.id.mainScreen_search_EditText);
        filterPosts = new ArrayList<>();
        filterPostsAdapter = new PostRecycleAdapter(filterPosts,getContext());


        filterTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textViewCounter++;
                textViewCounter = textViewCounter%3;
                updatePostsList();

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

    private void updatePostsList()
    {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_screen_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen_menu_search:
            {
                if(searchPostET.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Enter Values to search", Toast.LENGTH_SHORT).show();
                }else
                {
                    FilterPosts();
                }

                return true;
            }

            case R.id.mainScreen_menu_searchUsers:
            {
                searchOption = 0;
                searchPostET.setHint("Search For User: ");
                searchPostET.setText("");
                searchPostET.setInputType(InputType.TYPE_CLASS_TEXT);

                return true;
            }

            case R.id.mainScreen_menu_searchPlaces:
            {
                searchOption = 1;
                searchPostET.setHint("Search For Places: ");
                searchPostET.setText("");
                searchPostET.setInputType(InputType.TYPE_CLASS_TEXT);

                return true;
            }

            case R.id.mainScreen_menu_searchPrices:
            {
                searchOption = 2;
                searchPostET.setHint("Search For Prices: ");
                searchPostET.setText("");
                searchPostET.setInputType(InputType.TYPE_CLASS_NUMBER);

                return true;
            }


        }

        return false;
    }

    //0 - Users, 1- Places, 2- Prices
    private void FilterPosts()
    {
        filterPosts.clear();
        updatePostsList();

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        //by UserName
        if(searchOption == 0)
        {
            String userName;
            firestore.collection("users")
                    .whereEqualTo("fullName",searchPostET.getText().toString())
                    .addSnapshotListener(new EventListener<QuerySnapshot>()
                    {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e)
                        {
                            if(!documentSnapshots.isEmpty())
                            {
                            User user = documentSnapshots.toObjects(User.class).get(0);

                                firestore.collection("Posts").whereEqualTo("postOwner_ID",user.get_ID())
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e)
                                            {
                                                List<Post> posts = documentSnapshots.toObjects(Post.class);
                                                filterPosts.addAll(posts);

                                                recyclerView.swapAdapter(filterPostsAdapter,false);

                                            }
                                        });
                            }else
                            {
                                Toast.makeText(getContext(), "No User Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


            //By Places
        }else if(searchOption == 1)
        {
            firestore.collection("Posts").whereEqualTo("placesOfPost",searchPostET.getText().toString())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                        {
                            if(!documentSnapshots.isEmpty())
                            {
                                List<Post> posts = documentSnapshots.toObjects(Post.class);
                                filterPosts.addAll(posts);
                                recyclerView.swapAdapter(filterPostsAdapter,false);


                            }else
                            {
                                Toast.makeText(getContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            //by Prices
        }else if(searchOption == 2)
        {
            int price = 0;
            price =  Integer.parseInt(searchPostET.getText().toString());

            firestore.collection("Posts").whereLessThan("price",price)
                    .addSnapshotListener(new EventListener<QuerySnapshot>()
                    {
                        @Override
                        public void onEvent( QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                        {
                            if(!documentSnapshots.isEmpty())
                            {

                                List<Post> posts = documentSnapshots.toObjects(Post.class);
                                filterPosts.addAll(posts);
                                recyclerView.swapAdapter(filterPostsAdapter,false);

                            }else
                            {
                                Toast.makeText(getContext(), "No Matching Prices Found", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }


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

