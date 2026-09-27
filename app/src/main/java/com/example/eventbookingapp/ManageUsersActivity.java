package com.example.eventbookingapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerUsers;
    private ArrayList<User> userList;
    private UserAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerUsers = findViewById(R.id.recyclerUsers);

        recyclerUsers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        userList = new ArrayList<>();

        adapter = new UserAdapter(userList);

        recyclerUsers.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null) {
            loadUsers();
        }
    }

    private void loadUsers() {

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    userList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {

                        Toast.makeText(
                                ManageUsersActivity.this,
                                "No users found in Firestore",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        for (QueryDocumentSnapshot document :
                                queryDocumentSnapshots) {

                            User user =
                                    document.toObject(User.class);

                            user.setId(document.getId());

                            userList.add(user);
                        }

                        adapter.notifyDataSetChanged();

                        Toast.makeText(
                                ManageUsersActivity.this,
                                userList.size() + " user(s) loaded",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            ManageUsersActivity.this,
                            "Firestore Error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
