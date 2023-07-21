package com.example.bookboard.UI.Rooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoomsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public RoomsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Schedule Fragment"); // Update the text to match the fragment's purpose
    }

    public LiveData<String> getText() {
        return mText;
    }
}
