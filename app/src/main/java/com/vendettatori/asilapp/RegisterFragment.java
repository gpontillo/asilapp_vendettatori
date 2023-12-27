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
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    NavController navController;
    EditText emailInput;
    EditText passwordInput;
    EditText passwordConfInput;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonConfirm = view.findViewById(R.id.confButtonRegister);

        emailInput = view.findViewById(R.id.emailRegister);
        passwordInput = view.findViewById(R.id.passwordRegister);
        passwordConfInput = view.findViewById(R.id.passwordConfRegister);

        buttonConfirm.setOnClickListener(v -> {
            if(validateForm()) {
                onConfirmRegister(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });
    }

    public boolean validateForm() {
        if(emailInput != null && passwordInput != null && passwordConfInput != null) {
            if(emailInput.getText().toString().equals(""))
                emailInput.setError("Email can't be empty");
            else if(passwordInput.getText().toString().equals(""))
                passwordInput.setError("Password can't be empty");
            else if(passwordConfInput.getText().toString().equals(""))
                passwordConfInput.setError("Confirm password can't be empty");
            else if(!passwordInput.getText().toString().equals(passwordConfInput.getText().toString()))
                passwordConfInput.setError("The password must be the same");
            else
                return true;
        }
        return false;
    }

    public void onConfirmRegister(String email, String password) {
        Toast.makeText(getContext(), "Registered with " + email + " and password " + password, Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.action_registerFragment_to_loginFragment);
    }
}