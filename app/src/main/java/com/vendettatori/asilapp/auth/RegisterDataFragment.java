package com.vendettatori.asilapp.auth;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.db.UserAnagrafici;
import com.vendettatori.asilapp.utils.InputUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterDataFragment extends Fragment {

    NavController navController;
    boolean loading = false;
    TextInputLayout cognomeInput;
    TextInputLayout nomeInput;
    TextInputLayout telefonoInput;
    TextInputLayout dataNascitaInput;
    TextInputLayout luogoNascitaInput;
    TextInputLayout indirizzoInput;
    Button buttonConfirm;
    ProgressBar loader;

    public RegisterDataFragment() {
        // Required empty public constructor
    }

    public static RegisterDataFragment newInstance() {
        return new RegisterDataFragment();
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
        return inflater.inflate(R.layout.fragment_register_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        buttonConfirm = view.findViewById(R.id.confButtonRegisterData);

        loader = view.findViewById(R.id.progressBarRegisterData);

        cognomeInput = view.findViewById(R.id.cognomeLayoutAnagrafica);
        nomeInput = view.findViewById(R.id.nomeLayoutAnagrafica);
        telefonoInput = view.findViewById(R.id.telefonoLayoutAnagrafica);
        dataNascitaInput = view.findViewById(R.id.dataLayoutAnagrafica);
        luogoNascitaInput = view.findViewById(R.id.luogoLayoutAnagrafica);
        indirizzoInput = view.findViewById(R.id.indirizzoLayoutAnagrafica);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
            LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
            String formattedDate = localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
            dataNascitaInput.getEditText().setText(formattedDate);
        }, 1990, 1, 1);

        dataNascitaInput.setStartIconOnClickListener(v -> datePickerDialog.show());
        dataNascitaInput.getEditText().setOnClickListener(v -> datePickerDialog.show());
        dataNascitaInput.getEditText().setInputType(InputType.TYPE_NULL);

        buttonConfirm.setOnClickListener(v -> {
            if(validateForm()) {
                String cognome = cognomeInput.getEditText().getText().toString();
                String nome = nomeInput.getEditText().getText().toString();
                String telefono = telefonoInput.getEditText().getText().toString();

                String dataString = dataNascitaInput.getEditText().getText().toString();
                Timestamp dataNascita = InputUtils.formatTimestampFromString(dataString);

                String luogoNascita = luogoNascitaInput.getEditText().getText().toString();
                String indirizzo = indirizzoInput.getEditText().getText().toString();
                onConfirmRegisterData(cognome, nome, telefono, dataNascita, luogoNascita, indirizzo);
            }
        });

        cognomeInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cognomeInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        nomeInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomeInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        telefonoInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 10)
                    telefonoInput.setError("Phone number too long");
                else
                    telefonoInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dataNascitaInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataNascitaInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        luogoNascitaInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                luogoNascitaInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        indirizzoInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                indirizzoInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onConfirmRegisterData(String cognome, String nome, String telefono, Timestamp dataNascita, String luogoNascita, String indirizzo) {
        toggleLoading();
        UserAnagrafici userDataForm = new UserAnagrafici(nome, cognome, telefono, dataNascita, luogoNascita, indirizzo);
        ((MainActivity) getActivity()).registerUserData(userDataForm, () -> {
            toggleLoading();
            return null;
        });
    }

    public boolean validateForm() {
        if(cognomeInput != null && nomeInput != null && telefonoInput != null && dataNascitaInput != null && luogoNascitaInput != null && indirizzoInput != null) {
            String cognome = cognomeInput.getEditText().getText().toString();
            String nome = nomeInput.getEditText().getText().toString();
            String telefono = telefonoInput.getEditText().getText().toString();
            String dataNascita = dataNascitaInput.getEditText().getText().toString();
            String luogoNascita = luogoNascitaInput.getEditText().getText().toString();
            String indirizzo = indirizzoInput.getEditText().getText().toString();

            if(TextUtils.isEmpty(cognome))
                cognomeInput.setError("Surname is required");
            if(TextUtils.isEmpty(nome))
                nomeInput.setError("Name is required");
            if(!InputUtils.isValidPhone(telefono))
                telefonoInput.setError("Invalid phone number");
            if(TextUtils.isEmpty(dataNascita))
                telefonoInput.setError("Date of birth is required");
            if(TextUtils.isEmpty(luogoNascita))
                telefonoInput.setError("Birth place is required");
            if(TextUtils.isEmpty(indirizzo))
                telefonoInput.setError("Residence address is required");

            return cognomeInput.getError() == null && nomeInput.getError() == null && telefonoInput.getError() == null && dataNascitaInput.getError() == null && luogoNascitaInput.getError() == null && indirizzoInput.getError() == null;
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