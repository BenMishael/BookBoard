package com.example.bookboard.Model;

public class Room {
    private String title;
    private String roomID;
    private int posterLottieID;
    private int maxPeople;
    private String roomCalendarID;
    private String describe;
    private String available;

    public Room() {}

    public Room(String title, int posterLottieID, int maxPeople, String describe, String available, String roomID, String roomCalendarID) {
        this.title = title;
        this.roomID = roomID;
        this.posterLottieID = posterLottieID;
        this.maxPeople = maxPeople;
        this.roomCalendarID = roomCalendarID;
        this.describe = describe;
        this.available = available;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosterLottieID() {
        return posterLottieID;
    }

    public void setPosterLottieID(int posterLottieID) {
        this.posterLottieID = posterLottieID;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getRoomCalendarID() {
        return roomCalendarID;
    }

    public void setRoomCalendarID(String roomCalendarID) {
        this.roomCalendarID = roomCalendarID;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "title='" + title + '\'' +
                ", roomID='" + roomID + '\'' +
                ", posterLottieID=" + posterLottieID +
                ", maxPeople=" + maxPeople +
                ", roomCalendarID='" + roomCalendarID + '\'' +
                ", describe='" + describe + '\'' +
                ", available='" + available + '\'' +
                '}';
    }
}
