package com.vendettatori.asilapp.auth;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends AuthFragment {
    NavController navController;
    boolean loading = false;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    Button buttonLogin;
    Button buttonRegister;
    TextView textGuest;
    TextView textForgot;
    ProgressBar loader;

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
        buttonLogin = view.findViewById(R.id.confloginButtonLogin);
        buttonRegister = view.findViewById(R.id.registerButtonLogin);
        textGuest = view.findViewById(R.id.guestText);
        textForgot = view.findViewById(R.id.forgotText);

        loader = view.findViewById(R.id.progressBarLogin);

        emailInput = view.findViewById(R.id.emailLayoutLogin);
        passwordInput = view.findViewById(R.id.passwordLayoutLogin);

        buttonLogin.setOnClickListener(v -> {
            if(validateForm()) {
                String email = emailInput.getEditText().getText().toString();
                String password = passwordInput.getEditText().getText().toString();
                onLogin(email, password);
            }
        });

        buttonRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));

        textGuest.setOnClickListener(v -> onLoginAsGuest());
        textForgot.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_forgotFragment));

        emailInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public boolean validateForm() {
        if(emailInput != null && passwordInput != null) {
            String email = emailInput.getEditText().getText().toString();
            String password = passwordInput.getEditText().getText().toString();

            if(TextUtils.isEmpty(email))
                emailInput.setError("Email is required");
            if(TextUtils.isEmpty(password))
                passwordInput.setError("Password is required");

            return emailInput.getError() == null && passwordInput.getError() == null;
        }
        return false;
    }

    public void onLogin(String email, String password) {
        toggleLoading();
        ((MainActivity) getActivity()).loginFirebase(email, password, () -> {
            toggleLoading();
            return null;
        });
    }

    public void onLoginAsGuest() {
        toggleLoading();
        ((MainActivity) getActivity()).loginGuest(() -> {
            toggleLoading();
            return null;
        });
    }

    public void toggleLoading() {
        loading = !loading;
        if(loading) {
            buttonLogin.setVisibility(View.INVISIBLE);
            buttonRegister.setVisibility(View.INVISIBLE);
            textGuest.setVisibility(View.INVISIBLE);
            textForgot.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.VISIBLE);
        }
        else {
            buttonLogin.setVisibility(View.VISIBLE);
            buttonRegister.setVisibility(View.VISIBLE);
            textGuest.setVisibility(View.VISIBLE);
            textForgot.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}
