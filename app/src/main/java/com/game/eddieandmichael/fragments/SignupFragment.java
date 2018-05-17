package com.game.eddieandmichael.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.eddieandmichael.classes.User;
import com.game.eddieandmichael.doggiewalker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class SignupFragment extends Fragment
{
    EditText fullName_et;
    EditText userName_et;
    EditText email_et;
    EditText password_et;
    EditText rePassword_et;
    Uri photoUri;

    Button email_btn;
    Button facebook_btn;
    Button google_btn;
    Button addProfilePhoto_btn;

    TextView profilePhotoURI_tv;

    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestoreDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    final int PICK_IMAGE_REQUEST = 2;
    final int GOOGLE_SIGNUP_REQUEST = 1;
    User user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.signup_fragment,container,false);


        getReferences(view);

        google_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent signInIntent =  signInClient.getSignInIntent();
                startActivityForResult(signInIntent,GOOGLE_SIGNUP_REQUEST);
            }
        });


        email_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(checkFields())
                {
                    final String email = email_et.getText().toString();
                    final String fullName = fullName_et.getText().toString();
                    final String userName = userName_et.getText().toString();
                    String password = password_et.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {

                                public void onComplete( Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        final User user = User.getInstance();
                                        user.setEmail(email);
                                        user.setFullName(fullName);
                                        user.setUserName(userName);
                                        user.set_ID(firebaseAuth.getUid());

                                        String firebasePath = ("profilePhotos/"+user.get_ID());

                                        storageReference = firebaseStorage.getReference(firebasePath);

                                        if(photoUri != null)
                                        {
                                            storageReference.putFile(photoUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                                    {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                        {
                                                            Uri uri = taskSnapshot.getDownloadUrl();
                                                            user.setProfilePhoto(uri.toString());

                                                            firestoreDatabase.collection("users")
                                                                    .add(user)
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                                                                    {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentReference> task)
                                                                        {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                Toast.makeText(getActivity(), "Signup success", Toast.LENGTH_SHORT).show();

                                                                            }else
                                                                            {
                                                                                Toast.makeText(getActivity(), "Signup Failed", Toast.LENGTH_SHORT).show();

                                                                            }

                                                                        }
                                                                    });

                                                        }
                                                    });
                                        }else
                                        {
                                            firestoreDatabase.collection("users")
                                                    .add(user)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(getActivity(), "Signup success", Toast.LENGTH_SHORT).show();

                                                            }else
                                                            {
                                                                Toast.makeText(getActivity(), "Signup Failed", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });
                                        }


                                        getActivity().onBackPressed();

                                    }else
                                    {
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }


            }

            private boolean checkFields()
            {
                if(fullName_et.getText().toString() == null)
                {
                    Toast.makeText(getActivity(), "Enter Full Name", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(userName_et.getText().toString() == null)
                {
                    Toast.makeText(getActivity(), "Enter User Name", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(email_et.getText().toString() == null)
                {
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return false;
                }else if (password_et.getText().toString() == null)
                {
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(rePassword_et.getText().toString() == null)
                {
                    Toast.makeText(getActivity(), "Enter your password again", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(!rePassword_et.getText().toString().equals(password_et.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Passwords Dont Match!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }
        });


        addProfilePhoto_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_IMAGE_REQUEST);

            }
        });

        return view;
    }

    private void getReferences(View view)
    {
        fullName_et = view.findViewById(R.id.signup_fullName_et);
        userName_et = view.findViewById(R.id.signUp_username_et);
        email_et = view.findViewById(R.id.signup_email_et);
        password_et = view.findViewById(R.id.signup_password_et);
        rePassword_et = view.findViewById(R.id.signup_rePassword_et);

        email_btn = view.findViewById(R.id.signup_email_btn);
        facebook_btn = view.findViewById(R.id.signup_facebook_btn);
        google_btn = view.findViewById(R.id.signup_google_btn);
        addProfilePhoto_btn = view.findViewById(R.id.signup_addPhoto_btn);

        profilePhotoURI_tv = view.findViewById(R.id.signup_profilePhoto_tv);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        signInClient = GoogleSignIn.getClient(getActivity(),signInOptions);

        firestoreDatabase = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference().child("usersPhotos");

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

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK)
        {
            photoUri = data.getData();
            profilePhotoURI_tv.setText(photoUri.getLastPathSegment());
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
                    if(documentSnapshots.isEmpty())
                    {
                        user.set_ID(account.getId());
                        user.setEmail(account.getEmail());
                        user.setFullName(account.getDisplayName());
                        user.setUserName(account.getEmail());
                        user.setProfilePhoto(account.getPhotoUrl().toString());

                        allTheUsers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {

                            }
                        });
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
