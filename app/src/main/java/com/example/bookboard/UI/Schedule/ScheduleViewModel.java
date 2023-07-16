package com.example.bookboard.UI.Schedule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScheduleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public ScheduleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Schedule Fragment"); // Update the text to match the fragment's purpose
    }

    public LiveData<String> getText() {
        return mText;
    }
}
