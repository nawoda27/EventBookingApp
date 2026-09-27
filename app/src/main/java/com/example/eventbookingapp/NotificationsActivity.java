package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;

    private ArrayList<Notification> notificationList;
    private NotificationAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);

        recyclerNotifications =
                findViewById(R.id.recyclerNotifications);

        recyclerNotifications.setLayoutManager(
                new LinearLayoutManager(this)
        );

        notificationList =
                new ArrayList<>();

        adapter =
                new NotificationAdapter(
                        notificationList
                );

        recyclerNotifications.setAdapter(
                adapter
        );

        db =
                FirebaseFirestore.getInstance();

        auth =
                FirebaseAuth.getInstance();

        loadNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && auth != null) {
            loadNotifications();
        }
    }

    private void loadNotifications() {

        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId =
                auth.getCurrentUser().getUid();

        db.collection("notifications")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {

                            notificationList.clear();

                            for (
                                    QueryDocumentSnapshot document :
                                    queryDocumentSnapshots
                            ) {

                                Notification notification =
                                        document.toObject(
                                                Notification.class
                                        );

                                notificationList.add(
                                        notification
                                );
                            }

                            adapter.notifyDataSetChanged();
                        }
                )
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            NotificationsActivity.this,
                            "Failed to load notifications",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}