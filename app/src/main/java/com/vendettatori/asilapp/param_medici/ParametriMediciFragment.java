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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.textfield.TextInputLayout;
import com.vendettatori.asilapp.MainActivity;
import com.vendettatori.asilapp.R;
import com.vendettatori.asilapp.db.UserAnagrafica;
import com.vendettatori.asilapp.utils.GraphFactory;
import com.vendettatori.asilapp.utils.InputUtils;

public class ParametriMediciFragment extends Fragment implements SensorEventListener {

    TextInputLayout pesoInput;
    TextInputLayout altezzaInput;
    LineChart contapassiChart;
    LineChart pressioneChart;
    LineChart freqCardiacaChart;
    LineChart temperaturaChart;
    LineChart glicemiaChart;
    Button buttonConfirmChanges;
    ProgressBar loaderDatiGenerali;
    boolean loading = false;

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

        pesoInput = view.findViewById(R.id.pesoLayout);
        altezzaInput = view.findViewById(R.id.altezzaLayout);
        buttonConfirmChanges = view.findViewById(R.id.confChangesButton);
        loaderDatiGenerali = view.findViewById(R.id.progressBarDatiGenerali);

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

        float peso = ((MainActivity) getActivity()).userData.getPeso();
        float altezza = ((MainActivity) getActivity()).userData.getAltezza();

        pesoInput.getEditText().setText(peso == -1 ? null : Float.toString(peso));
        altezzaInput.getEditText().setText(altezza == -1 ? null : Float.toString(altezza));

        MainActivity activity = (MainActivity) getActivity();

        buttonConfirmChanges.setOnClickListener(v -> {
            if(validateForm()) {
                float pesoFromInput = InputUtils.parseStringInputFloat(pesoInput.getEditText().getText().toString());
                float altezzaFromInput = InputUtils.parseStringInputFloat(altezzaInput.getEditText().getText().toString());

                UserAnagrafica newUserData = activity.userData.cloneUser();
                newUserData.setPeso(pesoFromInput);
                newUserData.setAltezza(altezzaFromInput);

                toggleLoading();
                activity.saveNewUserData(newUserData, () -> {
                    toggleLoading();
                    return null;
                });
            }
        });

        pesoInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesoInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        altezzaInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                altezzaInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public boolean validateForm() {
        if(pesoInput != null && altezzaInput != null) {
            String pesoStr = pesoInput.getEditText().getText().toString();
            String altezzaStr = altezzaInput.getEditText().getText().toString();

            if(!InputUtils.isValidNumber(pesoStr))
                pesoInput.setError("Weight is invalid");
            if(!InputUtils.isValidNumber(altezzaStr))
                altezzaInput.setError("Height is invalid");

            return pesoInput.getError() == null && altezzaInput.getError() == null;
        }
        return false;
    }

    public void toggleLoading() {
        loading = !loading;
        if(loading) {
            pesoInput.setVisibility(View.GONE);
            altezzaInput.setVisibility(View.GONE);
            buttonConfirmChanges.setVisibility(View.GONE);
            loaderDatiGenerali.setVisibility(View.VISIBLE);
        }
        else {
            pesoInput.setVisibility(View.VISIBLE);
            altezzaInput.setVisibility(View.VISIBLE);
            buttonConfirmChanges.setVisibility(View.VISIBLE);
            loaderDatiGenerali.setVisibility(View.GONE);
        }
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