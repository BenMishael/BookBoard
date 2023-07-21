package com.example.bookboard.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookboard.Interfaces.NfcListener;
import com.example.bookboard.Model.UserDB;
import com.example.bookboard.R;
import com.example.bookboard.UI.Reservations.ReservationsFragment;
import com.example.bookboard.Utilities.ImageLoader;
import com.example.bookboard.Utilities.NfcUtils;
import com.example.bookboard.Utilities.SignalGenerator;
import com.example.bookboard.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NfcListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rooms, R.id.nav_reservations, R.id.nav_myaccount)
                .setOpenableLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Set Name for user
        updateUI(navigationView);

        // Initialize NFC adapter and enable foreground dispatch
        initializeNfcAdapter();
    }

    private void updateUI(NavigationView navigationView) {
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.navheader_TEXT_username);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.navheader_TEXT_useremail);
        ImageView photo = navigationView.getHeaderView(0).findViewById(R.id.navheader_IMG_photo);
        name.setText(LoginActivity.currentUser.getDisplayName());
        email.setText(LoginActivity.currentUser.getEmail());
        String photoUrl = String.valueOf(LoginActivity.currentUser.getPhotoUrl());
        Log.d(TAG, "updateUI: " + photoUrl);
        if (!TextUtils.isEmpty(photoUrl)) {
            ImageLoader.getInstance().loadProfileImage(photoUrl, photo);
        }
    }

    private void initializeNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // NFC is not supported on this device
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Enable foreground dispatch for NFC events
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Disable foreground dispatch when the activity is paused
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handle NFC tag data here
        Log.d(TAG, "MainActivity-OnNewIntent: NFC event triggered");
        SignalGenerator.getInstance().toast("MainActivity: NFC event triggered", Toast.LENGTH_SHORT);

        // Get the NFC message as a String
        NdefMessage ndefMessage = NfcUtils.getNdefMessageFromIntent(intent);
        if (ndefMessage != null) {
            Log.d(TAG, "MainActivity: Message not null");
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "MainActivity: Message: " + message);

            // Pass the NFC message as a String to the ReservationsFragment
            ReservationsFragment reservationsFragment = (ReservationsFragment) getSupportFragmentManager().findFragmentById(R.id.nav_reservations);
            if (reservationsFragment != null) {
                reservationsFragment.onNfcDetected(ndefMessage, message);
            }
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void signOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        setEmptyDataUserDB();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setEmptyDataUserDB() {
        UserDB.getInstance().setAllReservations(new ArrayList<>());
    }

    @Override
    public void onNfcDetected(NdefMessage ndefMessage, String message) {
        // Handle NFC data received from NFCFragment here if needed
        Log.d(TAG, "Received NFC Message in MainActivity: " + message);
        SignalGenerator.getInstance().vibrate();
        SignalGenerator.getInstance().toast("MainActivity: " + message, Toast.LENGTH_SHORT);
    }
}
