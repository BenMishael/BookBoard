package com.example.bookboard.Model;

import com.example.bookboard.Utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeSlot {
    private String date; // New date field
    private String startTime;
    private String endTime;
    private String title;
    private int position;

    public TimeSlot(String date, String startTime, String endTime, String title) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.position = -1;
    }

    public TimeSlot(String date, String title, Date startDateTime, Date endDateTime) {
        this.date = date;
        this.startTime = DateUtils.getFormattedTime(startDateTime);
        this.endTime = DateUtils.getFormattedTime(endDateTime);
        this.title = title;
        this.position = -1;
    }

    public String getDate() {
        return date;
    }

    // Format the start and end times in RFC3339 format for Google Calendar API
    public String getStartDateTime() {
        return formatDateForAPI(this.date, this.startTime);
    }

    public String getEndDateTime() {
        return formatDateForAPI(this.date, this.endTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    private String formatDateForAPI(String date, String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateTime = inputFormat.parse(date + " " + time);
            return outputFormat.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", title='" + title + '\'' +
                ", position=" + position +
                '}';
    }

}