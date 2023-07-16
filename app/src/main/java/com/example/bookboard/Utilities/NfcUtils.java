package com.example.bookboard.Utilities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;

public class NfcUtils {
    private static NfcUtils instance;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;

    private NfcUtils(Context context) {
        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        if (nfcManager != null) {
            nfcAdapter = nfcManager.getDefaultAdapter();
        }
    }

    public static synchronized NfcUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NfcUtils(context.getApplicationContext());
        }
        return instance;
    }

    public NfcAdapter getNfcAdapter() {
        return nfcAdapter;
    }

    public PendingIntent getPendingIntent(Activity activity) {
        if (pendingIntent == null) {
            Intent intent = new Intent(activity, activity.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        }
        return pendingIntent;
    }

    public IntentFilter[] getIntentFilters() {
        return intentFilters;
    }

    public String[][] getTechLists() {
        return techLists;
    }

    public void enableForegroundDispatch(Activity activity, Intent intent) {
        if (nfcAdapter != null) {
            if (pendingIntent == null) {
                pendingIntent = getPendingIntent(activity);
            }
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, techLists);
        }
    }

    public void disableForegroundDispatch(Activity activity) {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(activity);
        }
    }

    public boolean isNFCEnabled() {
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }

    public NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] messages = null;
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null && rawMessages.length > 0) {
            messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
        }
        return messages;
    }


    public String getTextFromNdefRecord(NdefRecord record) {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0x3F;
        try {
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
