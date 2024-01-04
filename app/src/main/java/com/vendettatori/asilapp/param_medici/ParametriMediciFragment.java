package com.vendettatori.asilapp.param_medici;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.utils.MockGraphFactory;

import java.util.ArrayList;
import java.util.List;

public class ParametriMediciFragment extends Fragment {

    LineChart contapassiChart;
    LineChart pressioneChart;
    LineChart freqCardiacaChart;
    LineChart temperaturaChart;
    LineChart glicemiaChart;

    public ParametriMediciFragment() {
    }

    public static ParametriMediciFragment newInstance() {
        ParametriMediciFragment fragment = new ParametriMediciFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parametri_medici, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout pesoInput = view.findViewById(R.id.pesoLayout);
        TextInputLayout altezzaInput = view.findViewById(R.id.altezzaLayout);

        contapassiChart = view.findViewById(R.id.contapassiChartView);
        pressioneChart = view.findViewById(R.id.pressioneChartView);
        freqCardiacaChart = view.findViewById(R.id.freqCardiacaChartView);
        temperaturaChart = view.findViewById(R.id.temperaturaChartView);
        glicemiaChart = view.findViewById(R.id.glicemiaChartView);

        MockGraphFactory.createMockGraph(contapassiChart);
        MockGraphFactory.createMockGraph(pressioneChart);
        MockGraphFactory.createMockGraph(freqCardiacaChart);
        MockGraphFactory.createMockGraph(temperaturaChart);
        MockGraphFactory.createMockGraph(glicemiaChart);

        pesoInput.getEditText().setInputType(InputType.TYPE_NULL);
        altezzaInput.getEditText().setInputType(InputType.TYPE_NULL);

        pesoInput.getEditText().setText("70");
        altezzaInput.getEditText().setText("180");
    }
}