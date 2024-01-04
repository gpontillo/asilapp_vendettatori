package com.vendettatori.asilapp.utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockGraphFactory {

    public static void createMockGraph(LineChart lineChart) {
        ArrayList<Entry> seriesData = new ArrayList<>();

        Random r = new Random();
        for(int i = 1; i < 11; i++)
            seriesData.add(new Entry(i, r.nextFloat() + 1));

        LineDataSet lineDataSet = new LineDataSet(seriesData, "Data");
        lineChart.setData(new LineData(lineDataSet));
    }
}
