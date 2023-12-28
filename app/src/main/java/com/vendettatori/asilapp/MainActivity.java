package com.vendettatori.asilapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Callable;

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
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile) {
            navController.navigate(R.id.profile_nav_graph, null, new NavOptions.Builder()
                    .setEnterAnim(android.R.animator.fade_in)
                    .setExitAnim(android.R.animator.fade_out)
                    .build()
            );
            return true;
        }
        else if(id == R.id.action_logout) {
            mAuth.signOut();
            currentUser = null;
            navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                    .setEnterAnim(android.R.animator.fade_in)
                    .setExitAnim(android.R.animator.fade_out)
                    .setPopUpTo(R.id.navigator_graph, true)
                    .build()
            );
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public boolean isUserLogged() {
        return currentUser != null;
    }

    public void loginFirebase(String email, String password, Callable<Void> methodParam) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                        try {
                            methodParam.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
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

    public void loginGuest(Callable<Void> methodParam) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    try {
                        methodParam.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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

    public void registerUser(String email, String password, Callable<Void> methodParam) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    try {
                        methodParam.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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