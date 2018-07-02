package com.game.eddieandmichael.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginFragment extends Fragment
{
    Button login_btn;
    TextView signUp_tv;
    EditText emailTv;
    EditText passwordTv;
    ImageView logoImage;


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

        signUp_tv = view.findViewById(R.id.frag_signUpText);
        login_btn = view.findViewById(R.id.login_login_btn);
        logoImage = view.findViewById(R.id.login_logoImage);
        emailTv = view.findViewById(R.id.login_email_et);
        passwordTv = view.findViewById(R.id.login_password_et);


        signUp_tv.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment,new SignupFragment());
                transaction.commit();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
                                                    }else
                                                    {
                                                        List<User> users = documentSnapshots.toObjects(User.class);

                                                        user.set_ID(currentUser.getUid());
                                                        user.setUserName(users.get(0).getUserName());
                                                        user.setFullName(users.get(0).getFullName());
                                                        user.setEmail(users.get(0).getEmail());
                                                        user.setProfilePhoto(users.get(0).getProfilePhoto());


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
}
