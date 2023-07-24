package com.example.bookboard.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class UserDB {
    private ArrayList<Reservation> allReservations;
    private String name;
    private static UserDB userDB = null;

    // Private constructor for Singleton
    private UserDB() {
        allReservations = new ArrayList<>();
    }

    private UserDB(FirebaseUser currentUser) {
        this.allReservations = new ArrayList<>();
        this.name = currentUser.getDisplayName();
    }

    // Thread-safe Singleton implementation
    public static UserDB getInstance(){return userDB;}

    // Initialize with currentUser
    public static void init(FirebaseUser currentUser){
        if (userDB == null) {
            userDB = new UserDB(currentUser);
        }
    }

    // Getters
    public ArrayList<Reservation> getAllReservations() {
        return allReservations;
    }
    public String getName() {
        return name;
    }

    // Setters
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

    // Adding Function
    public void addReservation(Reservation reservation) {
        allReservations.add(0, reservation);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("UserDB").child(userId).child("allReservations");
            userReference.setValue(allReservations)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Reservation added to Firebase successfully.");
                            } else {
                                Log.e(TAG, "Failed to add reservation to Firebase: " + task.getException());
                            }
                        }
                    });
        }
    }




}
