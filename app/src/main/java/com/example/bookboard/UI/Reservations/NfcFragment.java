package com.example.bookboard.UI.Reservations;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookboard.Interfaces.NfcListener;
import com.example.bookboard.R;

public class NfcFragment extends DialogFragment {
    private NfcListener mListener;
    private NdefMessage nfcMessage;
    public static final String TAG = NfcFragment.class.getSimpleName();
    public static NfcFragment newInstance() {
        return new NfcFragment();
    }
    private TextView mTvMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nfc, container, false);
        initViews(view);

        // Set the dialog size
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return view;
    }

    private void initViews(View view) {
        mTvMessage = view.findViewById(R.id.tv_message);

        LottieAnimationView animationCancel = view.findViewById(R.id.nfc_animation_cancel);
        animationCancel.setOnClickListener(v -> {
            dismiss(); // Close the dialog when the cancel button is clicked
        });
    }

    public void setNfcListener(NfcListener listener) {
        mListener = listener;
        if (nfcMessage != null) {
            handleNfcMessage(nfcMessage);
            nfcMessage = null;
        }
    }

    public void onNfcDetected(NdefMessage ndefMessage) {
        // Handle the NFC data received here
        NdefRecord firstRecord = ndefMessage.getRecords()[0];
        String payload = new String(firstRecord.getPayload());
        Log.d(TAG, "NFC Data: " + payload);

        if (mListener != null) {
            mListener.onNfcDetected(ndefMessage, payload);
        } else {
            // If the listener is not yet set, store the NFC message
            nfcMessage = ndefMessage;
        }
    }

    private void handleNfcMessage(NdefMessage ndefMessage) {
        NdefRecord firstRecord = ndefMessage.getRecords()[0];
        String payload = new String(firstRecord.getPayload());
        Log.d(TAG, "NFC Data: " + payload);
        if (mListener != null) {
            mListener.onNfcDetected(ndefMessage, payload);
        }
    }
}