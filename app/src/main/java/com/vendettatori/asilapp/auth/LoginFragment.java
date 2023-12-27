package com.vendettatori.asilapp.auth;

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

import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.utils.InputUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    NavController navController;
    EditText emailInput;
    EditText passwordInput;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
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

        emailInput = view.findViewById(R.id.emailLogin);
        passwordInput = view.findViewById(R.id.passwordLogin);

        buttonLogin.setOnClickListener(v -> {
            if(validateForm()) {
                onLogin(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });

        buttonRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));

        textGuest.setOnClickListener(v -> onLoginAsGuest());

    }

    public boolean validateForm() {
        if(emailInput != null && passwordInput != null) {
            if(emailInput.getText().toString().equals(""))
                emailInput.setError("Email can't be empty");
            else if(passwordInput.getText().toString().equals(""))
                passwordInput.setError("Password can't be empty");
            else
                return true;
        }
        return false;
    }

    public void onLogin(String email, String password) {
        ((MainActivity) getActivity()).loginFirebase(email, password);
    }

    public void onLoginAsGuest() {
        ((MainActivity) getActivity()).loginGuest();
    }
}