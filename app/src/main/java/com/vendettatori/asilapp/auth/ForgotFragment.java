package com.vendettatori.asilapp.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
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