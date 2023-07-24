package com.example.bookboard.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reservation implements Parcelable {
    private String reservationId;
    private String roomName;
    private String roomId;
    private String date;
    private String startTime;
    private String endTime;
    private boolean isValidated;

    public Reservation() {}

    public Reservation(String reservationId, String roomName, String roomId, String date, String startTime, String endTime, boolean isValidated) {
        this.reservationId = reservationId;
        this.roomName = roomName;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isValidated = isValidated;
    }

    protected Reservation(Parcel in) {
        reservationId = in.readString();
        roomName = in.readString();
        roomId = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        isValidated = in.readByte() != 0;
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reservationId);
        dest.writeString(roomName);
        dest.writeString(roomId);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeByte((byte) (isValidated ? 1 : 0));
    }
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomId='" + roomId + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
