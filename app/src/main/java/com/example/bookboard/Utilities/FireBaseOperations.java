package com.example.bookboard.Utilities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseOperations
{
    //singleton
    private static FireBaseOperations fireBaseOperations = null;

    //firebase members

    private FirebaseDatabase database ;
    private DatabaseReference databaseReference;


    public FireBaseOperations(){
        database = FirebaseDatabase.getInstance();

    }

    public static FireBaseOperations getInstance(){
        if(fireBaseOperations == null){
            fireBaseOperations = new FireBaseOperations();
        }
        return fireBaseOperations;
    }

    public DatabaseReference getDatabaseReference(String name) {
        databaseReference = database.getReference(name);
        return databaseReference;
    }

    public void getLottieAnimationUrl(String lottieID, final LottieUrlCallback callback) {
        DatabaseReference lottieRef = getDatabaseReference("lotties").child(lottieID);
        lottieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String animationUrl = dataSnapshot.getValue(String.class);
                if (callback != null)
                    callback.onUrlReceived(animationUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    public interface LottieUrlCallback {
        void onUrlReceived(String animationUrl);
    }
}