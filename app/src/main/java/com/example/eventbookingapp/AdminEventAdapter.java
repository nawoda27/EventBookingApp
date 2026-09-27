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

import java.util.ArrayList;

public class AdminEventAdapter
        extends RecyclerView.Adapter<AdminEventAdapter.ViewHolder> {

    private ArrayList<Event> eventList;
    private FirebaseFirestore db;

    public AdminEventAdapter(ArrayList<Event> eventList) {

        this.eventList = eventList;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_admin_event,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Event event = eventList.get(position);

        // Event Name
        String eventName = event.getEventName();

        if (eventName == null ||
                eventName.trim().isEmpty()) {

            eventName = "Untitled Event";
        }

        holder.txtEventName.setText(eventName);


        // Date
        holder.txtDate.setText(
                "Date • " +
                        safeText(event.getDate())
        );


        // Location
        holder.txtLocation.setText(
                "Location • " +
                        safeText(event.getLocation())
        );


        // Description
        String description = event.getDescription();

        if (description != null &&
                !description.trim().isEmpty()) {

            holder.txtDescription.setText(description);

            holder.txtDescription.setVisibility(
                    View.VISIBLE
            );

        } else {

            holder.txtDescription.setVisibility(
                    View.GONE
            );
        }


        // Delete Button
        holder.btnDeleteEvent.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {

                return;
            }

            Event selectedEvent =
                    eventList.get(currentPosition);

            deleteEvent(
                    selectedEvent,
                    currentPosition,
                    holder
            );
        });
    }


    // Delete Event
    private void deleteEvent(
            Event event,
            int position,
            ViewHolder holder) {

        if (event.getId() == null ||
                event.getId().trim().isEmpty()) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Event ID not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        holder.btnDeleteEvent.setEnabled(false);

        holder.btnDeleteEvent.setText(
                "Deleting..."
        );


        db.collection("events")
                .document(event.getId())
                .delete()
                .addOnSuccessListener(unused -> {

                    if (position >= 0 &&
                            position < eventList.size()) {

                        eventList.remove(position);

                        notifyItemRemoved(position);
                    }


                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Event deleted successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> {

                    holder.btnDeleteEvent.setEnabled(true);

                    holder.btnDeleteEvent.setText(
                            "Delete Event"
                    );


                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Failed to delete event",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // Safe text helper
    private String safeText(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not specified";
        }

        return value;
    }


    @Override
    public int getItemCount() {

        return eventList.size();
    }


    // ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtEventName;
        TextView txtDate;
        TextView txtLocation;
        TextView txtDescription;

        Button btnDeleteEvent;


        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);


            txtEventName =
                    itemView.findViewById(
                            R.id.txtEventName
                    );


            txtDate =
                    itemView.findViewById(
                            R.id.txtDate
                    );


            txtLocation =
                    itemView.findViewById(
                            R.id.txtLocation
                    );


            txtDescription =
                    itemView.findViewById(
                            R.id.txtDescription
                    );


            btnDeleteEvent =
                    itemView.findViewById(
                            R.id.btnDeleteEvent
                    );
        }
    }
}