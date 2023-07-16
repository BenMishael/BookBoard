package com.example.bookboard.Model;
import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private String title;
    private int posterLottieID;
    private int maxPeople;
    private String describe;
    private String available;

    public Room() {}

    public Room(String title, int posterLottieID, int maxPeople, String describe, String available) {
        this.title = title;
        this.posterLottieID = posterLottieID;
        this.maxPeople = maxPeople;
        this.describe = describe;
        this.available = available;
    }

    protected Room(Parcel in) {
        title = in.readString();
        posterLottieID = in.readInt();
        maxPeople = in.readInt();
        describe = in.readString();
        available = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

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
                ", posterLottieID=" + posterLottieID +
                ", maxPeople=" + maxPeople +
                ", describe='" + describe + '\'' +
                ", available='" + available + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(posterLottieID);
        dest.writeInt(maxPeople);
        dest.writeString(describe);
        dest.writeString(available);
    }
}
