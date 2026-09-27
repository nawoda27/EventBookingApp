package com.example.eventbookingapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;



public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {


    ArrayList<Booking> bookingList;



    public BookingAdapter(ArrayList<Booking> bookingList) {

        this.bookingList = bookingList;

    }





    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);


        return new BookingViewHolder(view);

    }






    @Override
    public void onBindViewHolder(
            @NonNull BookingViewHolder holder,
            int position) {


        Booking booking = bookingList.get(position);



        holder.txtUserName.setText(
                "User : " + booking.getUserName()
        );


        holder.txtEventName.setText(
                "Event : " + booking.getEventName()
        );


        holder.txtDate.setText(
                "Date : " + booking.getBookingDate()
        );


        holder.txtStatus.setText(
                "Status : " + booking.getStatus()
        );



    }







    @Override
    public int getItemCount() {

        return bookingList.size();

    }








    public static class BookingViewHolder extends RecyclerView.ViewHolder {


        TextView txtUserName;
        TextView txtEventName;
        TextView txtDate;
        TextView txtStatus;



        public BookingViewHolder(@NonNull View itemView) {

            super(itemView);



            txtUserName =
                    itemView.findViewById(R.id.txtUserName);


            txtEventName =
                    itemView.findViewById(R.id.txtEventName);


            txtDate =
                    itemView.findViewById(R.id.txtDate);


            txtStatus =
                    itemView.findViewById(R.id.txtStatus);


        }

    }

}