//package com.example.bookboard.Utilities;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.DateTime;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.services.calendar.model.Events;
//import com.google.api.services.calendar.model.FreeBusyRequest;
//import com.google.api.services.calendar.model.FreeBusyRequestItem;
//import com.google.api.services.calendar.model.FreeBusyResponse;
//import com.google.api.services.calendar.model.TimePeriod;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//public class GoogleCalendarApi {
//    private static final String TAG = "GoogleCalendarApi";
//    private static final String APPLICATION_NAME = "Bookboard";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
//
//    private static GoogleCalendarApi instance;
//    private Calendar calendarService;
//
//    private GoogleCalendarApi(Context context) {
//        initializeCalendarService();
//    }
//
//    public static synchronized GoogleCalendarApi getInstance(Context context) {
//        if (instance == null) {
//            instance = new GoogleCalendarApi(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    private void initializeCalendarService() {
//        try {
//            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            GoogleCredentials credentials = getGoogleCredentials();
//            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
//            Calendar calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, requestInitializer)
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//            calendarService = calendar;
//        } catch (IOException | GeneralSecurityException e) {
//            Log.e(TAG, "Failed to initialize Calendar service: " + e.getMessage());
//        }
//    }
//
//
//    private GoogleCredentials getGoogleCredentials() throws IOException {
//        return GoogleCredentials.getApplicationDefault();
//    }
//
//    public List<Event> getEvents(String calendarId, Date startDate, Date endDate) throws IOException {
//        DateTime timeMin = new DateTime(startDate);
//        DateTime timeMax = new DateTime(endDate);
//        Events events = calendarService.events().list(calendarId)
//                .setTimeMin(timeMin)
//                .setTimeMax(timeMax)
//                .execute();
//        return events.getItems();
//    }
//
//    public boolean isRoomAvailable(String calendarId, Date startDate, Date endDate) throws IOException {
//        List<TimePeriod> busyPeriods = getBusyPeriods(calendarId, startDate, endDate);
//        return busyPeriods.isEmpty();
//    }
//
//    private List<TimePeriod> getBusyPeriods(String calendarId, Date startDate, Date endDate) throws IOException {
//        DateTime timeMin = new DateTime(startDate);
//        DateTime timeMax = new DateTime(endDate);
//        List<FreeBusyRequestItem> requestItems = new ArrayList<>();
//        requestItems.add(new FreeBusyRequestItem().setId(calendarId));
//        FreeBusyRequest request = new FreeBusyRequest()
//                .setTimeMin(timeMin)
//                .setTimeMax(timeMax)
//                .setItems(requestItems);
//        FreeBusyResponse response = calendarService.freebusy().query(request).execute();
//        if (response.getCalendars() != null && response.getCalendars().containsKey(calendarId)) {
//            return response.getCalendars().get(calendarId).getBusy();
//        }
//        return new ArrayList<>();
//    }
//
//    public String createBooking(String calendarId, String summary, Date startDate, Date endDate) throws IOException {
//        Event event = new Event()
//                .setSummary(summary)
//                .setStart(new EventDateTime().setDateTime(EventDateTimeConverter.convertToGoogleDateTime(startDate)))
//                .setEnd(new EventDateTime().setDateTime(EventDateTimeConverter.convertToGoogleDateTime(endDate)));
//
//        event = calendarService.events().insert(calendarId, event).execute();
//        return event.getId();
//    }
//
//    public void cancelBooking(String calendarId, String eventId) throws IOException {
//        calendarService.events().delete(calendarId, eventId).execute();
//    }
//}
