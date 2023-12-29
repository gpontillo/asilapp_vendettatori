package com.vendettatori.asilapp.db;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private final FirebaseFirestore db;

    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void setUser(String userId, @NonNull UserAnagrafici user, IHandlerDBUser handler) {
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(documentReference -> {
                    handler.onSuccessSetUser();
                })
                .addOnFailureListener(e -> {
                    handler.onFailureSetUser(e);
                });
    }

    public void getUser(String userId, IHandlerDBUser handler) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        UserAnagrafici userData = document.toObject(UserAnagrafici.class);
                        handler.onSuccessRetrieveUser(userData);
                    } else {
                        handler.onFailureRetrieveUser(new RuntimeException("Accessing unknown user"));
                    }
                });
    }
}
