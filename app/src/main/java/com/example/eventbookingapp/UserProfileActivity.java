package com.example.eventbookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;



public class UserProfileActivity extends AppCompatActivity {



    TextView tvProfileName;
    TextView tvProfileEmail;
    TextView tvUserId;


    Button btnLogoutProfile;


    FirebaseAuth auth;
    FirebaseFirestore db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);





        tvProfileName =
                findViewById(R.id.tvProfileName);


        tvProfileEmail =
                findViewById(R.id.tvProfileEmail);


        tvUserId =
                findViewById(R.id.tvUserId);



        btnLogoutProfile =
                findViewById(R.id.btnLogoutProfile);






        auth =
                FirebaseAuth.getInstance();


        db =
                FirebaseFirestore.getInstance();





        loadProfile();






        btnLogoutProfile.setOnClickListener(v -> {


            auth.signOut();


            Intent intent =
                    new Intent(
                            UserProfileActivity.this,
                            LoginActivity.class
                    );


            startActivity(intent);


            finish();


        });



    }







    private void loadProfile(){



        if(auth.getCurrentUser() == null){

            return;

        }




        String uid =
                auth.getCurrentUser()
                        .getUid();





        tvProfileEmail.setText(
                auth.getCurrentUser()
                        .getEmail()
        );



        tvUserId.setText(
                "User ID : " + uid
        );







        db.collection("users")

                .document(uid)

                .get()

                .addOnSuccessListener(documentSnapshot -> {



                    if(documentSnapshot.exists()){


                        String name =
                                documentSnapshot.getString("name");



                        if(name != null){


                            tvProfileName.setText(
                                    name
                            );


                        }


                    }



                });



    }


}