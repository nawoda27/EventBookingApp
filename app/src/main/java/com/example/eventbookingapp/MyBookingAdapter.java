package com.example.eventbookingapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyBookingAdapter
        extends RecyclerView.Adapter<MyBookingAdapter.ViewHolder> {

    private final ArrayList<Booking> bookingList;
    private final FirebaseFirestore db;

    public MyBookingAdapter(ArrayList<Booking> bookingList) {
        this.bookingList = bookingList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_my_booking,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Booking booking = bookingList.get(position);

        // ---------------------------------------------
        // EVENT NAME
        // ---------------------------------------------

        String eventName = booking.getEventName();

        if (eventName == null ||
                eventName.trim().isEmpty()) {

            eventName = "Event";
        }

        holder.txtEventName.setText(eventName);


        // ---------------------------------------------
        // BOOKING DATE
        // ---------------------------------------------

        String bookingDate = booking.getBookingDate();

        if (bookingDate == null ||
                bookingDate.trim().isEmpty()) {

            bookingDate = "Not available";
        }

        holder.txtBookingDate.setText(
                "Booking Date: " + bookingDate
        );


        // ---------------------------------------------
        // BOOKING ID
        // ---------------------------------------------

        String bookingId = booking.getId();

        if (bookingId != null &&
                !bookingId.trim().isEmpty()) {

            String shortId = bookingId;

            if (bookingId.length() > 8) {
                shortId = bookingId.substring(0, 8);
            }

            holder.txtBookingId.setText(
                    "Booking ID: " + shortId
            );

        } else {

            holder.txtBookingId.setText(
                    "Booking ID: Not available"
            );
        }


        // ---------------------------------------------
        // STATUS
        // ---------------------------------------------

        String status = booking.getStatus();

        if (status == null ||
                status.trim().isEmpty()) {

            status = "Pending";
        }

        holder.txtStatus.setText(status);


        // ---------------------------------------------
        // STATUS DESIGN
        // ---------------------------------------------

        switch (status.toLowerCase()) {

            case "confirmed":

                holder.txtStatus.setTextColor(
                        Color.parseColor("#166534")
                );

                holder.txtStatus.setBackgroundColor(
                        Color.parseColor("#DCFCE7")
                );

                break;


            case "rejected":

                holder.txtStatus.setTextColor(
                        Color.parseColor("#991B1B")
                );

                holder.txtStatus.setBackgroundColor(
                        Color.parseColor("#FEE2E2")
                );

                break;


            case "cancelled":

                holder.txtStatus.setTextColor(
                        Color.parseColor("#475569")
                );

                holder.txtStatus.setBackgroundColor(
                        Color.parseColor("#E2E8F0")
                );

                break;


            default:

                holder.txtStatus.setTextColor(
                        Color.parseColor("#92400E")
                );

                holder.txtStatus.setBackgroundColor(
                        Color.parseColor("#FEF3C7")
                );

                break;
        }


        // ---------------------------------------------
        // CANCEL BUTTON
        // ---------------------------------------------

        if ("pending".equalsIgnoreCase(status)) {

            holder.btnCancelBooking.setVisibility(
                    View.VISIBLE
            );

            holder.btnCancelBooking.setEnabled(true);

            holder.btnCancelBooking.setText(
                    "Cancel Booking"
            );

        } else {

            holder.btnCancelBooking.setVisibility(
                    View.GONE
            );
        }


        // ---------------------------------------------
        // CANCEL BUTTON CLICK
        // ---------------------------------------------

        holder.btnCancelBooking.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {

                return;
            }

            Booking selectedBooking =
                    bookingList.get(currentPosition);

            showCancelConfirmation(
                    selectedBooking,
                    currentPosition,
                    holder
            );
        });
    }


    // =====================================================
    // CANCEL CONFIRMATION
    // =====================================================

    private void showCancelConfirmation(
            Booking booking,
            int position,
            ViewHolder holder) {

        new AlertDialog.Builder(
                holder.itemView.getContext()
        )
                .setTitle("Cancel Booking")
                .setMessage(
                        "Are you sure you want to cancel this booking?"
                )
                .setNegativeButton(
                        "No",
                        null
                )
                .setPositiveButton(
                        "Yes, Cancel",
                        (dialog, which) -> {

                            cancelBooking(
                                    booking,
                                    position,
                                    holder
                            );
                        }
                )
                .show();
    }


    // =====================================================
    // CANCEL BOOKING
    // =====================================================

    private void cancelBooking(
            Booking booking,
            int position,
            ViewHolder holder) {

        String bookingId = booking.getId();


        // ---------------------------------------------
        // CHECK BOOKING ID
        // ---------------------------------------------

        if (bookingId == null ||
                bookingId.trim().isEmpty()) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Booking ID not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ---------------------------------------------
        // DISABLE BUTTON
        // ---------------------------------------------

        holder.btnCancelBooking.setEnabled(false);

        holder.btnCancelBooking.setText(
                "Cancelling..."
        );


        // ---------------------------------------------
        // UPDATE FIRESTORE
        // ---------------------------------------------

        db.collection("bookings")
                .document(bookingId)
                .update("status", "Cancelled")
                .addOnSuccessListener(unused -> {

                    // Update local object
                    booking.setStatus("Cancelled");


                    // Refresh this card
                    notifyItemChanged(position);


                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Booking cancelled successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> {

                    holder.btnCancelBooking.setEnabled(true);

                    holder.btnCancelBooking.setText(
                            "Cancel Booking"
                    );

                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Failed to cancel booking",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // =====================================================
    // ITEM COUNT
    // =====================================================

    @Override
    public int getItemCount() {
        return bookingList.size();
    }


    // =====================================================
    // VIEW HOLDER
    // =====================================================

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtEventName;
        TextView txtStatus;
        TextView txtBookingDate;
        TextView txtBookingId;

        Button btnCancelBooking;


        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);


            txtEventName =
                    itemView.findViewById(
                            R.id.txtEventName
                    );

            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );

            txtBookingDate =
                    itemView.findViewById(
                            R.id.txtBookingDate
                    );

            txtBookingId =
                    itemView.findViewById(
                            R.id.txtBookingId
                    );

            btnCancelBooking =
                    itemView.findViewById(
                            R.id.btnCancelBooking
                    );
        }
    }
}