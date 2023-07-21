package com.example.bookboard.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

}