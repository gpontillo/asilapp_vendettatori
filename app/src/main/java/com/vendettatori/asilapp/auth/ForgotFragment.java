package com.vendettatori.asilapp.auth;

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

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotFragment extends Fragment {
    NavController navController;
    boolean loading = false;
    TextInputLayout emailInput;
    Button buttonConfirm;
    ProgressBar loader;

    public ForgotFragment() {
    }
    public static ForgotFragment newInstance() {
        ForgotFragment fragment = new ForgotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonConfirm = view.findViewById(R.id.BtnRecoverPassword);
        loader = view.findViewById(R.id.progressBarForgot);

        emailInput = view.findViewById(R.id.emailLayoutForgot);

        buttonConfirm.setOnClickListener(v -> {
            if(validateForm()) {
                String email = emailInput.getEditText().getText().toString();
                onForgot(email);
            }
        });

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
    }

    public void onForgot(String email) {
        toggleLoading();
        ((MainActivity) getActivity()).forgotFirebase(email, () -> {
            toggleLoading();
            return null;
        });
    }

    public boolean validateForm() {
        if(emailInput != null) {
            String email = emailInput.getEditText().getText().toString();

            if(TextUtils.isEmpty(email))
                emailInput.setError("Email is required");

            return emailInput.getError() == null;
        }
        return false;
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

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}