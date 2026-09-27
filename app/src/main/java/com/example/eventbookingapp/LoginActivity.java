package com.example.eventbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;
    private TextView tvRegister;

    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        // Initialize Views

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);


        // Firebase

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Login Button

        btnLogin.setOnClickListener(v -> loginUser());


        // Register Button

        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);

        });

    }


    // =====================================================
    // LOGIN USER
    // =====================================================

    private void loginUser() {

        String email = etEmail.getText()
                .toString()
                .trim();

        String password = etPassword.getText()
                .toString()
                .trim();


        // Empty Email

        if (email.isEmpty()) {

            etEmail.setError("Enter your email");
            etEmail.requestFocus();

            return;
        }


        // Empty Password

        if (password.isEmpty()) {

            etPassword.setError("Enter your password");
            etPassword.requestFocus();

            return;
        }


        // Disable Button

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");


        // =====================================================
        // FIREBASE AUTHENTICATION
        // =====================================================

        auth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");

                        Exception e = task.getException();

                        String message =
                                e != null
                                        ? e.getMessage()
                                        : "Login failed";


                        Toast.makeText(
                                LoginActivity.this,
                                "Login Failed:\n" + message,
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }


                    // =====================================================
                    // GET LOGGED-IN USER
                    // =====================================================

                    FirebaseUser firebaseUser =
                            auth.getCurrentUser();


                    if (firebaseUser == null) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");

                        Toast.makeText(
                                LoginActivity.this,
                                "User account not found.",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }


                    String uid = firebaseUser.getUid();


                    // =====================================================
                    // CHECK USER DOCUMENT
                    // =====================================================

                    db.collection("users")
                            .document(uid)
                            .get()

                            .addOnSuccessListener(documentSnapshot -> {

                                if (!documentSnapshot.exists()) {

                                    btnLogin.setEnabled(true);
                                    btnLogin.setText("Login");

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "User profile not found in database.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                String role =
                                        documentSnapshot.getString("role");


                                // =====================================================
                                // ADMIN LOGIN
                                // =====================================================

                                if ("admin".equalsIgnoreCase(role)) {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Admin Login Successful",
                                            Toast.LENGTH_SHORT
                                    ).show();


                                    Intent intent =
                                            new Intent(
                                                    LoginActivity.this,
                                                    AdminDashboardActivity.class
                                            );

                                    startActivity(intent);
                                    finish();

                                }


                                // =====================================================
                                // NORMAL USER LOGIN
                                // =====================================================

                                else {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                    ).show();


                                    Intent intent =
                                            new Intent(
                                                    LoginActivity.this,
                                                    HomeActivity.class
                                            );

                                    startActivity(intent);
                                    finish();

                                }

                            })

                            .addOnFailureListener(e -> {

                                btnLogin.setEnabled(true);
                                btnLogin.setText("Login");

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Database Error:\n"
                                                + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();

                            });

                });

    }

}
