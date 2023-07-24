package com.example.bookboard.UI.Rooms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookboard.Activities.LoginActivity;
import com.example.bookboard.Adapters.TimeSlotAdapter;
import com.example.bookboard.Model.Reservation;
import com.example.bookboard.Model.Room;
import com.example.bookboard.Model.TimeSlot;
import com.example.bookboard.Model.UserDB;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.Constants;
import com.example.bookboard.Utilities.DataManager;
import com.example.bookboard.Utilities.DateUtils;
import com.example.bookboard.Utilities.FireBaseOperations;
import com.example.bookboard.databinding.FragmentCalendarBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CalendarFragment extends Fragment implements TimeSlotAdapter.SlotClickListener {

    private static final String TAG = "CalendarFragment";
    private FragmentCalendarBinding binding;
    private String selectedRoomID;
    private String selectedCalendarID;
    private Room selectedRoom;
    private TimeSlotAdapter timeSlotAdapter;
    private GoogleAccountCredential googleCredential;
    private Calendar googleCalendar;
    private GoogleSignInOptions gso;
    private DatabaseReference reference = FireBaseOperations.getInstance().getDatabaseReference(Constants.USER_DB);
    private static final int REQUEST_AUTHORIZATION = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        if (getArguments() != null && getArguments().containsKey(Constants.ARGS_ROOMID)) {
            selectedRoomID = getArguments().getString(Constants.ARGS_ROOMID);
            selectedRoom = findRoomById(selectedRoomID);
            if (selectedRoom != null) {
                requireActivity().setTitle(selectedRoom.getTitle());
                selectedCalendarID = selectedRoom.getCalendarID();
            }
        }

        if (selectedRoom == null || selectedCalendarID == null) {
            navigateBackToRoomsFragment();
            return;
        }

        initializeGoogleSignIn();

        setupAvailableSlotsRecyclerView();

        setupCalendarView();

//        // Grant write access to the calendar
//        grantWriteAccessToCalendar();
    }

    private void loadAvailableSlots(Date selectedDate) {
        Log.d(TAG, "loadAvailableSlots for date: " + selectedDate.toString());
        new FetchAvailableSlotsTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectedDate);
    }

    private void loadAvailableSlots(List<TimeSlot> availableTimeSlots) {
        Log.d(TAG, "loadAvailableSlots with " + availableTimeSlots.size() + " slots");
        timeSlotAdapter.updateSlots(availableTimeSlots);
    }

    private static class FetchAvailableSlotsTask extends AsyncTask<Date, Void, List<TimeSlot>> {
        private final WeakReference<CalendarFragment> fragmentReference;

        FetchAvailableSlotsTask(CalendarFragment fragment) {
            fragmentReference = new WeakReference<>(fragment);
        }

        @Override
        protected List<TimeSlot> doInBackground(Date... dates) {
            CalendarFragment fragment = fragmentReference.get();
            if (fragment == null) return null;

            Date selectedDate = dates[0];
            return fragment.fetchAvailableSlotsFromRoomCalendar(selectedDate);
        }

        @Override
        protected void onPostExecute(List<TimeSlot> availableTimeSlots) {
            CalendarFragment fragment = fragmentReference.get();
            if (fragment == null) return;

            if (availableTimeSlots != null) {
                fragment.loadAvailableSlots(availableTimeSlots);
                Log.d(TAG, "onPostExecute: availableSlots " + availableTimeSlots);
            } else {
                Toast.makeText(fragment.requireContext(), "Failed to fetch available slots.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<TimeSlot> fetchAvailableSlotsFromRoomCalendar(Date selectedDate) {
        HttpTransport httpTransport;
        JsonFactory jsonFactory;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<TimeSlot> timeSlots = new ArrayList<>();

        // Define the static time slots
        List<String> staticSlots = Arrays.asList("09:00-12:00", "12:00-15:00", "15:00-18:00", "18:00-21:00");

        try {
            java.util.Calendar startTime = java.util.Calendar.getInstance();
            startTime.setTime(selectedDate);
            startTime.set(java.util.Calendar.HOUR_OF_DAY, 0);
            startTime.set(java.util.Calendar.MINUTE, 0);

            java.util.Calendar endTime = java.util.Calendar.getInstance();
            endTime.setTime(selectedDate);
            endTime.set(java.util.Calendar.HOUR_OF_DAY, 23);
            endTime.set(java.util.Calendar.MINUTE, 59);

            Log.d(TAG, "Fetching events from " + startTime.getTime() + " to " + endTime.getTime());

            // Build the Google Calendar service with the provided GoogleAccountCredential
            Calendar googleCalendarService = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport,
                    jsonFactory,
                    googleCredential)
                    .setApplicationName(getString(R.string.app_name))
                    .build();

            Events events = googleCalendarService.events().list(selectedCalendarID)
                    .setTimeMin(new com.google.api.client.util.DateTime(startTime.getTime()))
                    .setTimeMax(new com.google.api.client.util.DateTime(endTime.getTime()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            Log.d(TAG, "fetchAvailableSlotsFromRoomCalendar: events: " + events);

            List<Event> items = events.getItems();

            // Get the formatted date for TimeSlot
            String formattedDate = DateUtils.getFormattedDate(selectedDate);

            if (items != null && !items.isEmpty()) {
                for (String staticSlot : staticSlots) {
                    boolean isSlotReserved = false;
                    for (Event event : items) {
                        com.google.api.client.util.DateTime startDateTime = event.getStart().getDateTime();
                        if (startDateTime != null) {
                            Log.d(TAG, "startDateTime.getValue(): " +startDateTime.getValue());
                            Date slotStartTime = new Date(startDateTime.getValue());
                            java.util.Calendar calendar = java.util.Calendar.getInstance();
                            calendar.setTime(slotStartTime);
                            calendar.add(java.util.Calendar.HOUR, 3); // Add 3 hours to get the end time
                            Date slotEndTime = calendar.getTime();

                            String formattedStartTime = DateUtils.getFormattedTime(slotStartTime);
                            String formattedEndTime = DateUtils.getFormattedTime(slotEndTime);

                            // Check if the current static slot is reserved by comparing the formatted times
                            if (staticSlot.equals(formattedStartTime + "-" + formattedEndTime)) {
                                isSlotReserved = true;
                                timeSlots.add(new TimeSlot(formattedDate, formattedStartTime, formattedEndTime, event.getSummary()));
                                break;
                            }
                        }
                    }
                    // If the static slot is not reserved, add it as an available slot
                    if (!isSlotReserved) {
                        String[] times = staticSlot.split("-");
                        timeSlots.add(new TimeSlot(formattedDate, times[0], times[1], "Available"));
                    }
                }
            } else {
                // If there are no events on the selected day, populate the slots list with all static time slots as available
                for (String staticSlot : staticSlots) {
                    String[] times = staticSlot.split("-");
                    timeSlots.add(new TimeSlot(formattedDate, times[0], times[1], "Available"));
                }
            }

            Log.d(TAG, "Fetched " + timeSlots.size() + " events from Google Calendar API.");
        } catch (UserRecoverableAuthIOException e) {
            startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timeSlots;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_AUTHORIZATION && resultCode == Activity.RESULT_OK) {
            Date selectedDate = java.util.Calendar.getInstance().getTime();
            loadAvailableSlots(selectedDate);
        }
    }

    private Room findRoomById(String roomID) {
        List<Room> rooms = DataManager.getRooms();
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    private void navigateBackToRoomsFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }

    private void setupCalendarView() {
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Date selectedDate = DateUtils.getDate(year, month, dayOfMonth);
            loadAvailableSlots(selectedDate);
        });
    }

    private void setupAvailableSlotsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.timeSlotsRecyclerView.setLayoutManager(layoutManager);
        timeSlotAdapter = new TimeSlotAdapter(new ArrayList<>());
        binding.timeSlotsRecyclerView.setAdapter(timeSlotAdapter);
        timeSlotAdapter.setSlotClickListener(this);
    }

    private void initializeGoogleSignIn() {
        // Initialize Google SignInOptions
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR), new Scope(CalendarScopes.CALENDAR_EVENTS))
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Check if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            // User is already signed in, setup the Google Calendar with the existing account
            googleCredential = GoogleAccountCredential.usingOAuth2(
                    requireContext(), Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS));
            googleCredential.setSelectedAccount(account.getAccount());
            setupGoogleCalendar(); // Updated to use setupGoogleCalendar() method without any parameters

            // Load available slots for the current date
            Date today = java.util.Calendar.getInstance().getTime();
            loadAvailableSlots(today);
        } else {
            // User is not signed in, start the sign-in flow
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_AUTHORIZATION);
        }
    }


    private void setupGoogleCalendar() {
        HttpTransport httpTransport;
        JsonFactory jsonFactory;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Read the service account credentials from the JSON file
        InputStream credentialsStream = getResources().openRawResource(R.raw.credentials); // Replace "your_credentials_file" with the actual file name
        GoogleCredential googleCredential;
        try {
            googleCredential = GoogleCredential.fromStream(credentialsStream, httpTransport, jsonFactory)
                    .createScoped(Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        googleCalendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport,
                jsonFactory,
                googleCredential)
                .setApplicationName(getString(R.string.app_name))
                .build();
    }



    private void signInToGoogleAccount() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_AUTHORIZATION);
    }

    @Override
    public void onSlotClick(TimeSlot timeSlot) {
        if (timeSlot.getTitle().equals("Available")) {
            // Slot is available, show a dialog to confirm booking
            showBookingConfirmationDialog(timeSlot);
        } else {
            // Slot is reserved, show event details or perform other actions
            Toast.makeText(requireContext(), "Slot reserved: " + timeSlot.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBookSlot(TimeSlot timeSlot) {
        // Handle booking the slot and creating a new event in the user's Google Calendar
        new CreateEventTask(timeSlot).execute(timeSlot);
    }

    private void showBookingConfirmationDialog(TimeSlot timeSlot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Please click on Reserve to book this timeslot, otherwise click cancel.")
                .setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBookSlot(timeSlot);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private class CreateEventTask extends AsyncTask<TimeSlot, Void, Event> {
        private TimeSlot timeSlot;
        private boolean requiresAuthorization = false;

        CreateEventTask(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
        }

        @Override
        protected Event doInBackground(TimeSlot... timeSlots) {
            TimeSlot timeSlot = timeSlots[0];
            Log.d(TAG, "doInBackground: timeSlots[0]" + timeSlots[0]);
            Event event = new Event()
                    .setSummary(getUserEmail()) // Use the user's email as the event title
                    .setDescription("Booked from BookBoard App"); // Add a description if needed

            Date startDate = DateUtils.getDateFromString(timeSlot.getDate() + " " + timeSlot.getStartTime()); // Correct order of concatenation
            Date endDate = DateUtils.getDateFromString(timeSlot.getDate() + " " + timeSlot.getEndTime()); // Correct order of concatenation

            event.setStart(new com.google.api.services.calendar.model.EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDate, TimeZone.getTimeZone("GMT"))));

            event.setEnd(new com.google.api.services.calendar.model.EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(endDate, TimeZone.getTimeZone("GMT"))));

            try {
                Event backEvent = googleCalendar.events().insert(selectedCalendarID, event).execute();
                Log.d(TAG, "doInBackground: " + backEvent);
                return backEvent;
            } catch (UserRecoverableAuthIOException e) {
                requiresAuthorization = true;
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(Event createdEvent) {
            if (createdEvent != null) {
                // Event created successfully, update the UI with the booked slot
                Toast.makeText(requireContext(), "Slot booked successfully.", Toast.LENGTH_SHORT).show();

                // Retrieve room object using the roomId and get the roomName from it
                Room room = findRoomById(selectedRoomID);
                if (room == null) {
                    Log.d(TAG, "onPostExecute: Room does not exist");
                    return; // Exit the method as there's nothing more to do without a valid room
                }

                String roomName = room.getTitle();
                String roomId = selectedRoomID;
                String reservationId = createdEvent.getId(); // Assuming the Event's ID is used as the reservationId

                boolean isValidated = false; // Change according to your app's logic
                Reservation newReservation = new Reservation(reservationId, roomName, roomId, timeSlot.getDate(), timeSlot.getStartTime(), timeSlot.getEndTime(), isValidated);

                // Add the new reservation to the UserDB singleton
                UserDB.getInstance().addReservation(newReservation);

                // The rest of your code to update the UI and adapter
                reference = reference.child(LoginActivity.currentUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        timeSlotAdapter.notifyDataSetChanged(); // Refresh the RecyclerView adapter
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
                timeSlot.updateTitle(getUserEmail()); // Update the title of the slot with the user's email
                timeSlotAdapter.notifyItemChanged(timeSlot.getPosition()); // Notify the adapter about the change in the slot
            } else if (requiresAuthorization) {
                // Requires user authorization, start the authorization flow
                signInToGoogleAccount();
            } else {
                // Failed to create the event
                Toast.makeText(requireContext(), "Failed to book the slot. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }



    }






    private String getUserEmail() {
        // Implement your logic to get the user's email
        // For example, you can use GoogleSignIn to get the signed-in account's email
        // Replace this with the actual logic to get the user's email
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}