package trapleh.io.greenworld.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.Random;

import trapleh.io.greenworld.R;



public class PlantFragment extends Fragment {
    HorizontalBarChart skillRatingChart;
    private HorizontalBarChart horizontalChart;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_plant, container, false);

        horizontalChart = (HorizontalBarChart)view.findViewById(R.id.chart);
        BarDataSet barDataSet = new BarDataSet(getData(), "");
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.colorPrimary)});
        BarData barData = new BarData(barDataSet);
        XAxis xAxis = horizontalChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"Death", "Live"};
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(months);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        horizontalChart.setData(barData);
        horizontalChart.setFitBars(true);
        horizontalChart.animateXY(2000, 2000);
        horizontalChart.invalidate();
        horizontalChart.getAxisRight().setDrawGridLines(false);
        horizontalChart.getAxisLeft().setDrawGridLines(false);
        horizontalChart.getXAxis().setDrawGridLines(false);

        return view;
    }
    private ArrayList getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 40));//
        entries.add(new BarEntry(1f, 41));
        return entries;
    }








}
