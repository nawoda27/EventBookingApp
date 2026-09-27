package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


public class CreateEventActivity extends AppCompatActivity {


    EditText etEventName;
    EditText etDate;
    EditText etLocation;
    EditText etDescription;

    Button btnSaveEvent;

    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);



        etEventName = findViewById(R.id.etEventName);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);

        btnSaveEvent = findViewById(R.id.btnSaveEvent);



        db = FirebaseFirestore.getInstance();



        btnSaveEvent.setOnClickListener(v -> {


            String name =
                    etEventName.getText().toString().trim();

            String date =
                    etDate.getText().toString().trim();

            String location =
                    etLocation.getText().toString().trim();

            String description =
                    etDescription.getText().toString().trim();



            if(name.isEmpty() ||
                    date.isEmpty() ||
                    location.isEmpty()){


                Toast.makeText(
                        this,
                        "Please fill required fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }



            Event event =
                    new Event(
                            name,
                            date,
                            location,
                            description
                    );



            db.collection("events")
                    .add(event)
                    .addOnSuccessListener(documentReference -> {


                        Toast.makeText(
                                this,
                                "Event Added Successfully",
                                Toast.LENGTH_SHORT
                        ).show();


                        finish();


                    });



        });



    }


}