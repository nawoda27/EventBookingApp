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

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerMyBookings;

    private ArrayList<Booking> bookingList;
    private MyBookingAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_bookings);

        recyclerMyBookings = findViewById(R.id.recyclerMyBookings);

        recyclerMyBookings.setLayoutManager(
                new LinearLayoutManager(this)
        );

        bookingList = new ArrayList<>();

        adapter = new MyBookingAdapter(bookingList);

        recyclerMyBookings.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadMyBookings();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMyBookings();
    }

    private void loadMyBookings() {

        if (auth.getCurrentUser() == null) {

            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String userId =
                auth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    bookingList.clear();

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        Booking booking =
                                document.toObject(Booking.class);

                        booking.setId(document.getId());

                        bookingList.add(booking);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            MyBookingsActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}