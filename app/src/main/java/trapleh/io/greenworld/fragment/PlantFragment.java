package trapleh.io.greenworld.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;


import java.util.ArrayList;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.adapter.PlantAdapter;
import trapleh.io.greenworld.model.Plant;


public class PlantFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_plant, container, false);
        RoundCornerProgressBar progress_live = (RoundCornerProgressBar) view.findViewById(R.id.progress_1);
        progress_live.setMax(100);
        progress_live.setProgress(75);

        RoundCornerProgressBar progress_death = (RoundCornerProgressBar) view.findViewById(R.id.progress_death);
        progress_death.setMax(100);
        progress_death.setProgress(25);

        final Button btn_live=view.findViewById(R.id.btn_live);
        final Button btn_death=view.findViewById(R.id.btn_death);
        final RecyclerView rv_live=view.findViewById(R.id.rv_live);
        final RecyclerView rv_death=view.findViewById(R.id.rv_death);
        ArrayList<Plant> ary_live=new ArrayList<>();
        Plant p=new Plant("dfd","Rose","No ( 10 ),Pakokku","true","12-1-2020");
        ary_live.add(p);
        ary_live.add(p);
        ary_live.add(p);
        ary_live.add(p);
        ary_live.add(p);
        ary_live.add(p);
        PlantAdapter plantAdapter_forlive=new PlantAdapter(ary_live);
        rv_live.setAdapter(plantAdapter_forlive);
        rv_live.setLayoutManager(new LinearLayoutManager(getContext()));



        ArrayList<Plant> ary_death=new ArrayList<>();
        Plant v=new Plant("dfd","Orchid","No ( 10 ),Pakokku","false","12-1-2020");
        ary_death.add(v);
        ary_death.add(v);
        PlantAdapter plantAdapter_fordeath=new PlantAdapter(ary_death);
        rv_death.setAdapter(plantAdapter_fordeath);
        rv_death.setLayoutManager(new LinearLayoutManager(getContext()));


        btn_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_live.setBackgroundResource(R.drawable.live_design_click);
                btn_death.setBackgroundResource(R.drawable.death_design_enable);
                btn_death.setTextColor(getResources().getColor(R.color.colorPrimary) );
                btn_live.setTextColor(getResources().getColor(R.color.white) );
                rv_live.setVisibility(View.VISIBLE);
                rv_death.setVisibility(View.GONE);
            }
        });

        btn_death.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_live.setBackgroundResource(R.drawable.live_design_enable);
                btn_death.setBackgroundResource(R.drawable.death_design_click);
                btn_death.setTextColor(getResources().getColor(R.color.white) );
                btn_live.setTextColor(getResources().getColor(R.color.colorPrimary) );
                rv_live.setVisibility(View.GONE);
                rv_death.setVisibility(View.VISIBLE);

            }
        });



        return view;
    }









}
