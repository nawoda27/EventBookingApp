package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private TextView txtEventName;
    private TextView txtEventDate;
    private TextView txtEventLocation;

    private Button btnConfirmBooking;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking);


        // -----------------------------
        // INITIALIZE VIEWS
        // -----------------------------

        txtEventName = findViewById(R.id.txtEventName);
        txtEventDate = findViewById(R.id.txtEventDate);
        txtEventLocation = findViewById(R.id.txtEventLocation);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);


        // -----------------------------
        // FIREBASE
        // -----------------------------

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // -----------------------------
        // GET EVENT DATA
        // -----------------------------

        eventId = getIntent().getStringExtra("eventId");
        eventName = getIntent().getStringExtra("eventName");
        eventDate = getIntent().getStringExtra("eventDate");
        eventLocation = getIntent().getStringExtra("eventLocation");


        // -----------------------------
        // DISPLAY EVENT INFORMATION
        // -----------------------------

        if (eventName == null || eventName.trim().isEmpty()) {
            txtEventName.setText("Event");
        } else {
            txtEventName.setText(eventName);
        }


        if (eventDate == null || eventDate.trim().isEmpty()) {
            txtEventDate.setText("Date: Not specified");
        } else {
            txtEventDate.setText("Date: " + eventDate);
        }


        if (eventLocation == null || eventLocation.trim().isEmpty()) {
            txtEventLocation.setText("Location: Not specified");
        } else {
            txtEventLocation.setText("Location: " + eventLocation);
        }


        // -----------------------------
        // CONFIRM BOOKING
        // -----------------------------

        btnConfirmBooking.setOnClickListener(
                v -> checkExistingBooking()
        );
    }


    // =====================================================
    // CHECK EXISTING BOOKING
    // =====================================================

    private void checkExistingBooking() {

        if (auth.getCurrentUser() == null) {

            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (eventId == null ||
                eventId.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Event information is missing",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String userId =
                auth.getCurrentUser().getUid();


        btnConfirmBooking.setEnabled(false);
        btnConfirmBooking.setText("Checking...");


        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(
                        querySnapshot -> {

                            if (querySnapshot.isEmpty()) {

                                createBooking();

                            } else {

                                handleExistingBooking(
                                        querySnapshot
                                );
                            }
                        }
                )
                .addOnFailureListener(e -> {

                    btnConfirmBooking.setEnabled(true);
                    btnConfirmBooking.setText(
                            "Confirm Booking"
                    );

                    Toast.makeText(
                            BookingActivity.this,
                            "Unable to check booking",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }


    // =====================================================
    // HANDLE EXISTING BOOKING
    // =====================================================

    private void handleExistingBooking(
            QuerySnapshot querySnapshot) {

        String status = "Pending";


        if (!querySnapshot.isEmpty()) {

            Booking existingBooking =
                    querySnapshot.getDocuments()
                            .get(0)
                            .toObject(Booking.class);


            if (existingBooking != null &&
                    existingBooking.getStatus() != null) {

                status =
                        existingBooking.getStatus();
            }
        }


        btnConfirmBooking.setEnabled(true);
        btnConfirmBooking.setText(
                "Confirm Booking"
        );


        if ("Rejected".equalsIgnoreCase(status)) {

            Toast.makeText(
                    this,
                    "You have already submitted a booking for this event.",
                    Toast.LENGTH_LONG
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "You have already booked this event.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // CREATE BOOKING
    // =====================================================

    private void createBooking() {

        if (auth.getCurrentUser() == null) {

            btnConfirmBooking.setEnabled(true);
            btnConfirmBooking.setText(
                    "Confirm Booking"
            );

            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String userId =
                auth.getCurrentUser().getUid();


        String userEmail =
                auth.getCurrentUser().getEmail();


        String bookingDate =
                new SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                ).format(new Date());


        btnConfirmBooking.setEnabled(false);
        btnConfirmBooking.setText(
                "Booking..."
        );


        Booking booking =
                new Booking(
                        userId,
                        userEmail,
                        eventId,
                        eventName,
                        bookingDate,
                        "Pending"
                );


        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(
                        documentReference -> {

                            Toast.makeText(
                                    BookingActivity.this,
                                    "Booking submitted successfully!",
                                    Toast.LENGTH_LONG
                            ).show();


                            btnConfirmBooking.setEnabled(true);

                            btnConfirmBooking.setText(
                                    "Confirm Booking"
                            );


                            finish();
                        }
                )
                .addOnFailureListener(e -> {

                    btnConfirmBooking.setEnabled(true);

                    btnConfirmBooking.setText(
                            "Confirm Booking"
                    );


                    Toast.makeText(
                            BookingActivity.this,
                            "Booking failed",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}