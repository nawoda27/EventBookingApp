package com.example.eventbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;

    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        // -----------------------------
        // Initialize Views
        // -----------------------------

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);


        // -----------------------------
        // Initialize Firebase
        // -----------------------------

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // -----------------------------
        // Register Button
        // -----------------------------

        btnRegister.setOnClickListener(v -> registerUser());


        // -----------------------------
        // Login Button
        // -----------------------------

        tvLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();

        });

    }


    // =====================================================
    // REGISTER USER
    // =====================================================

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        // -----------------------------
        // Validate Name
        // -----------------------------

        if (name.isEmpty()) {

            etName.setError("Enter your full name");
            etName.requestFocus();

            return;
        }


        // -----------------------------
        // Validate Email
        // -----------------------------

        if (email.isEmpty()) {

            etEmail.setError("Enter your email");
            etEmail.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();

            return;
        }


        // -----------------------------
        // Validate Password
        // -----------------------------

        if (password.isEmpty()) {

            etPassword.setError("Enter your password");
            etPassword.requestFocus();

            return;
        }


        if (password.length() < 6) {

            etPassword.setError(
                    "Password must contain at least 6 characters"
            );

            etPassword.requestFocus();

            return;
        }


        // -----------------------------
        // Disable Register Button
        // -----------------------------

        btnRegister.setEnabled(false);
        btnRegister.setText("Creating Account...");


        // =====================================================
        // CREATE FIREBASE AUTH ACCOUNT
        // =====================================================

        auth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(task -> {

                    // -----------------------------
                    // Authentication Failed
                    // -----------------------------

                    if (!task.isSuccessful()) {

                        btnRegister.setEnabled(true);
                        btnRegister.setText("Create Account");

                        handleRegistrationError(task.getException());

                        return;
                    }


                    // -----------------------------
                    // Get Current User
                    // -----------------------------

                    if (auth.getCurrentUser() == null) {

                        btnRegister.setEnabled(true);
                        btnRegister.setText("Create Account");

                        Toast.makeText(
                                RegisterActivity.this,
                                "Registration failed",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }


                    String uid = auth.getCurrentUser().getUid();


                    // =====================================================
                    // CREATE USER DATA FOR FIRESTORE
                    // =====================================================

                    HashMap<String, Object> user = new HashMap<>();

                    user.put("name", name);
                    user.put("email", email);
                    user.put("role", "user");
                    user.put("status", "Active");


                    // =====================================================
                    // SAVE USER TO FIRESTORE
                    // =====================================================

                    db.collection("users")
                            .document(uid)
                            .set(user)

                            .addOnSuccessListener(unused -> {

                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Registration Successful!",
                                        Toast.LENGTH_SHORT
                                ).show();


                                // -----------------------------
                                // Go to Login
                                // -----------------------------

                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        LoginActivity.class
                                );

                                startActivity(intent);
                                finish();

                            })

                            .addOnFailureListener(e -> {

                                btnRegister.setEnabled(true);
                                btnRegister.setText("Create Account");


                                // =====================================================
                                // FIRESTORE FAILED
                                // Delete newly created Auth account
                                // =====================================================

                                if (auth.getCurrentUser() != null) {

                                    auth.getCurrentUser()
                                            .delete()
                                            .addOnCompleteListener(deleteTask -> {

                                                Toast.makeText(
                                                        RegisterActivity.this,
                                                        "User data could not be saved.\n"
                                                                + "Please check Firestore Rules.",
                                                        Toast.LENGTH_LONG
                                                ).show();

                                            });

                                } else {

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Firestore Error: "
                                                    + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            });

                });

    }


    // =====================================================
    // HANDLE FIREBASE AUTH ERRORS
    // =====================================================

    private void handleRegistrationError(Exception exception) {

        if (exception instanceof FirebaseAuthUserCollisionException) {

            Toast.makeText(
                    this,
                    "This email is already registered.\nPlease login.",
                    Toast.LENGTH_LONG
            ).show();

        }

        else if (exception instanceof FirebaseAuthWeakPasswordException) {

            Toast.makeText(
                    this,
                    "Password is too weak.\nUse at least 6 characters.",
                    Toast.LENGTH_LONG
            ).show();

        }

        else if (exception instanceof FirebaseAuthInvalidCredentialsException) {

            Toast.makeText(
                    this,
                    "Invalid email address.",
                    Toast.LENGTH_LONG
            ).show();

        }

        else {

            String message;

            if (exception != null) {

                message = exception.getMessage();

            } else {

                message = "Registration failed.";

            }


            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
            ).show();
        }

    }

}