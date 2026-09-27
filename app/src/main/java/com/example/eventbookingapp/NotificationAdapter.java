package com.example.eventbookingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter
        extends RecyclerView.Adapter<
        NotificationAdapter.NotificationViewHolder> {

    private ArrayList<Notification> notificationList;

    public NotificationAdapter(
            ArrayList<Notification> notificationList) {

        this.notificationList =
                notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_notification,
                        parent,
                        false
                );

        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NotificationViewHolder holder,
            int position) {

        Notification notification =
                notificationList.get(position);

        String message =
                notification.getMessage();

        String date =
                notification.getDate();

        holder.txtMessage.setText(
                message != null
                        ? message
                        : "New notification"
        );

        holder.txtDate.setText(
                date != null
                        ? date
                        : "Date not available"
        );
    }

    @Override
    public int getItemCount() {

        return notificationList.size();
    }

    public static class NotificationViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;
        TextView txtDate;

        public NotificationViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtMessage =
                    itemView.findViewById(
                            R.id.txtMessage
                    );

            txtDate =
                    itemView.findViewById(
                            R.id.txtDate
                    );
        }
    }
}