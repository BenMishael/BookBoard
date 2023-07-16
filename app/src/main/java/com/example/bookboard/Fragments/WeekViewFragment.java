package com.example.bookboard.Fragments;

import static com.example.bookboard.Calendar.CalendarUtils.daysInWeekArray;
import static com.example.bookboard.Calendar.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Calendar.CalendarAdapter;
import com.example.bookboard.Calendar.CalendarUtils;
import com.example.bookboard.Calendar.Event;
import com.example.bookboard.Calendar.EventAdapter;
import com.example.bookboard.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button nextButton;
    private Button backButton;
    private Button newEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        initWidgets(rootView);
        setWeekView();
        setOnClickListener();
        return rootView;
    }

    private void setOnClickListener() {
        newEventButton.setOnClickListener(this::newEventAction);
        backButton.setOnClickListener(this::previousWeekAction);
        nextButton.setOnClickListener(this::nextWeekAction);
    }

    private void initWidgets(View rootView) {
        calendarRecyclerView = rootView.findViewById(R.id.calendarRecyclerView);
        monthYearText = rootView.findViewById(R.id.monthYearTV);
        eventListView = rootView.findViewById(R.id.eventListView);
        nextButton = rootView.findViewById(R.id.weekfrag_BTN_next);
        backButton = rootView.findViewById(R.id.weekfrag_BTN_back);
        newEventButton = rootView.findViewById(R.id.weekfrag_BTN_newevent);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(requireContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {
        Fragment eventEditFragment = new EventEditFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, eventEditFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss(); // Add this line
    }

}

