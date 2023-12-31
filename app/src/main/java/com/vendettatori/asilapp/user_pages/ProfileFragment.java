package com.vendettatori.asilapp.user_pages;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.utils.InputUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    NavController navController;
    TextInputLayout themeInput;

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
        themeInput = view.findViewById(R.id.themeLayoutProfile);

        Button buttonLogout = view.findViewById(R.id.logoutProfile);

        MainActivity activity = ((MainActivity) getActivity());

        String email = "";
        String phone = "";
        if(activity.currentUser != null)
            email = activity.currentUser.getEmail();
        if(activity.userData != null)
            phone = activity.userData .getTelefono();

        emailInput.getEditText().setInputType(InputType.TYPE_NULL);
        if(email == null || (activity.currentUser != null && activity.currentUser.isAnonymous())) {
            emailInput.getEditText().setText("user@guest.com");
        }
        else {
            emailInput.getEditText().setText(email);
        }

        phoneInput.getEditText().setInputType(InputType.TYPE_NULL);
        phoneInput.getEditText().setText(phone);

        themeInput.getEditText().setInputType(InputType.TYPE_NULL);
        themeInput.setStartIconOnClickListener(v -> selectTheme(v));
        themeInput.getEditText().setOnClickListener(v -> selectTheme(v));
        themeInput.getEditText().setText(InputUtils.fromIdtoStringTheme((activity.themeId)));

        buttonLogout.setOnClickListener(v -> ((MainActivity) getActivity()).logout());
    }

    public void selectTheme(View v){
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.theme_selection, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> onThemeSelected(item));
        popup.show();
    }

    private boolean onThemeSelected(MenuItem item) {
        themeInput.getEditText().setText(item.getTitle());
        String themeString = (String) item.getTitle();
        int idTheme = InputUtils.fromStringtoIdTheme(themeString);
        ((MainActivity) getActivity()).setThemeId(idTheme);
        return true;
    }
}