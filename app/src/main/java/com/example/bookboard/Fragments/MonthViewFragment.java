package com.example.bookboard.Fragments;

import static com.example.bookboard.Calendar.CalendarUtils.daysInMonthArray;
import static com.example.bookboard.Calendar.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Calendar.CalendarAdapter;
import com.example.bookboard.Calendar.CalendarUtils;
import com.example.bookboard.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backButton;
    private Button nextButton;
    private Button weeklyButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monthview, container, false);
        initWidgets(rootView);
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        setOnClickListener();
        return rootView;
    }

    private void setOnClickListener() {
        backButton.setOnClickListener(this::previousMonthAction);
        nextButton.setOnClickListener(this::nextMonthAction);
        weeklyButton.setOnClickListener(this::weeklyAction);
    }

    private void initWidgets(View rootView) {
        calendarRecyclerView = rootView.findViewById(R.id.calendarRecyclerView);
        monthYearText = rootView.findViewById(R.id.monthYearTV);
        backButton = rootView.findViewById(R.id.schedule_BTN_back);
        nextButton = rootView.findViewById(R.id.schedule_BTN_next);
        weeklyButton = rootView.findViewById(R.id.schedule_BTN_weekly);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {
        Fragment weekViewFragment = new WeekViewFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, weekViewFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss(); // Add this line
    }

}
