package com.vendettatori.asilapp.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.utils.InputUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends AuthFragment {
    NavController navController;
    boolean loading = false;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    TextInputLayout passwordConfInput;
    Button buttonConfirm;
    ProgressBar loader;

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

        buttonConfirm = view.findViewById(R.id.logoutProfile);

        loader = view.findViewById(R.id.progressBarRegister);

        emailInput = view.findViewById(R.id.emailLayoutRegister);
        passwordInput = view.findViewById(R.id.passwordLayoutRegister);
        passwordConfInput = view.findViewById(R.id.passwordConfLayoutRegister);

        buttonConfirm.setOnClickListener(v -> {
            if(validateForm()) {
                String email = emailInput.getEditText().getText().toString();
                String password = passwordInput.getEditText().getText().toString();
                onConfirmRegister(email, password);
            }
        });

        emailInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

        passwordConfInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordConfInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public boolean validateForm() {
        if(emailInput != null && passwordInput != null && passwordConfInput != null) {
            String email = emailInput.getEditText().getText().toString();
            String password = passwordInput.getEditText().getText().toString();
            String passwordConf = passwordConfInput.getEditText().getText().toString();

            if(!InputUtils.isValidEmail(email))
                emailInput.setError("Invalid email");
            if(TextUtils.isEmpty(password))
                passwordInput.setError("Password is required");
            else if(password.length() < 6)
                passwordInput.setError("Password must be 6 characters");
            if(TextUtils.isEmpty(passwordConf) || !passwordConf.equals(password))
                passwordConfInput.setError("Passwords must be the same");

            return emailInput.getError() == null && passwordInput.getError() == null && passwordConfInput.getError() == null;
        }
        return false;
    }

    public void onConfirmRegister(String email, String password) {
        toggleLoading();
        ((MainActivity) getActivity()).registerUser(email, password, () -> {
            toggleLoading();
            return null;
        });
    }

    public void toggleLoading() {
        loading = !loading;
        if(loading) {
            buttonConfirm.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.VISIBLE);
        }
        else {
            buttonConfirm.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}