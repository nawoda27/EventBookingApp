package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookingsAdmin;

    private ArrayList<Booking> bookingList;

    private ManageBookingAdapter adapter;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_bookings);


        // Initialize RecyclerView
        recyclerBookingsAdmin =
                findViewById(R.id.recyclerBookingsAdmin);

        recyclerBookingsAdmin.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // Initialize booking list
        bookingList = new ArrayList<>();


        // Initialize adapter
        adapter = new ManageBookingAdapter(
                bookingList
        );

        recyclerBookingsAdmin.setAdapter(adapter);


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        // Load bookings
        loadBookings();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (db != null) {
            loadBookings();
        }
    }


    private void loadBookings() {

        db.collection("bookings")
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {

                            bookingList.clear();


                            for (QueryDocumentSnapshot document :
                                    queryDocumentSnapshots) {

                                Booking booking =
                                        document.toObject(
                                                Booking.class
                                        );


                                booking.setId(
                                        document.getId()
                                );


                                bookingList.add(
                                        booking
                                );
                            }


                            adapter.notifyDataSetChanged();
                        }
                )
                .addOnFailureListener(
                        e -> Toast.makeText(
                                ManageBookingsActivity.this,
                                "Failed to load bookings",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}