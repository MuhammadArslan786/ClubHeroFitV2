package com.arslan6015.clubherofitv2.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arslan6015.clubherofitv2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class MovFragment extends Fragment {
    BarChart MOVbarChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mov, container, false);
        MOVbarChart = root.findViewById(R.id.MOVbarChart);

        getEntries();

        barDataSet = new BarDataSet(barEntries, "Hp Data Set");
        barData = new BarData(barDataSet);
        MOVbarChart.setData(barData);
        barDataSet.setColors(getResources().getColor(R.color.TextViewColor));
        barDataSet.setValueTextColor(getResources().getColor(R.color.EditTextColor));
        barDataSet.setBarBorderColor(getResources().getColor(R.color.EditTextColor));

        barDataSet.setValueTextSize(16f);

        return root;
    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 7));
        barEntries.add(new BarEntry(2f, 4));
        barEntries.add(new BarEntry(3f, 1));
        barEntries.add(new BarEntry(5f, 3));
        barEntries.add(new BarEntry(6f, 4));
        barEntries.add(new BarEntry(7f, 5));

    }

}