package com.example.bookboard.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookboard.Model.UserDB;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.Constants;
import com.example.bookboard.Utilities.FireBaseOperations;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static FirebaseUser currentUser = null;
    private DatabaseReference reference = FireBaseOperations.getInstance().getDatabaseReference(Constants.USER_DB);
    private boolean isNewUser;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        animationView = findViewById(R.id.rooms_LTV_nfc);
        animationView.playAnimation();
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
    }

    private void createNewUserDB() {
        UserDB.getInstance().setName(currentUser.getDisplayName());
        reference.child(currentUser.getUid()).setValue(UserDB.getInstance());
    }

    /**
     *
     * Google API, checking if the currentUser is null if yes, he will need to login/Register.
     * If the user will register the data will be written in FB, if the user already exists,
     * the it will load from FB
     */
    private void login(FirebaseUser currentUser) {
        if (currentUser == null) {//not found in DB
            // Choose authentication providers

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers).setTheme(R.style.firebaseTheme).setLogo(R.drawable.bookboard).setIsSmartLockEnabled(false)
                    .build();
            signInLauncher.launch(signInIntent);

        } else {
            UserDB.init(currentUser);
            checkAlreadyExists();
        }
    }



    /**
     * Triggered the FB with Query and check if the user already exists
     * if the user exists it will change to boolean isNewUser to false
     * and connect him to the app with his own data
     */
    private void checkAlreadyExists() {
        Query query = reference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isNewUser = true;
                for (DataSnapshot snap:snapshot.getChildren()) {
                    if (currentUser.getUid().equals(snap.getKey())) {
                        loadUserFromDB();
                        isNewUser = false;
                        break;

                    }
                }
                if (isNewUser){
                    createNewUserDB();}
                switchScreen();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    /**
     * Getting the current user by uid
     */
    private void loadUserFromDB() {
        reference = reference.child(currentUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDB.getInstance().setUser(snapshot.getValue(UserDB.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void switchScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        login(currentUser);
        animationView.resumeAnimation(); // Add this line to resume the animation
    }
}