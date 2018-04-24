package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.eddieandmichael.adapters.ViewPagerAdapter;
import com.game.eddieandmichael.doggiewalker.R;

public class MainScreen extends Fragment
{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = null;

        view = inflater.inflate(R.layout.main_screen, container,false);

        tabLayout=view.findViewById(R.id.tablayout_id);
        viewPager=view. findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new FragmentDogs(),"");
        //adapter.AddFragment(new FragmentWalkers(),"");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_pets_black_24dp);
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);

        return view;
    }
}
