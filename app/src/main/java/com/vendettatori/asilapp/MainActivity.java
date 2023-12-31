package com.vendettatori.asilapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vendettatori.asilapp.db.FirebaseDatabase;
import com.vendettatori.asilapp.db.IHandlerDBUser;
import com.vendettatori.asilapp.db.UserAnagrafici;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authFireBase;
    private FirebaseUser currentUser;
    private NavController navController;
    private FirebaseDatabase dbFireBase;
    private UserAnagrafici userData;

    private int themeId = 999999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authFireBase = FirebaseAuth.getInstance();
        dbFireBase = new FirebaseDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = authFireBase.getCurrentUser();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        AppCompatDelegate.setDefaultNightMode(getThemeId());
    }

    @Override
    //noinspection RestrictedApi
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
            navController.navigate(R.id.profileFragment, null, new NavOptions.Builder()
                    .setEnterAnim(android.R.animator.fade_in)
                    .setExitAnim(android.R.animator.fade_out)
                    .build()
            );
            return true;
        }
        else if(id == R.id.action_anagrafici) {
            navController.navigate(R.id.datiAnagraficiFragment, null, new NavOptions.Builder()
                    .setEnterAnim(android.R.animator.fade_in)
                    .setExitAnim(android.R.animator.fade_out)
                    .build()
            );
            return true;
        }
        else if(id == R.id.action_logout) {
            logout();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public boolean isUserLogged() {
        return currentUser != null;
    }

    public boolean isUserLoggedComplete() {
        if(currentUser.isAnonymous()) {
            // Mock data in case of anonymous user
            if(userData == null)
                userData = new UserAnagrafici();
            return true;
        }
        else
            return userData != null;
    }

    public UserAnagrafici getUserData() {
        if(currentUser == null) {
            currentUser = authFireBase.getCurrentUser();
        }
        if(userData == null) {
            dbFireBase.getUser(currentUser.getUid(), new IHandlerDBUser() {
                @Override
                public void onSuccessSetUser() {}

                @Override
                public void onFailureSetUser(Exception e) {}

                @Override
                public void onRetrieveUser(UserAnagrafici userDataForm) {
                    if(userDataForm != null) {
                        userData = userDataForm;
                        Log.i("DB_USER", "Received from DB: " + userData);
                    }
                    else {
                        if(currentUser != null && currentUser.isAnonymous()) {
                            // Fake data
                            userData = new UserAnagrafici();
                        }
                        Log.w("DB_USER", "No user found");
                    }
                }

                @Override
                public void onErrorRetrieveUser(Exception e) {
                    Log.e("MAIN", "Error on retriving user from DB", e);
                    Toast.makeText(getBaseContext(), "An unexpected error accured", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return userData;
    }

    public FirebaseUser getUserAuth() {
        if(currentUser == null) {
            currentUser = authFireBase.getCurrentUser();
        }
        return currentUser;
    }

    public void loginFirebase(String email, String password, Callable<Void> onComplete) {
        authFireBase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                        try {
                            onComplete.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = authFireBase.getCurrentUser();
                            Log.i("LOGIN", "signInWithEmail:success");

                            //Retrive data from DB of user
                            dbFireBase.getUser(currentUser.getUid(), new IHandlerDBUser() {
                                @Override
                                public void onSuccessSetUser() {}

                                @Override
                                public void onFailureSetUser(Exception e) {}

                                @Override
                                public void onRetrieveUser(UserAnagrafici userDataForm) {
                                    if(userDataForm != null) {
                                        userData = userDataForm;
                                        Log.i("DB_USER", "Received from DB: " + userData);
                                        Toast.makeText(getBaseContext(), "Logged", Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.action_loginFragment_to_homeFragment);
                                    }
                                    else {
                                        Log.w("MAIN", "No user found");
                                        Toast.makeText(getBaseContext(), "We need you to complete your profile before logging in", Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.action_loginFragment_to_registerDataFragment);
                                    }
                                }

                                @Override
                                public void onErrorRetrieveUser(Exception e) {
                                    Log.e("MAIN", "Error on retriving user from DB", e);
                                    Toast.makeText(getBaseContext(), "An unexpected error accured", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Error on login. Retry", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
    }

    public void loginGuest(Callable<Void> onComplete) {
        authFireBase.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    try {
                        onComplete.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        currentUser = authFireBase.getCurrentUser();
                        userData = new UserAnagrafici();
                        Log.i("LOGIN", "signInAnonymously:success");
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

    public void registerUser(String email, String password, Callable<Void> onComplete) {
        authFireBase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    try {
                        onComplete.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        currentUser = task.getResult().getUser();
                        Log.i("REGISTER", "createUserWithEmail:success");
                        navController.navigate(R.id.action_registerFragment_to_registerDataFragment);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getBaseContext(), "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void forgotFirebase(String email, Callable<Void> onComplete) {
        authFireBase.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    try {
                        onComplete.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (task.isSuccessful()) {
                        Log.i("FORGOT", "sendPasswordResetEmail:success");
                        Toast.makeText(getBaseContext(), "Email sent! Check your inbox to change the password!",
                                Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_forgotFragment_to_loginFragment);
                    }
                    else {
                        Log.w("FORGOT", "sendPasswordResetEmail:failure", task.getException());
                        Toast.makeText(getBaseContext(), "Error on sending the email. Check if the email is correct",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerUserData(UserAnagrafici userDataForm, Callable<Void> onComplete) {
        dbFireBase.setUser(currentUser.getUid(), userDataForm, new IHandlerDBUser() {
            @Override
            public void onSuccessSetUser() {
                try {
                    onComplete.call();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                Log.i("DB_USER", "Saved on DB: " + userData);
                userData = userDataForm;
                navController.navigate(R.id.action_registerDataFragment_to_homeFragment);
                Toast.makeText(getBaseContext(), "Registration success. Welcome!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailureSetUser(Exception e) {
                try {
                    onComplete.call();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                Log.e("DB_USER", "Error on saving data on DB", e);
            }

            @Override
            public void onRetrieveUser(UserAnagrafici userData) {}

            @Override
            public void onErrorRetrieveUser(Exception e) {}
        });
    }

    public void loadUserData(Callable<Void> onUserFound, Callable<Void> onUserNotFound) {
        dbFireBase.getUser(currentUser.getUid(), new IHandlerDBUser() {
            @Override
            public void onSuccessSetUser() {}

            @Override
            public void onFailureSetUser(Exception e) {}

            @Override
            public void onRetrieveUser(UserAnagrafici userDataDb) {
                if(userDataDb != null) {
                    userData = userDataDb;
                    Log.i("MAIN", "Success on fetch from DB");
                    try {
                        onUserFound.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Log.w("MAIN", "No user found");
                    try {
                        onUserNotFound.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onErrorRetrieveUser(Exception e) {
                Log.e("MAIN", "Error on fetch from DB", e);
                Toast.makeText(getBaseContext(), "An unexpected error accured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout() {
        authFireBase.signOut();
        currentUser = null;
        userData = null;
        navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                .setEnterAnim(android.R.animator.fade_in)
                .setExitAnim(android.R.animator.fade_out)
                .setPopUpTo(R.id.navigator_graph, true)
                .build()
        );
    }

    public int getThemeId() {
        if(themeId == 999999) {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
            themeId = sharedPref.getInt("themeId", 999999);
            if(themeId == 999999) {
                themeId = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("themeId", themeId);
                editor.apply();
            }
        }
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("themeId", themeId);
        editor.apply();
    }
}