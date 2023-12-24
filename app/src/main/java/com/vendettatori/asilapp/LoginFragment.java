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
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;

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
        TextView textGuest = view.findViewById(R.id.guestText);

        EditText emailInput = view.findViewById(R.id.emailLogin);
        EditText passwordInput = view.findViewById(R.id.passwordLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailInput.getText().toString() != "" && passwordInput.getText().toString() != "") {
                    onLogin(emailInput.getText().toString(), passwordInput.getText().toString());
                }
                else {
                    Toast.makeText(getContext(), "Fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        textGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginAsGuest();
            }
        });

    }

    public void onLogin(String email, String password) {
        ((MainActivity) getActivity()).loginFirebase(email, password);
    }

    public void onLoginAsGuest() {
        ((MainActivity) getActivity()).loginGuest();
    }
}