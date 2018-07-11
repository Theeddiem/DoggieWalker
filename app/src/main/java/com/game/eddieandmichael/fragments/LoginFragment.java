package com.game.eddieandmichael.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static android.support.constraint.Constraints.TAG;

public class LoginFragment extends Fragment
{
    private static final int RC_SIGN_IN = 1;
    Button login_btn;
    Button  googleLogin_btn;
    TextView signUp_tv;
    EditText emailTv;
    EditText passwordTv;
    ImageView logoImage;
    FirebaseAuth firebaseAuth;
    Uri photoUri;




    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    FirebaseFirestore firestoreDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    final int PICK_IMAGE_REQUEST = 2;
    final int GOOGLE_SIGNUP_REQUEST = 1;
    User user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.login_fragment,container,false);

        firebaseAuth=FirebaseAuth.getInstance();
        signUp_tv = view.findViewById(R.id.frag_signUpText);
        login_btn = view.findViewById(R.id.login_login_btn);
        googleLogin_btn=view.findViewById(R.id.login_google_btn);
        logoImage = view.findViewById(R.id.login_logoImage);
        emailTv = view.findViewById(R.id.login_email_et);
        passwordTv = view.findViewById(R.id.login_password_et);


        signUp_tv.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment,new SignupFragment()).addToBackStack(null);;
                transaction.commit();




            }
        });




        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        signInClient = GoogleSignIn.getClient(getActivity(),signInOptions);

        firestoreDatabase = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference().child("usersPhotos");


        googleLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent =  signInClient.getSignInIntent();
                startActivityForResult(signInIntent,GOOGLE_SIGNUP_REQUEST);



            }
        });








        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(emailTv.getText().toString())&& TextUtils.isEmpty( passwordTv.getText().toString())) //null doesn't work need TextUtils
                {
                    Toast.makeText(getActivity(), "Enter Email and Password Please", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(passwordTv.getText().toString())) //null doesn't work need TextUtils
                    Toast.makeText(getActivity(), "Enter Password Please", Toast.LENGTH_SHORT).show();

               else if (TextUtils.isEmpty(emailTv.getText().toString())) //null doesn't work need TextUtils
                        Toast.makeText(getActivity(), "Enter Email Please", Toast.LENGTH_SHORT).show();

                else
                {

                    firebaseAuth.signInWithEmailAndPassword(emailTv.getText().toString(), passwordTv.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                            {
                                @Override
                                public void onSuccess(AuthResult authResult)
                                {
                                    final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    final User user = User.getInstance();

                                    user.set_ID(currentUser.getUid());

                                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                                    CollectionReference allTheUsers = firestore.collection("users");

                                    allTheUsers.whereEqualTo("_ID",currentUser.getUid()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                                            {
                                                @Override
                                                public void onSuccess(QuerySnapshot documentSnapshots)
                                                {
                                                    if(documentSnapshots.isEmpty())
                                                    {
                                                        Toast.makeText(getActivity(), "No User Data Found", Toast.LENGTH_SHORT).show();
                                                        Log.i(TAG, "onSuccess:  3check ");
                                                    }else
                                                    {
                                                        List<User> users = documentSnapshots.toObjects(User.class);

                                                        user.set_ID(currentUser.getUid());
                                                        user.setUserName(users.get(0).getUserName());
                                                        user.setFullName(users.get(0).getFullName());
                                                        user.setEmail(users.get(0).getEmail());
                                                        user.setProfilePhoto(users.get(0).getProfilePhoto());
                                                        user.setChatWithUser(users.get(0).getChatWithUser());

                                                        Log.i(TAG, "onSuccess:  4check ");
                                                        getActivity().onBackPressed();

                                                    }

                                                }
                                            });



                                }
                            });
                }

            }
        });


        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == GOOGLE_SIGNUP_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }



    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        final boolean userInDatabase = false;
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            user = User.getInstance();

            final CollectionReference allTheUsers = firestoreDatabase.collection("users");

            allTheUsers.whereEqualTo("_ID",account.getId())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
            {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots)
                {
                    List<User> users = documentSnapshots.toObjects(User.class);
                    if(documentSnapshots.isEmpty()) {
                        user.set_ID(account.getId());
                        user.setEmail(account.getEmail());
                        user.setFullName(account.getDisplayName());
                        user.setUserName(account.getEmail());
                        user.setProfilePhoto(account.getPhotoUrl().toString());
                        List<String> clearList = new ArrayList<>();
                        user.setChatWithUser(clearList);
                        allTheUsers.document(user.get_ID()).set(user);
                        Log.i(TAG, "onSuccess:  2check ");
                    }
                    else
                    {
                        User Firebaseuser = users.get(0);
                        user.set_ID(Firebaseuser.get_ID());
                        user.setEmail(Firebaseuser.getEmail());
                        user.setFullName(Firebaseuser.getFullName());
                        user.setUserName(Firebaseuser.getUserName());
                        user.setProfilePhoto(Firebaseuser.getProfilePhoto());
                        user.setAboutUser(Firebaseuser.getAboutUser());
                        user.setChatWithUser(Firebaseuser.getChatWithUser());
                        Log.i(TAG, "onSuccess:  1check ");

                    }

                }
            });


            user.set_ID(account.getId());
            user.setEmail(account.getEmail());
            user.setFullName(account.getDisplayName());
            user.setUserName(account.getEmail());
            user.setProfilePhoto(account.getPhotoUrl().toString());


            getActivity().onBackPressed();

        } catch (ApiException e) {

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();


    }




}
