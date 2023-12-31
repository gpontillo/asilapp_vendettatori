package com.vendettatori.asilapp.user_pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.db.UserAnagrafica;
import com.vendettatori.asilapp.utils.InputUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatiAnagraficiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatiAnagraficiFragment extends Fragment {
    NavController navController;

    public DatiAnagraficiFragment() {
        // Required empty public constructor
    }

    public static DatiAnagraficiFragment newInstance() {
        DatiAnagraficiFragment fragment = new DatiAnagraficiFragment();
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
        return inflater.inflate(R.layout.fragment_dati_anagrafici, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout cognomeInput = view.findViewById(R.id.cognomeLayoutAnagrafica);
        TextInputLayout nomeInput = view.findViewById(R.id.nomeLayoutAnagrafica);
        TextInputLayout dataNascitaInput = view.findViewById(R.id.dataLayoutAnagrafica);
        TextInputLayout luogoNascitaInput = view.findViewById(R.id.luogoLayoutAnagrafica);
        TextInputLayout indirizzoInput = view.findViewById(R.id.indirizzoLayoutAnagrafica);

        cognomeInput.getEditText().setInputType(InputType.TYPE_NULL);
        nomeInput.getEditText().setInputType(InputType.TYPE_NULL);
        dataNascitaInput.getEditText().setInputType(InputType.TYPE_NULL);
        luogoNascitaInput.getEditText().setInputType(InputType.TYPE_NULL);
        indirizzoInput.getEditText().setInputType(InputType.TYPE_NULL);

        MainActivity activity = ((MainActivity) getActivity());
        UserAnagrafica userData = activity.userData;
        if(userData != null) {
            String cognome = userData.getCognome();
            String nome = userData.getNome();
            String dataNascita = InputUtils.formatStringFromTimestamp(userData.getDataNascita());
            String luogoNascita = userData.getLuogoNascita();
            String indirizzo = userData.getIndirizzo();

            cognomeInput.getEditText().setText(cognome);
            nomeInput.getEditText().setText(nome);
            dataNascitaInput.getEditText().setText(dataNascita);
            luogoNascitaInput.getEditText().setText(luogoNascita);
            indirizzoInput.getEditText().setText(indirizzo);
        }
    }
}