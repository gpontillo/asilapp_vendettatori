package com.vendettatori.asilapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private EditText emailEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_forgot);

        emailEditText = findViewById(R.id.editbox);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void callforgetpassword(View view) {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Inserisci l'indirizzo email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Utilizza FirebaseAuth per inviare l'email di recupero
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // L'email di recupero è stata inviata con successo
                            Toast.makeText(PasswordRecoveryActivity.this,
                                    "Email di recupero inviata con successo", Toast.LENGTH_SHORT).show();
                        } else {
                            // Si è verificato un errore nell'invio dell'email di recupero
                            Toast.makeText(PasswordRecoveryActivity.this,
                                    "Errore nell'invio dell'email di recupero", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
