package com.example.eventbookingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ManageBookingAdapter
        extends RecyclerView.Adapter<ManageBookingAdapter.ViewHolder> {

    private ArrayList<Booking> bookingList;
    private FirebaseFirestore db;

    public ManageBookingAdapter(ArrayList<Booking> bookingList) {
        this.bookingList = bookingList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_manage_booking,
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

        holder.txtUserName.setText(
                "User : " + safeText(booking.getUserName())
        );

        holder.txtUserId.setText(
                "User ID : " + safeText(booking.getUserId())
        );

        holder.txtEventName.setText(
                "Event : " + safeText(booking.getEventName())
        );

        holder.txtBookingDate.setText(
                "Booking Date : " + safeText(booking.getBookingDate())
        );

        holder.txtStatus.setText(
                "Status : " + safeText(booking.getStatus())
        );


        // ================= APPROVE =================

        holder.btnApprove.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            updateBookingStatus(
                    booking,
                    "Confirmed",
                    holder
            );
        });


        // ================= REJECT =================

        holder.btnReject.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            updateBookingStatus(
                    booking,
                    "Rejected",
                    holder
            );
        });
    }


    // ================= UPDATE STATUS =================

    private void updateBookingStatus(
            Booking booking,
            String newStatus,
            ViewHolder holder) {

        if (booking.getId() == null ||
                booking.getId().trim().isEmpty()) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Booking ID not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        holder.btnApprove.setEnabled(false);
        holder.btnReject.setEnabled(false);

        db.collection("bookings")
                .document(booking.getId())
                .update("status", newStatus)
                .addOnSuccessListener(unused -> {

                    booking.setStatus(newStatus);

                    notifyItemChanged(
                            holder.getBindingAdapterPosition()
                    );

                    // Create notification for the user
                    createNotification(
                            booking,
                            newStatus,
                            holder
                    );
                })
                .addOnFailureListener(e -> {

                    holder.btnApprove.setEnabled(true);
                    holder.btnReject.setEnabled(true);

                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Failed to update booking",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // ================= CREATE NOTIFICATION =================

    private void createNotification(
            Booking booking,
            String status,
            ViewHolder holder) {

        String userId = booking.getUserId();

        if (userId == null ||
                userId.trim().isEmpty()) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Booking updated, but User ID is missing",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        String eventName = booking.getEventName();

        if (eventName == null ||
                eventName.trim().isEmpty()) {

            eventName = "your event";
        }


        String message;

        if ("Confirmed".equals(status)) {

            message =
                    "Your booking for " +
                            eventName +
                            " has been approved.";

        } else {

            message =
                    "Your booking for " +
                            eventName +
                            " has been rejected.";
        }


        String currentDate =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                ).format(new Date());


        Map<String, Object> notification =
                new HashMap<>();

        notification.put(
                "userId",
                userId
        );

        notification.put(
                "message",
                message
        );

        notification.put(
                "date",
                currentDate
        );

        notification.put(
                "read",
                false
        );


        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Booking " +
                                    status +
                                    "\nNotification sent to user",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Booking updated, but notification failed",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }


    private String safeText(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtUserName;
        TextView txtUserId;
        TextView txtEventName;
        TextView txtBookingDate;
        TextView txtStatus;

        Button btnApprove;
        Button btnReject;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtUserName =
                    itemView.findViewById(
                            R.id.txtUserName
                    );

            txtUserId =
                    itemView.findViewById(
                            R.id.txtUserId
                    );

            txtEventName =
                    itemView.findViewById(
                            R.id.txtEventName
                    );

            txtBookingDate =
                    itemView.findViewById(
                            R.id.txtBookingDate
                    );

            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );

            btnApprove =
                    itemView.findViewById(
                            R.id.btnApprove
                    );

            btnReject =
                    itemView.findViewById(
                            R.id.btnReject
                    );
        }
    }
}