package com.example.bookboard.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookboard.Calendar.CalendarUtils;
import com.example.bookboard.Calendar.Event;
import com.example.bookboard.R;

import java.time.LocalTime;

public class EventEditFragment extends Fragment {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button saveEvent;
    private LocalTime time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_edit, container, false);
        initWidgets(rootView);
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        setOnClickListener();
        return rootView;
    }

    private void setOnClickListener() {
        saveEvent.setOnClickListener(this::saveEventAction);
    }

    private void initWidgets(View rootView) {
        eventNameET = rootView.findViewById(R.id.eventNameET);
        eventDateTV = rootView.findViewById(R.id.eventDateTV);
        eventTimeTV = rootView.findViewById(R.id.eventTimeTV);
        saveEvent = rootView.findViewById(R.id.eventedit_BTN_saveeventaction);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        requireActivity().finish();
    }
}
