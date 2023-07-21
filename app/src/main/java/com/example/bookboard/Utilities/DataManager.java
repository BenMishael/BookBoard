package com.example.bookboard.Utilities;

import com.example.bookboard.Model.Reservation;
import com.example.bookboard.Model.Room;
import com.example.bookboard.R;

import java.util.ArrayList;

public class DataManager {

    // public Room(String title, int posterLottieID, int maxPeople, String describe, String available, String roomID, String roomCalendarID)
    public static ArrayList<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        rooms.add(new Room("Room A", R.raw.lottie_workspace_1, 4, "Cozy room with a view", "Yes", "4zBcA9nLX5aPvAvbevbX", "5315572e6b0ebbc676f8cf58e371a2d87eccedaa14e4a5ed74f2fbc8bfe577ac@group.calendar.google.com"));
        rooms.add(new Room("Room B", R.raw.lottie_workspace_2, 2, "Small but comfortable", "Yes", "uEjPpjGnF8hZk6chq7Mp", "e275ec4c207705d075404a97d84a35c1b3a3722091f0cdc467c4b92204e66fd9@group.calendar.google.com"));
        rooms.add(new Room("Room C", R.raw.lottie_workspace_3, 3, "Spacious with modern decor", "Yes", "M7TmJSGkfmjtrMmxDQ8P", "61b6013f4f82edc0009443b5de7e9c7652c836516cf62277f4fa91339d9a779d@group.calendar.google.com"));
        rooms.add(new Room("Room D", R.raw.lottie_workspace_4, 6, "Large suite for groups", "Yes", "vtp3VQRURVnTujncVHBj", "53ceda37a1cf6cbce2627558d1cce4b8b731e7336d42c196624b7fd6da39385e@group.calendar.google.com"));
        rooms.add(new Room("Room E", R.raw.lottie_workspace_5, 2, "Quiet room with a work desk", "Yes", "JcXyfEdVX57G3KxXgx6S", "dd8f5a633bcd05902b26d7bebf0cccfe400e59860d7b45beaf4dbd55354e7907@group.calendar.google.com"));
        rooms.add(new Room("Room F", R.raw.lottie_workspace_6, 4, "Family-friendly with a balcony", "Yes", "FKTufT5Wp8CEpbZs5rFZ", "c347887697b54eb884870114798a1b49647a77feebc34038ba93ccadd7349fa3@group.calendar.google.com"));

        return rooms;
    }

    // Reservation(String reservationId, String roomName, String roomId, String date, String startTime, String endTime, boolean isValidated)
    public static ArrayList<Reservation> getReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();

        reservations.add(new Reservation("res001", "Room A", "4zBcA9nLX5aPvAvbevbX", "2023-07-22", "14:00", "16:00", true));
        reservations.add(new Reservation("res002", "Room B", "uEjPpjGnF8hZk6chq7Mp", "2023-07-23", "10:00", "12:00", true));
        reservations.add(new Reservation("res003", "Room C", "M7TmJSGkfmjtrMmxDQ8P", "2023-07-24", "09:00", "11:00", false));
        reservations.add(new Reservation("res004", "Room D", "vtp3VQRURVnTujncVHBj", "2023-07-25", "15:00", "17:00", true));
        reservations.add(new Reservation("res005", "Room E", "JcXyfEdVX57G3KxXgx6S", "2023-07-26", "13:00", "15:00", false));
        reservations.add(new Reservation("res006", "Room F", "FKTufT5Wp8CEpbZs5rFZ", "2023-07-27", "11:00", "13:00", true));

        return reservations;
    }
}
