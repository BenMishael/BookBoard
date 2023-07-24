package com.example.bookboard.UI.Reservations;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Adapters.ReservationAdapter;
import com.example.bookboard.Interfaces.NfcListener;
import com.example.bookboard.Model.Reservation;
import com.example.bookboard.Model.UserDB;
import com.example.bookboard.UI.Rooms.RoomsViewModel;
import com.example.bookboard.Utilities.SignalGenerator;
import com.example.bookboard.databinding.FragmentReservationsBinding;

public class ReservationsFragment extends Fragment implements NfcListener {

    private ReservationAdapter reservationAdapter;
    private FragmentReservationsBinding binding;
    private RecyclerView reservationsRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        RoomsViewModel reservationViewModel =
                new ViewModelProvider(this).get(RoomsViewModel.class);
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        reservationsRV = binding.reservationsLSTRes;
        reservationAdapter = new ReservationAdapter(getContext());
        reservationAdapter.updateReservations(UserDB.getInstance().getAllReservations());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        reservationsRV.setLayoutManager(linearLayoutManager);
        reservationsRV.setAdapter(reservationAdapter);
        reservationAdapter.setReservationClickListener(new ReservationAdapter.ReservationClickListener() {
            @Override
            public void changeScreen(Reservation reservation) {
                // Show NFC Dialog
                showNfcDialog(); // Corrected method name
            }
        });

        return rootView;
    }

    private void showNfcDialog() {
        NfcFragment nfcFragment = NfcFragment.newInstance();
        nfcFragment.setNfcListener(this); // Set the NfcListener to the NfcFragment
        nfcFragment.show(getParentFragmentManager(), NfcFragment.TAG);
    }

    public void handleNfcIntent(Intent intent) {
        // Handle NFC tag data here
        Log.d(TAG, "ReservationsFragment: NFC event triggered");

        // Handle the NFC data received here
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage ndefMessage = (NdefMessage) intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
            if (ndefMessage != null) {
                String message = new String(ndefMessage.getRecords()[0].getPayload());
                Log.d(TAG, "NFC Tag Data: " + message);

                // Pass the NFC data to the NfcFragment using the correct method from the interface
                onNfcDetected(ndefMessage, message);
            }
        }
    }

    @Override
    public void onNfcDetected(NdefMessage ndefMessage, String message) {
        // Handle the NFC data received here
        Log.d(TAG, "Received NFC Message in ReservationsFragment: " + message);
        SignalGenerator.getInstance().vibrate();
        SignalGenerator.getInstance().toast("Reservations: " + message, Toast.LENGTH_SHORT);

        // Show the NFC dialog
        showNfcDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
