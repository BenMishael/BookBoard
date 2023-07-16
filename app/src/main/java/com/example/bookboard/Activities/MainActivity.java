package com.example.bookboard.Activities;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
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

import com.example.bookboard.Interfaces.NFCListener;
import com.example.bookboard.Model.Reservation;
import com.example.bookboard.Model.UserDB;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.ImageLoader;
import com.example.bookboard.Utilities.NfcUtils;
import com.example.bookboard.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NFCListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;
    private NfcUtils nfcUtils;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Initialize NFC Utils
        nfcUtils = NfcUtils.getInstance(this);
        nfcAdapter = nfcUtils.getNfcAdapter();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_schedule, R.id.nav_reservations, R.id.nav_myaccount)
                .setOpenableLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Set Name for user
        updateUI(navigationView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcUtils.enableForegroundDispatch(this, getIntent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcUtils.disableForegroundDispatch(this);
        }
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
        UserDB.getInstance().setCurrentReservation(new Reservation());
        UserDB.getInstance().setAllReservations(new ArrayList<>());
    }

    @Override
    public void onNFCIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            handleNfcIntent(intent);
        }
    }

    private void handleNfcIntent(Intent intent) {
        NfcUtils nfcUtils = NfcUtils.getInstance(this);
        NdefMessage[] messages = nfcUtils.getNdefMessages(intent);
        if (messages != null && messages.length > 0) {
            NdefRecord record = messages[0].getRecords()[0];
            String tagContent = nfcUtils.getTextFromNdefRecord(record);
            validateNfcTag(tagContent);
        }
    }

    private void validateNfcTag(String tagContent) {
        if (isValidTag(tagContent)) {
            Toast.makeText(this, "Valid NFC tag", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid NFC tag", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidTag(String tagContent) {
        String validTagContent = "ABC123";
        return tagContent.equals(validTagContent);
    }
}
