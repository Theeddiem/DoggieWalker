package com.game.eddieandmichael.doggiewalker;

import android.util.Log;

import com.game.eddieandmichael.classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    public void TestingFirestorm()
    {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference collection = firestore.collection("users");

        collection.whereEqualTo("fullName","Michael Katan").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots)
                    {
                        if(documentSnapshots.isEmpty())
                        {
                            Log.d("Test", "onSuccess: no user found");
                        }else
                        {
                            List<User> users = documentSnapshots.toObjects(User.class);

                            assertEquals(users.size(),1);
                        }

                    }
                });

    }
}