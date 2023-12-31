package com.vendettatori.asilapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Retriving CardView elements from layout by id
        CardView anagraficaCard = (CardView) getView().findViewById(R.id.AnagraficiCard);
        CardView parametriCard = (CardView) getView().findViewById(R.id.ParametriMediciCard);
        CardView infoCard = (CardView) getView().findViewById(R.id.InfoCard);
        CardView gestioneSpeseCard = (CardView) getView().findViewById(R.id.GestioneSpeseCard);
        CardView gestioneDatiCard = (CardView) getView().findViewById(R.id.DatiCard);

        //Using Listener to set Navigation to each CardView
        anagraficaCard.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_datiAnagraficiFragment));
        parametriCard.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_parametri_medici_nav_graph));
        infoCard.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_informazioni_nav_graph));
        gestioneSpeseCard.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_gestione_spese_nav_graph));
        gestioneDatiCard.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_gestione_dati_nav_graph));

    }
}