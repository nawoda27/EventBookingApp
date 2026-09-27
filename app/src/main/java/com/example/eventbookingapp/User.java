package com.example.eventbookingapp;

public class User {

    private String id;
    private String name;
    private String email;
    private String role;
    private String status;

    // Required empty constructor for Firestore
    public User() {
    }

    public User(String name, String email, String role, String status) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    // ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}