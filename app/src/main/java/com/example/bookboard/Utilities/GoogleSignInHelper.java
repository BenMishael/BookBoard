package com.example.bookboard.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleSignInHelper {

    private static final int RC_SIGN_IN = 1001;

    private final Context context;
    private final GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private OnGoogleSignInListener signInListener;
    private GoogleSignInAccount googleSignInAccount; // Added variable to hold the signed-in account

    public GoogleSignInHelper(Context context, String webClientId) {
        this.context = context;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
    }

    public void setSignInListener(OnGoogleSignInListener listener) {
        signInListener = listener;
    }

    public void initializeGoogleSignIn() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            googleSignInAccount = result.getSignInAccount(); // Save the signed-in account
            if (googleSignInAccount != null) {
                String idToken = googleSignInAccount.getIdToken();
                if (signInListener != null) {
                    signInListener.onGoogleSignInSuccess(idToken);
                }
            }
        } else {
            if (signInListener != null) {
                signInListener.onGoogleSignInFailure();
            }
        }
    }

    // Added function to retrieve the signed-in account
    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public interface OnGoogleSignInListener {
        void onGoogleSignInSuccess(String idToken);

        void onGoogleSignInFailure();
    }
}
