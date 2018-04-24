package com.game.eddieandmichael.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.fragments.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.game.eddieandmichael.doggiewalker.R;


public class MainActivity extends AppCompatActivity
{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.main_ToolBar); //add this
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        fragmentManager = getSupportFragmentManager();

        drawerLayout=findViewById(R.id.drawer_layout);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment,new MainScreen());
        fragmentTransaction.commit();



        navigationView = findViewById(R.id.main_NavigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                item.setChecked(false); // change color if selceted.

                switch (item.getItemId())
                {
                    case R.id.navi_profile:
                    {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment,new ProfileFragment(),"ProfileScreen");
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();

                        return true;
                    }

                    case R.id.navi_mainScreen:
                    {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment,new MainScreen(),"MainScreen");
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();


                        return true;
                    }

                    case R.id.navi_login:
                    {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment,new LoginFragment(),"LoginScreen");
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();

                    }

                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null)
        {
            User user = User.getInstance();
            user.set_ID(account.getId()).setEmail(account.getEmail()).
                    setFullName(account.getDisplayName()).setUserName(account.getEmail())
                    .setProfilePhoto(account.getPhotoUrl());
        }else
        {
            User user = User.getInstance();
        }

    }

    @Override
    public void onBackPressed()
    {
        MainScreen mainScreen = (MainScreen) fragmentManager.findFragmentByTag("MainScreen");

        if(mainScreen != null && mainScreen.isVisible())
        {
            finish();
        }else
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment,new MainScreen(),"MainScreen");
            fragmentTransaction.commit();
        }

    }
}

//TODO Add more profile information
//TODO Add Post class
//TODO Fix the main screen to show posts (After last todo)