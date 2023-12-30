package com.vendettatori.asilapp.user_pages;

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

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    NavController navController;

    public ProfileFragment() {
    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout emailInput = view.findViewById(R.id.emailLayoutProfile);
        TextInputLayout phoneInput = view.findViewById(R.id.phoneLayoutProfile);

        Button buttonLogout = view.findViewById(R.id.logoutProfile);

        String email = ((MainActivity) getActivity()).getUserAuth().getEmail();
        String phone = ((MainActivity) getActivity()).getUserData().getTelefono();

        emailInput.getEditText().setText(email);
        phoneInput.getEditText().setText(phone);

        buttonLogout.setOnClickListener(v -> ((MainActivity) getActivity()).logout());
    }
}