package com.example.bookboard.Model;

public class Room {
    private String title;
    private String roomID;
    private int posterLottieID;
    private int maxPeople;
    private String calendarID;
    private String describe;
    private String available;

    public Room() {}

    public Room(String title, int posterLottieID, int maxPeople, String describe, String available, String roomID, String calendarID) {
        this.title = title;
        this.roomID = roomID;
        this.posterLottieID = posterLottieID;
        this.maxPeople = maxPeople;
        this.calendarID = calendarID;
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

    public String getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(String roomCalendarID) {
        this.calendarID = roomCalendarID;
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
                ", roomCalendarID='" + calendarID + '\'' +
                ", describe='" + describe + '\'' +
                ", available='" + available + '\'' +
                '}';
    }
}
