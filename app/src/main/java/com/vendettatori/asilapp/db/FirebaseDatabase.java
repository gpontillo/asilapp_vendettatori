package com.vendettatori.asilapp.db;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private final FirebaseFirestore db;

    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void setUser(String userId, @NonNull UserAnagrafica user, IHandlerDBUser handler) {
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
                        UserAnagrafica userData = document.toObject(UserAnagrafica.class);
                        handler.onRetrieveUser(userData);
                    } else {
                        handler.onErrorRetrieveUser(new RuntimeException("Accessing unknown user"));
                    }
                });
    }
}
