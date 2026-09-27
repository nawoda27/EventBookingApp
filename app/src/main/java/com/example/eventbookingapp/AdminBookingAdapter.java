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



public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.BookingViewHolder> {


    ArrayList<Booking> bookingList;


    FirebaseFirestore db;



    public AdminBookingAdapter(ArrayList<Booking> bookingList) {

        this.bookingList = bookingList;

        db = FirebaseFirestore.getInstance();

    }





    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {


        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_admin_booking,
                                parent,
                                false
                        );


        return new BookingViewHolder(view);

    }





    @Override
    public void onBindViewHolder(
            @NonNull BookingViewHolder holder,
            int position) {


        Booking booking =
                bookingList.get(position);



        holder.txtEventName.setText(
                "Event : " + booking.getEventName()
        );


        holder.txtUserId.setText(
                "User ID : " + booking.getUserId()
        );


        holder.txtStatus.setText(
                "Status : " + booking.getStatus()
        );



        holder.btnApprove.setOnClickListener(v -> {


            updateStatus(
                    booking,
                    "Confirmed",
                    holder
            );


        });




        holder.btnReject.setOnClickListener(v -> {


            updateStatus(
                    booking,
                    "Rejected",
                    holder
            );


        });



    }






    private void updateStatus(
            Booking booking,
            String status,
            BookingViewHolder holder
    ){


        db.collection("bookings")
                .document(booking.getId())
                .update(
                        "status",
                        status
                )

                .addOnSuccessListener(unused -> {


                    booking.setStatus(status);


                    holder.txtStatus.setText(
                            "Status : " + status
                    );


                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Status Updated",
                            Toast.LENGTH_SHORT
                    ).show();



                });



    }







    @Override
    public int getItemCount() {

        return bookingList.size();

    }






    public static class BookingViewHolder
            extends RecyclerView.ViewHolder {


        TextView txtEventName;
        TextView txtUserId;
        TextView txtStatus;


        Button btnApprove;
        Button btnReject;



        public BookingViewHolder(
                @NonNull View itemView) {


            super(itemView);



            txtEventName =
                    itemView.findViewById(
                            R.id.txtEventName
                    );


            txtUserId =
                    itemView.findViewById(
                            R.id.txtUserId
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