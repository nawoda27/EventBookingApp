package com.example.eventbookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;



public class ManageEventsActivity extends AppCompatActivity {



    RecyclerView recyclerEventsAdmin;

    Button btnCreateEvent;


    ArrayList<Event> eventList;


    AdminEventAdapter adapter;


    FirebaseFirestore db;







    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_manage_events);





        recyclerEventsAdmin =
                findViewById(
                        R.id.recyclerEventsAdmin
                );



        btnCreateEvent =
                findViewById(
                        R.id.btnCreateEvent
                );





        recyclerEventsAdmin.setLayoutManager(
                new LinearLayoutManager(this)
        );





        eventList =
                new ArrayList<>();





        adapter =
                new AdminEventAdapter(
                        eventList
                );





        recyclerEventsAdmin.setAdapter(adapter);





        db =
                FirebaseFirestore.getInstance();





        btnCreateEvent.setOnClickListener(v -> {


            startActivity(
                    new Intent(
                            this,
                            CreateEventActivity.class
                    )
            );


        });




        loadEvents();



    }







    @Override
    protected void onResume() {


        super.onResume();


        loadEvents();


    }








    private void loadEvents(){



        db.collection("events")

                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {



                    eventList.clear();





                    for(QueryDocumentSnapshot document :
                            queryDocumentSnapshots){



                        Event event =
                                document.toObject(
                                        Event.class
                                );



                        event.setId(
                                document.getId()
                        );



                        eventList.add(event);



                    }





                    adapter.notifyDataSetChanged();



                })


                .addOnFailureListener(e -> {


                    Toast.makeText(
                            this,
                            "Failed to load events",
                            Toast.LENGTH_SHORT
                    ).show();



                });



    }



}