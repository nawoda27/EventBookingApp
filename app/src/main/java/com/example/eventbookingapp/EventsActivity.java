package com.example.eventbookingapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EventsActivity extends AppCompatActivity {

    private LinearLayout eventContainer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

        eventContainer = findViewById(R.id.eventContainer);

        db = FirebaseFirestore.getInstance();

        loadEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && eventContainer != null) {
            loadEvents();
        }
    }

    // ================= LOAD EVENTS =================

    private void loadEvents() {

        db.collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    eventContainer.removeAllViews();

                    if (queryDocumentSnapshots.isEmpty()) {

                        TextView emptyMessage =
                                new TextView(this);

                        emptyMessage.setText(
                                "No events available right now."
                        );

                        emptyMessage.setTextSize(15);
                        emptyMessage.setTextColor(
                                Color.parseColor("#64748B")
                        );

                        emptyMessage.setGravity(
                                Gravity.CENTER
                        );

                        emptyMessage.setPadding(
                                0,
                                50,
                                0,
                                50
                        );

                        eventContainer.addView(
                                emptyMessage
                        );

                        return;
                    }

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        String eventId =
                                document.getId();

                        String eventName =
                                document.getString(
                                        "eventName"
                                );

                        String eventDate =
                                document.getString(
                                        "date"
                                );

                        String location =
                                document.getString(
                                        "location"
                                );

                        String description =
                                document.getString(
                                        "description"
                                );

                        createEventCard(
                                eventId,
                                eventName,
                                eventDate,
                                location,
                                description
                        );
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            EventsActivity.this,
                            "Failed to load events: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }


    // ================= CREATE EVENT CARD =================

    private void createEventCard(
            String eventId,
            String name,
            String date,
            String location,
            String description) {


        // ================= MAIN CARD =================

        CardView card =
                new CardView(this);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                16
        );

        card.setLayoutParams(cardParams);

        card.setRadius(20);

        card.setCardElevation(3);

        card.setCardBackgroundColor(
                Color.WHITE
        );


        // ================= MAIN LAYOUT =================

        LinearLayout mainLayout =
                new LinearLayout(this);

        mainLayout.setOrientation(
                LinearLayout.VERTICAL
        );


        // ================= IMAGE AREA =================

        FrameLayout imageArea =
                new FrameLayout(this);

        imageArea.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        150
                )
        );


        ImageView image =
                new ImageView(this);

        FrameLayout.LayoutParams imageParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        image.setLayoutParams(imageParams);

        image.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        image.setImageResource(
                R.drawable.event_booking_app
        );

        imageArea.addView(image);


        // ================= IMAGE OVERLAY =================

        View overlay =
                new View(this);

        overlay.setBackgroundColor(
                Color.parseColor("#550F172A")
        );

        imageArea.addView(
                overlay,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );


        // ================= FEATURED LABEL =================

        TextView eventLabel =
                new TextView(this);

        eventLabel.setText(
                "FEATURED EVENT"
        );

        eventLabel.setTextColor(
                Color.WHITE
        );

        eventLabel.setTextSize(10);

        eventLabel.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        eventLabel.setGravity(
                Gravity.CENTER
        );


        GradientDrawable labelBg =
                new GradientDrawable();

        labelBg.setColor(
                Color.parseColor("#CC2563EB")
        );

        labelBg.setCornerRadius(30);

        eventLabel.setBackground(
                labelBg
        );


        FrameLayout.LayoutParams labelParams =
                new FrameLayout.LayoutParams(
                        115,
                        32
                );

        labelParams.gravity =
                Gravity.TOP | Gravity.START;

        labelParams.setMargins(
                15,
                15,
                0,
                0
        );

        imageArea.addView(
                eventLabel,
                labelParams
        );


        mainLayout.addView(
                imageArea
        );


        // ================= CONTENT =================

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                17,
                15,
                17,
                17
        );


        // ================= EVENT NAME =================

        TextView tvName =
                new TextView(this);

        tvName.setText(
                name != null &&
                        !name.trim().isEmpty()
                        ? name
                        : "Untitled Event"
        );

        tvName.setTextColor(
                Color.parseColor("#0F172A")
        );

        tvName.setTextSize(20);

        tvName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                tvName
        );


        // ================= DATE =================

        TextView tvDate =
                new TextView(this);

        tvDate.setText(
                "Date • " +
                        (
                                date != null
                                        ? date
                                        : "Not specified"
                        )
        );

        tvDate.setTextColor(
                Color.parseColor("#64748B")
        );

        tvDate.setTextSize(13);


        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        dateParams.setMargins(
                0,
                7,
                0,
                0
        );

        content.addView(
                tvDate,
                dateParams
        );


        // ================= LOCATION =================

        TextView tvLocation =
                new TextView(this);

        tvLocation.setText(
                "Location • " +
                        (
                                location != null
                                        ? location
                                        : "Not specified"
                        )
        );

        tvLocation.setTextColor(
                Color.parseColor("#64748B")
        );

        tvLocation.setTextSize(13);


        LinearLayout.LayoutParams locationParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        locationParams.setMargins(
                0,
                5,
                0,
                0
        );

        content.addView(
                tvLocation,
                locationParams
        );


        // ================= DESCRIPTION =================

        if (description != null &&
                !description.trim().isEmpty()) {

            TextView tvDescription =
                    new TextView(this);

            tvDescription.setText(
                    description
            );

            tvDescription.setTextColor(
                    Color.parseColor("#64748B")
            );

            tvDescription.setTextSize(12);

            tvDescription.setMaxLines(2);


            LinearLayout.LayoutParams descParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            descParams.setMargins(
                    0,
                    9,
                    0,
                    0
            );

            content.addView(
                    tvDescription,
                    descParams
            );
        }


        // ================= BOOK BUTTON =================

        Button btnBook =
                new Button(this);

        btnBook.setText(
                "Book Now"
        );

        btnBook.setTextColor(
                Color.WHITE
        );

        btnBook.setTextSize(13);

        btnBook.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        btnBook.setAllCaps(false);

        btnBook.setBackgroundTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#2563EB")
                )
        );


        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        48
                );

        buttonParams.setMargins(
                0,
                15,
                0,
                0
        );

        content.addView(
                btnBook,
                buttonParams
        );


        // ================= BOOKING ACTION =================

        View.OnClickListener bookingClickListener =
                v -> {

                    Intent intent =
                            new Intent(
                                    EventsActivity.this,
                                    BookingActivity.class
                            );

                    intent.putExtra(
                            "eventId",
                            eventId
                    );

                    intent.putExtra(
                            "eventName",
                            name
                    );

                    intent.putExtra(
                            "eventDate",
                            date
                    );

                    intent.putExtra(
                            "eventLocation",
                            location
                    );

                    startActivity(intent);
                };


        // Book Now button click
        btnBook.setOnClickListener(
                bookingClickListener
        );


        // Whole event card click
        card.setOnClickListener(
                bookingClickListener
        );


        // ================= ADD CONTENT =================

        mainLayout.addView(
                content
        );

        card.addView(
                mainLayout
        );

        eventContainer.addView(
                card
        );
    }
}