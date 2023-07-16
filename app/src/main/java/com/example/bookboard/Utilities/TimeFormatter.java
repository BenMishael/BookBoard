package com.example.bookboard.Utilities;

public class TimeFormatter {
    public static String getFormattedTime(int duration) {
        int h = duration / 60;
        int m = duration % 60;
        String hh = h < 10 ? "0" + h : "" + h;
        String mm = m < 10 ? "0" + m : "" + m;
        return hh + "h " + mm + "m";
    }
}
