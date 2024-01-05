package com.vendettatori.asilapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vendettatori.asilapp.db.FirebaseDatabase;
import com.vendettatori.asilapp.db.IHandlerDBUser;
import com.vendettatori.asilapp.db.UserAnagrafica;
import com.vendettatori.asilapp.db.UserAnagraficaFileManager;

import java.io.IOException;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authFireBase;
    private NavController navController;
    private FirebaseDatabase dbFireBase;

    public UserAnagrafica userData;
    public FirebaseUser currentUser;

    public int themeId = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;

    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authFireBase = FirebaseAuth.getInstance();
        dbFireBase = new FirebaseDatabase();
        if(savedInstanceState != null) {
            userData = savedInstanceState.getParcelable("userData");
            currentUser = authFireBase.getCurrentUser();
        }
        if(themeId == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            // Get selected theme mode
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
            themeId = sharedPref.getInt("themeId", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
            if(themeId == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
                themeId = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("themeId", themeId);
                editor.apply();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set selected theme mode
        UiModeManager uiModeManager = (UiModeManager) getBaseContext().getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setNightMode(themeId);
        AppCompatDelegate.setDefaultNightMode(themeId);

        //Retrive user
        currentUser = authFireBase.getCurrentUser();
        if(currentUser != null) {
            if(currentUser.isAnonymous()) {
                userData = new UserAnagrafica();
            }
            else {
                retriveUserData(currentUser.getUid());
            }
        }

        // Set toolbar and navigator
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("userData", userData);
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

    public boolean isUserLoggedComplete() {
        if(currentUser.isAnonymous()) {
            // Mock data in case of anonymous user
            if(userData == null)
                userData = new UserAnagrafica();
            return true;
        }
        else
            return userData != null;
    }

    public void loginFirebase(String email, String password, Callable<Void> onComplete) {
        authFireBase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = authFireBase.getCurrentUser();
                            Log.i("LOGIN", "signInWithEmail:success");

                            //Retrive data
                            UserAnagraficaFileManager fileManager = new UserAnagraficaFileManager(this, currentUser.getUid());
                            dbFireBase.getUser(currentUser.getUid(), new IHandlerDBUser() {
                                @Override
                                public void onSuccessSetUser() {}

                                @Override
                                public void onFailureSetUser(Exception e) {}

                                @Override
                                public void onRetrieveUser(UserAnagrafica userDataForm) {
                                    // Call onComplete callback
                                    try {
                                        onComplete.call();
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }

                                    if(userDataForm != null) {
                                        userData = userDataForm;

                                        // Save data on file
                                        try {
                                            fileManager.saveUserData(userDataForm);
                                        } catch (Exception e) {
                                            Log.e("MAIN", "Error on retrive user data", e);
                                        }

                                        Log.i("MAIN", "Received from DB: " + userData);
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
                                    // Call onComplete callback
                                    try {
                                        onComplete.call();
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }

                                    Log.e("MAIN", "Error on retriving user from DB", e);
                                    Toast.makeText(getBaseContext(), "An unexpected error accured", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Call onComplete callback
                            try {
                                onComplete.call();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

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
                        userData = new UserAnagrafica();

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

    public void registerUserData(UserAnagrafica userDataForm, Callable<Void> onComplete) {
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
            public void onRetrieveUser(UserAnagrafica userData) {}

            @Override
            public void onErrorRetrieveUser(Exception e) {}
        });
    }

    public void logout() {
        UserAnagraficaFileManager fileManager = new UserAnagraficaFileManager(this, currentUser.getUid());
        authFireBase.signOut();
        currentUser = null;
        userData = null;
        try {
            fileManager.deleteFile();
        } catch (Exception e) {
            Log.e("MAIN", "Error on delete user data", e);
        }
        navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                .setEnterAnim(android.R.animator.fade_in)
                .setExitAnim(android.R.animator.fade_out)
                .setPopUpTo(R.id.navigator_graph, true)
                .build()
        );
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
        UiModeManager uiModeManager = (UiModeManager) getBaseContext().getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setNightMode(themeId);
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("themeId", themeId);
        editor.apply();
        AppCompatDelegate.setDefaultNightMode(themeId);
    }

    private void retriveUserData(String id) {
        UserAnagraficaFileManager fileManager = new UserAnagraficaFileManager(this, id);
        UserAnagrafica userDataFile;
        try {
            userDataFile = fileManager.retriveUserData();
            if(userDataFile == null) {
                dbFireBase.getUser(currentUser.getUid(), new IHandlerDBUser() {
                    @Override
                    public void onSuccessSetUser() {}

                    @Override
                    public void onFailureSetUser(Exception e) {}

                    @Override
                    public void onRetrieveUser(UserAnagrafica userDataForm) {
                        if(userDataForm != null) {
                            try {
                                userData = userDataForm;
                                fileManager.saveUserData(userDataForm);
                            } catch (Exception e) {
                                Log.e("MAIN", "Error on save user data on file", e);
                            }
                            Log.i("MAIN", "Received from DB: " + userData);
                        }
                        else {
                            Log.w("MAIN", "No user found");
                        }
                    }

                    @Override
                    public void onErrorRetrieveUser(Exception e) {
                        Log.e("MAIN", "Error on retriving user from DB", e);
                    }
                });
            }
            else
                userData = userDataFile;
        } catch (Exception e) {
            Log.e("MAIN", "Error on retrive user data", e);
        }
    }

    public void saveNewUserData(UserAnagrafica newUserData, Callable<Void> onComplete) {
        UserAnagraficaFileManager fileManager = new UserAnagraficaFileManager(this, currentUser.getUid());
        dbFireBase.setUser(currentUser.getUid(), newUserData, new IHandlerDBUser() {
            @Override
            public void onSuccessSetUser() {
                try {
                    onComplete.call();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                Log.i("DB_USER", "Saved on DB: " + userData);
                userData = newUserData;

                try {
                    fileManager.saveUserData(newUserData);
                } catch (Exception e) {
                    Log.e("MAIN", "Error on retrive user data", e);
                }

                Toast.makeText(getBaseContext(), "Data updated",
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
            public void onRetrieveUser(UserAnagrafica userData) {}

            @Override
            public void onErrorRetrieveUser(Exception e) {}
        });
    }

    private void requestRunTimePermissions() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACTIVITY_RECOGNITION) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You have granted the Step-counter permissions", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACTIVITY_RECOGNITION)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("This app use permission to count your steps, please enable it if you want to want to know your steps")
                    .setTitle("Informative message")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("Ok", (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,
                                new String[] { android.Manifest.permission.ACTIVITY_RECOGNITION },
                                REQUEST_CODE);
                        dialog.dismiss();
                    });
            alert.show();
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.ACTIVITY_RECOGNITION },
                    REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You have granted the Step-counter permissions", Toast.LENGTH_SHORT).show();
        }  else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACTIVITY_RECOGNITION)){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("This app use permission to count your steps, please enable it if you want to want to know your steps")
                    .setTitle("Informative message")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("Ok", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        dialog.dismiss();
                    });

        } else {
            requestRunTimePermissions();
        }

    }
}
