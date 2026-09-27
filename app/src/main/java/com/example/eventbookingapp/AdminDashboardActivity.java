package com.example.eventbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    CardView cardManageEvents;
    CardView cardManageBookings;
    CardView cardManageUsers;

    Button btnLogoutAdmin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dashboard);

        // Find Views
        cardManageEvents = findViewById(R.id.cardManageEvents);
        cardManageBookings = findViewById(R.id.cardManageBookings);
        cardManageUsers = findViewById(R.id.cardManageUsers);

        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);

        // Firebase Authentication
        auth = FirebaseAuth.getInstance();


        // Manage Events
        cardManageEvents.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageEventsActivity.class
            );

            startActivity(intent);
        });


        // Manage Bookings
        cardManageBookings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageBookingsActivity.class
            );

            startActivity(intent);
        });


        // Manage Users
        cardManageUsers.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageUsersActivity.class
            );

            startActivity(intent);
        });


        // Logout
        btnLogoutAdmin.setOnClickListener(v -> {

            auth.signOut();

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
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
