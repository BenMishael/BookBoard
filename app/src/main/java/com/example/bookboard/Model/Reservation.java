package com.example.bookboard.Model;

import com.example.bookboard.Utilities.FireBaseOperations;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class Reservation {
    private String id;
    private String roomId;
    private Date time;

    public Reservation() {
        // Default constructor required for Firebase serialization
    }

    public Reservation(String id, String roomId, Date time) {
        this.id = id;
        this.roomId = roomId;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getRoomId() {
        return roomId;
    }

    public Date getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void saveReservation() {
        FireBaseOperations fireBaseOperations = FireBaseOperations.getInstance();
        DatabaseReference reservationsRef = fireBaseOperations.getDatabaseReference("reservations");
        reservationsRef.child(id).setValue(this);
    }
}
