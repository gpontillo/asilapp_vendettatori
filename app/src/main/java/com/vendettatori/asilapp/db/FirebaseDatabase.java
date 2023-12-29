package com.vendettatori.asilapp.db;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void setUser(String userId, @NonNull UserAnagrafici user, IHandlerDBUser handler) {
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(documentReference -> {
                    handler.onSuccessSetUser();
                })
                .addOnFailureListener(e -> {
                    handler.onFailureSetUser(e);
                });
    }

    public static void getUser(String userId, IHandlerDBUser handler) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        UserAnagrafici userData = document.toObject(UserAnagrafici.class);
                        handler.onRetrieveUser(userData);
                    } else {
                        throw new RuntimeException("Accessing unknown user");
                    }
                });
    }
}
