package com.vendettatori.asilapp.utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.vendettatori.asilapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphFactory {

    public static void createMockGraph(LineChart lineChart, int min, int max, String description) {
        lineChart.getDescription().setText(description);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);

        List<Entry> seriesData = new ArrayList<>();
        Random r = new Random();
        for(int i = 1; i < 11; i++)
            seriesData.add(new Entry(i, Math.round(min + r.nextFloat() * (max - min))));

        LineDataSet lineDataSet = new LineDataSet(seriesData, "dataMock");
        lineDataSet.setCircleColor(R.color.md_theme_tertiary);
        lineDataSet.setColor(R.color.md_theme_tertiaryContainer);
        lineDataSet.setDrawCircleHole(false);
        lineChart.setData(new LineData(lineDataSet));

        lineChart.invalidate();
    }
}
