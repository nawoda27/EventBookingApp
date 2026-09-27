package com.example.eventbookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter
        extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> eventList;
    private final Context context;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public EventAdapter(
            Context context,
            List<Event> eventList) {

        this.context = context;
        this.eventList = eventList;

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_event,
                        parent,
                        false
                );

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EventViewHolder holder,
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
        String date = event.getDate();

        if (date == null ||
                date.trim().isEmpty()) {

            date = "Not specified";
        }

        holder.txtDate.setText(
                "Date • " + date
        );


        // Location
        String location = event.getLocation();

        if (location == null ||
                location.trim().isEmpty()) {

            location = "Not specified";
        }

        holder.txtLocation.setText(
                "Location • " + location
        );


        // Description
        String description = event.getDescription();

        if (description == null ||
                description.trim().isEmpty()) {

            holder.txtDescription.setVisibility(
                    View.GONE
            );

        } else {

            holder.txtDescription.setVisibility(
                    View.VISIBLE
            );

            holder.txtDescription.setText(
                    description
            );
        }


        // Book Button
        holder.btnBook.setOnClickListener(v -> {

            Toast.makeText(
                    context,
                    "Opening booking...",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }


    @Override
    public int getItemCount() {

        return eventList.size();
    }


    public static class EventViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtEventName;
        TextView txtDate;
        TextView txtLocation;
        TextView txtDescription;

        Button btnBook;


        public EventViewHolder(
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

            btnBook =
                    itemView.findViewById(
                            R.id.btnBook
                    );
        }
    }
}