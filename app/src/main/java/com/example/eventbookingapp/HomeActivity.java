package com.example.eventbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;

    Button btnEvents;
    Button btnBookings;
    Button btnNotifications;
    Button btnProfile;
    Button btnCancel;
    Button btnLogout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home2);

        // Find Views
        tvWelcome = findViewById(R.id.tvWelcome);

        btnEvents = findViewById(R.id.btnEvents);
        btnBookings = findViewById(R.id.btnBookings);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnProfile = findViewById(R.id.btnProfile);
        btnCancel = findViewById(R.id.btnCancel);
        btnLogout = findViewById(R.id.btnLogout);

        // Firebase
        auth = FirebaseAuth.getInstance();

        // Welcome message
        tvWelcome.setText("Welcome User");

        // Explore Events
        btnEvents.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    EventsActivity.class
            );

            startActivity(intent);
        });

        // My Bookings
        btnBookings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    MyBookingsActivity.class
            );

            startActivity(intent);
        });

        // Notifications
        btnNotifications.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    NotificationsActivity.class
            );

            startActivity(intent);
        });

        // My Profile
        btnProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ProfileActivity.class
            );

            startActivity(intent);
        });

        // Cancel Booking
        btnCancel.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    MyBookingsActivity.class
            );

            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {

            auth.signOut();

            Intent intent = new Intent(
                    HomeActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }
}