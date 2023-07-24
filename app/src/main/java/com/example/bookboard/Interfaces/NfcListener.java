package com.example.bookboard.Interfaces;

import android.nfc.NdefMessage;

public interface NfcListener {
    void onNfcDetected(NdefMessage ndefMessage, String message);
}
