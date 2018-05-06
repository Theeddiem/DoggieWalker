package com.game.eddieandmichael.activities;

import android.content.Intent;
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
import android.widget.Toast;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.fragments.*;
import com.game.eddieandmichael.services.SyncWithFirebaseService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class MainActivity extends AppCompatActivity
{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);


        Toolbar toolbar=findViewById(R.id.main_ToolBar);
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
            public boolean onNavigationItemSelected(MenuItem item)
            {
                item.setChecked(false); // change color if selceted.

                switch (item.getItemId())
                {
                    case R.id.navi_profile:
                    {
                        if(user.get_ID() == null)
                        {
                            Toast.makeText(MainActivity.this, "Login to watch profile", Toast.LENGTH_SHORT).show();
                        }else {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_fragment, new ProfileFragment(), "ProfileScreen");
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();
                        }
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
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                        if(user.get_ID() != null)
                        {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_fragment, new SignOutFragment(), "SignOut");
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();

                        }else {

                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_fragment, new LoginFragment(), "LoginScreen");
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();
                        }

                    }

                }

                return false;
            }
        });

        Intent syncServiceIntent = new Intent(this, SyncWithFirebaseService.class);
        startService(syncServiceIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
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
        user = User.getInstance();

        FirebaseAuth instance = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = instance.getCurrentUser();

        if(currentUser != null || account != null)
        {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            CollectionReference allTheUsers = firestore.collection("users");

            String accountId = "";

            if(account == null)
            {
                accountId = currentUser.getUid();
            }

            if(currentUser == null)
            {
                accountId = account.getId();
            }

            allTheUsers.whereEqualTo("_ID",accountId).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                    {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots)
                        {
                            if(documentSnapshots.isEmpty())
                            {
                                Toast.makeText(MainActivity.this, "No User Data Found", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                List<User> users = documentSnapshots.toObjects(User.class);

                                user.set_ID(users.get(0).get_ID());
                                user.setUserName(users.get(0).getUserName());
                                user.setFullName(users.get(0).getFullName());
                                user.setEmail(users.get(0).getEmail());
                                user.setProfilePhoto(users.get(0).getProfilePhoto());

                            }

                        }
                    });


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

//TODO Fix upper action bar (looks weird)
//TODO remove login option from menu when user is logged in
