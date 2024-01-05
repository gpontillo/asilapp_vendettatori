package com.vendettatori.asilapp.param_medici;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.utils.GraphFactory;

public class ParametriMediciFragment extends Fragment implements SensorEventListener {

    LineChart contapassiChart;
    LineChart pressioneChart;
    LineChart freqCardiacaChart;
    LineChart temperaturaChart;
    LineChart glicemiaChart;

    //Step-counter sensor variables
    private SensorManager sensorManager = null;
    private Sensor stepCounter = null;
    private int steps = 0;

    public ParametriMediciFragment() {
    }

    public static ParametriMediciFragment newInstance() {
        ParametriMediciFragment fragment = new ParametriMediciFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
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

        GraphFactory.createMockGraph(contapassiChart, 100, 1000, "Steps/m");
        GraphFactory.createMockGraph(pressioneChart, 80, 90, "bar/m");
        GraphFactory.createMockGraph(freqCardiacaChart, 60, 100, "battiti/m");
        GraphFactory.createMockGraph(temperaturaChart, 35, 37, "C°/m");
        GraphFactory.createMockGraph(glicemiaChart, 70, 80, "(mg * m)/dl");

        pesoInput.getEditText().setInputType(InputType.TYPE_NULL);
        altezzaInput.getEditText().setInputType(InputType.TYPE_NULL);

        pesoInput.getEditText().setText("70");
        altezzaInput.getEditText().setText("180");
    }

    //Methods relative to Step-counter sensor
    public void onResume()  {
        super.onResume();
        if(stepCounter != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps = (int) event.values[0]; // Valore da far tornare al grafico
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}