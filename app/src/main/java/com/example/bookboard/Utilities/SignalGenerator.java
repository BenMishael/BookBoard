package com.example.bookboard.Utilities;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalGenerator {

    private static SignalGenerator instance;
    private static Context context;
    private static Vibrator vibrator;
    private static MediaPlayer soundsMediaPlayer;
    private static LocationManager locationManager;

    private SignalGenerator(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SignalGenerator(context);
        }
    }

    public static SignalGenerator getInstance() {
        return instance;
    }

    public void toast(String text,int length){
        Toast.makeText(context,text,length).show();
    }

    public void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    public void makeSound(int soundId){
        soundsMediaPlayer.release();
        soundsMediaPlayer = MediaPlayer.create(context,soundId);
        soundsMediaPlayer.start();
    }

    public Location getCurrentLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Do nothing
                }

                @Override
                public void onStatusChanged(String provider, int status, android.os.Bundle extras) {
                    // Do nothing
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Do nothing
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // Do nothing
                }
            });
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
