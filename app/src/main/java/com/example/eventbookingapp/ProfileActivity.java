package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvProfileRole;
    private TextView tvProfileStatus;

    private Button btnBack;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfileRole = findViewById(R.id.tvProfileRole);
        tvProfileStatus = findViewById(R.id.tvProfileStatus);

        btnBack = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadProfile();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProfile() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String name =
                                documentSnapshot.getString("name");

                        String email =
                                documentSnapshot.getString("email");

                        String role =
                                documentSnapshot.getString("role");

                        String status =
                                documentSnapshot.getString("status");

                        tvProfileName.setText(
                                name != null ? name : "User"
                        );

                        tvProfileEmail.setText(
                                email != null ? email : "No email"
                        );

                        tvProfileRole.setText(
                                "Role: " +
                                        (role != null ? role : "user")
                        );

                        tvProfileStatus.setText(
                                "Status: " +
                                        (status != null ? status : "Active")
                        );

                    } else {

                        Toast.makeText(
                                this,
                                "Profile not found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Failed to load profile",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}