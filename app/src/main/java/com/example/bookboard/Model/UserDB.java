package com.example.bookboard.Model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserDB {
    private ArrayList<Reservation> allReservations;
    private String name;
    private static UserDB userDB = null;

    // Constructors
    private UserDB() {
        allReservations = new ArrayList<>();
    }

    private UserDB(FirebaseUser currentUser) {
        allReservations = new ArrayList<>();
        this.name = currentUser.getDisplayName();
    }

    // Initialize
    public static void init(FirebaseUser currentUser) {
        if (userDB == null) {
            userDB = new UserDB(currentUser);
        }
    }

    // Get Functions
    public static UserDB getInstance() {
        return userDB;
    }
    public ArrayList<Reservation> getAllReservations() {
        return allReservations;
    }
    public String getName() {
        return name;
    }

    // Set Functions
    public void setUser(UserDB user) {
        this.name = user.name;
        this.allReservations = user.allReservations;
    }
    public void setAllReservations(ArrayList<Reservation> allReservations) {
        this.allReservations = allReservations;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Adding Functions
    public void addReservation(Reservation reservation) {
        allReservations.add(0, reservation);
    }

    // Removing Functions
    public void removeReservation(Reservation reservation) {
        allReservations.removeIf(r -> r.equals(reservation));
    }

}
