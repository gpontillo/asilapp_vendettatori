package com.vendettatori.asilapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonLogin = view.findViewById(R.id.confloginButtonLogin);
        Button buttonRegister = view.findViewById(R.id.registerButtonLogin);

        EditText emailInput = view.findViewById(R.id.emailLogin);
        EditText passwordInput = view.findViewById(R.id.passwordLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });

    }

    public void onLogin(String email, String password) {
        Toast.makeText(this.getContext(),"Logged with \"" + email + "\" and password \"" + password + "\"", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.action_loginFragment_to_homeFragment);
    }

    public void onRegister() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }
}