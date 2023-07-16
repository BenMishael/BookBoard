package com.example.bookboard.Utilities;

import com.google.api.client.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDateTimeConverter {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }

    public static DateTime convertToGoogleDateTime(Date date) {
        return new DateTime(date);
    }
}
