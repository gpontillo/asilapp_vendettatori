package com.vendettatori.asilapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
    }

    public boolean isUserLogged() {
        return currentUser != null;
    }

    public void loginFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = mAuth.getCurrentUser();
                            Log.w("LOGIN", "signInWithEmail:success");
                            Toast.makeText(getBaseContext(), "Logged", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_loginFragment_to_homeFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Not logged", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
    }

    public void loginGuest() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        currentUser = mAuth.getCurrentUser();
                        Log.d("LOGIN", "signInAnonymously:success");
                        Toast.makeText(getBaseContext(), "Authenticated as guest.",
                                Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_loginFragment_to_homeFragment);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LOGIN", "signInAnonymously:failure", task.getException());
                        Toast.makeText(getBaseContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("REGISTER", "createUserWithEmail:success");
                        Toast.makeText(getBaseContext(), "Registered! Login to access the app.",
                                Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getBaseContext(), "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}