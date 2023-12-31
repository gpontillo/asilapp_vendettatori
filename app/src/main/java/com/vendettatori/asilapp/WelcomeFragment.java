package com.vendettatori.asilapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    NavController navController;
    public WelcomeFragment() {
    }

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
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
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = ((MainActivity) getActivity());
        activity.getSupportActionBar().hide();
        if(activity.currentUser != null) {
            if(activity.isUserLoggedComplete())
                navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
            else {
                Toast.makeText(getContext(), "We need you to complete your profile before logging in", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_welcomeFragment_to_registerDataFragment);
            }
        }
        else {
            navController.navigate(R.id.action_welcomeFragment_to_loginFragment);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}