package com.example.bookboard;

import android.app.Application;

import com.example.bookboard.Utilities.FireBaseOperations;
import com.example.bookboard.Utilities.ImageLoader;
import com.example.bookboard.Utilities.MySharedPreferences;
import com.example.bookboard.Utilities.SignalGenerator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseOperations.getInstance();
        MySharedPreferences.init(this);
        SignalGenerator.init(this);
        ImageLoader.initImageLoader(this);
    }
}
