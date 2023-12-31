package com.vendettatori.asilapp.auth;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class AuthFragment extends Fragment {
    ActionBar toolbar;

    @Override
    public void onStart() {
        super.onStart();
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(toolbar != null)
            toolbar.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(toolbar != null)
            toolbar.show();
    }
}
