package com.example.eventbookingapp;

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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ArrayList<User> userList;
    private final FirebaseFirestore db;

    public UserAdapter(ArrayList<User> userList) {
        this.userList = userList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_user,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        User user = userList.get(position);

        // -------------------------
        // USER NAME
        // -------------------------
        String name = user.getName();

        if (name == null || name.trim().isEmpty()) {
            name = "Unknown User";
        }

        holder.txtName.setText(name);


        // -------------------------
        // USER EMAIL
        // -------------------------
        String email = user.getEmail();

        if (email == null || email.trim().isEmpty()) {
            email = "No email available";
        }

        holder.txtEmail.setText(email);


        // -------------------------
        // USER ROLE
        // -------------------------
        String role = user.getRole();

        if (role == null || role.trim().isEmpty()) {
            role = "User";
        }

        holder.txtRole.setText(
                holder.itemView.getContext()
                        .getString(R.string.user_role_format, role)
        );


        // -------------------------
        // USER STATUS
        // -------------------------
        String status = user.getStatus();

        if (status == null || status.trim().isEmpty()) {
            status = "Active";
        }

        holder.txtStatus.setText(
                holder.itemView.getContext()
                        .getString(R.string.user_status_format, status)
        );


        // -------------------------
        // STATUS COLOR
        // -------------------------
        if ("active".equalsIgnoreCase(status)) {

            holder.txtStatus.setTextColor(
                    Color.parseColor("#166534")
            );

        } else {

            holder.txtStatus.setTextColor(
                    Color.parseColor("#B45309")
            );
        }


        // -------------------------
        // DELETE USER
        // -------------------------
        holder.btnDeleteUser.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {

                return;
            }

            User selectedUser =
                    userList.get(currentPosition);

            deleteUser(
                    selectedUser,
                    currentPosition,
                    holder
            );
        });
    }


    // =====================================================
    // DELETE USER FROM FIRESTORE
    // =====================================================
    private void deleteUser(
            User user,
            int position,
            ViewHolder holder) {

        String userId = user.getId();

        if (userId == null ||
                userId.trim().isEmpty()) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    R.string.user_id_not_found,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ---------------------------------------------
        // PREVENT ADMIN DELETION
        // ---------------------------------------------
        String role = user.getRole();

        if ("admin".equalsIgnoreCase(role)) {

            Toast.makeText(
                    holder.itemView.getContext(),
                    R.string.admin_cannot_be_deleted,
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        // ---------------------------------------------
        // DISABLE BUTTON WHILE DELETING
        // ---------------------------------------------
        holder.btnDeleteUser.setEnabled(false);

        holder.btnDeleteUser.setText(
                R.string.deleting_user
        );


        // ---------------------------------------------
        // DELETE FIRESTORE USER DOCUMENT
        // ---------------------------------------------
        db.collection("users")
                .document(userId)
                .delete()
                .addOnSuccessListener(unused -> {

                    if (position >= 0 &&
                            position < userList.size()) {

                        userList.remove(position);

                        notifyItemRemoved(position);
                    }

                    Toast.makeText(
                            holder.itemView.getContext(),
                            R.string.user_deleted_successfully,
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> {

                    holder.btnDeleteUser.setEnabled(true);

                    holder.btnDeleteUser.setText(
                            R.string.delete_user
                    );

                    Toast.makeText(
                            holder.itemView.getContext(),
                            R.string.failed_to_delete_user,
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // =====================================================
    // ITEM COUNT
    // =====================================================
    @Override
    public int getItemCount() {
        return userList.size();
    }


    // =====================================================
    // VIEW HOLDER
    // =====================================================
    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtEmail;
        TextView txtRole;
        TextView txtStatus;

        Button btnDeleteUser;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );

            txtEmail =
                    itemView.findViewById(
                            R.id.txtEmail
                    );

            txtRole =
                    itemView.findViewById(
                            R.id.txtRole
                    );

            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );

            btnDeleteUser =
                    itemView.findViewById(
                            R.id.btnDeleteUser
                    );
        }
    }
}