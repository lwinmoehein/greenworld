package trapleh.io.greenworld.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.helper.MyValueFormatter;
import trapleh.io.greenworld.statics.Codes;
import trapleh.io.greenworld.ui.Plastuc_Upload_Dialog;


public class PlasticFragment extends Fragment {
    LinearLayout overall_f;
    RelativeLayout daily_f;
    PieChart chart;
    private HorizontalBarChart horizontalChart;
    public PlasticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plastic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton addPlastic=view.findViewById(R.id.add_plastic);
        TextView pl=view.findViewById(R.id.daily_PLastic_label);
        addPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Codes.pu=Codes.pu+1;
               pl.setText(Codes.pu+"");
              //  Plastuc_Upload_Dialog plastic_upload_dialog=new Plastuc_Upload_Dialog(getContext());
               // plastic_upload_dialog.show();
            }
        });
        TextView daily_btn,overall_btn;
        daily_btn=view.findViewById(R.id.daily_btn);
        overall_btn=view.findViewById(R.id.overall_btn);
        daily_f=view.findViewById(R.id.daily_frame);
        overall_f=view.findViewById(R.id.overall_frame);
        chart= view.findViewById(R.id.chart);

        pl.setText(Codes.pu+"");

        horizontalChart = (HorizontalBarChart)view.findViewById(R.id.chart1);
        BarDataSet barDataSet = new BarDataSet(getData(), "");
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setColors(new int[]{Color.GREEN,Color.YELLOW,Color.RED,Color.BLUE});
        BarData barData = new BarData(barDataSet);
        XAxis xAxis = horizontalChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"APR", "MAR","FEB","JAN"};
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

        forgraph(chart);

        overall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_btn.setBackgroundColor(getResources().getColor(R.color.white));
                daily_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                overall_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                overall_btn.setTextColor(getResources().getColor(R.color.white));
                daily_f.setVisibility(View.GONE);
                overall_f.setVisibility(View.VISIBLE);
            }
        });
        daily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overall_btn.setBackgroundColor(getResources().getColor(R.color.white));
                overall_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                daily_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                daily_btn.setTextColor(getResources().getColor(R.color.white));
                overall_f.setVisibility(View.GONE);
                daily_f.setVisibility(View.VISIBLE);
            }
        });

    }

    public void forgraph(PieChart chart){
        ArrayList<String> array_percent = new ArrayList<String>();
        array_percent.add("Sun");
        array_percent.add("Mon");
        array_percent.add("Thu");
        array_percent.add("Wed");
        array_percent.add("Tue");
        array_percent.add("Fri");
        array_percent.add("Sat");
        // array of graph drawing size according to design
        ArrayList<Integer> array_drawGraph = new ArrayList<>();
        array_drawGraph.add(20);
        array_drawGraph.add(10);
        array_drawGraph.add(10);
        array_drawGraph.add(20);
        array_drawGraph.add(10);
        array_drawGraph.add(10);
        array_drawGraph.add(20);
        // array of graph different colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.LTGRAY);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        // count is the number of values you need to display into graph
        for (int i = 0; i < 7; i++) {
            entries.add(new PieEntry(array_drawGraph.get(i),array_percent.get(i),null));
        }
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setValueFormatter(new MyValueFormatter());
        // set the data
        PieData datas = new PieData(dataset);        // initialize Piedata
        chart.setData(datas);
        // colors according to the dataset
        dataset.setColors(colors);
        // adding legends to the desigred positions
        Legend l = chart.getLegend();
        l.setTextSize(14f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.BLACK);
        l.setEnabled(true);
        // calling method to set center text
        chart.setCenterText("Weekly");
        // if no need to add description
        chart.getDescription().setEnabled(false);
        // animation and the center text color
        chart.animateY(500);
        chart.setEntryLabelColor(R.color.colorPrimary);



    }
    private ArrayList getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 90));//
        entries.add(new BarEntry(1f, 41));
        entries.add(new BarEntry(2f, 21));
        entries.add(new BarEntry(3f, 61));
        return entries;
    }
}
