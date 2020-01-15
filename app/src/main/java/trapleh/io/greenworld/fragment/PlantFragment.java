package trapleh.io.greenworld.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.adapter.PlantAdapter;
import trapleh.io.greenworld.model.Plant;
import trapleh.io.greenworld.statics.UserStatic;
import trapleh.io.greenworld.ui.Plant_Upload_Dialog;


public class PlantFragment extends Fragment {
    DatabaseReference liveplantReference= FirebaseDatabase.getInstance().getReference().child("Plants").child(UserStatic.currentUser.getUid()).child("LIVE_PLANT");
    DatabaseReference deathplantReference= FirebaseDatabase.getInstance().getReference().child("Plants").child(UserStatic.currentUser.getUid()).child("DEATH_PLANT");

    ArrayList<Plant> ary_live=new ArrayList<>();
    ArrayList<String> id_live=new ArrayList<>();

    ArrayList<Plant> ary_death=new ArrayList<>();
    ArrayList<String> id_death =new ArrayList<>();

    Button btn_live,btn_death;
    TextView live_percent,death_percent;
    RoundCornerProgressBar progress_live,progress_death;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_plant, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress_live = (RoundCornerProgressBar) view.findViewById(R.id.progress_1);
        progress_death = (RoundCornerProgressBar) view.findViewById(R.id.progress_death);
        btn_live=view.findViewById(R.id.btn_live);
        btn_death=view.findViewById(R.id.btn_death);
        final RecyclerView rv_live=view.findViewById(R.id.rv_live);
        final RecyclerView rv_death=view.findViewById(R.id.rv_death);
        live_percent=view.findViewById(R.id.live_percent);
        death_percent=view.findViewById(R.id.death_percent);


        progress_live.setMax(100);
        progress_death.setMax(100);

        progress_live.setProgress(0);
        progress_death.setProgress(0);


        PlantAdapter plantAdapter_forlive=new PlantAdapter(ary_live);
        rv_live.setAdapter(plantAdapter_forlive);
        rv_live.setLayoutManager(new LinearLayoutManager(getContext()));


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

        FloatingActionButton add_plant=view.findViewById(R.id.add_plant);
        add_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plant_Upload_Dialog plant_upload_dialog=new Plant_Upload_Dialog(getContext());
                plant_upload_dialog.show();
            }
        });
        liveplantReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plant post=dataSnapshot.getValue(Plant.class);
                if(!id_live.contains(post.getId())){
                    ary_live.add(0,post);
                    id_live.add(0, dataSnapshot.getKey());
                    plantAdapter_forlive.notifyItemInserted(0);
                    plantAdapter_forlive.notifyDataSetChanged();
                }

                refreshData();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                plantAdapter_forlive.notifyDataSetChanged();
                refreshData();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String Key = dataSnapshot.getKey();
                int postIndex = id_live.indexOf(Key);
                if (postIndex > -1) {
                    id_live.remove(postIndex);
                    ary_live.remove(postIndex);
                    plantAdapter_forlive.notifyDataSetChanged();
                }
                refreshData();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                plantAdapter_forlive.notifyDataSetChanged();
                refreshData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                plantAdapter_forlive.notifyDataSetChanged();
            }
        });

        deathplantReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plant post=dataSnapshot.getValue(Plant.class);
                if(!id_death.contains(post.getId())){
                    ary_death.add(0,post);
                    id_death.add(0, dataSnapshot.getKey());
                    plantAdapter_fordeath.notifyItemInserted(0);
                    plantAdapter_fordeath.notifyDataSetChanged();
                }


                refreshData();



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                plantAdapter_fordeath.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String Key = dataSnapshot.getKey();

                int postIndex = id_death.indexOf(Key);
                if (postIndex > -1) {
                    id_death.remove(postIndex);
                    ary_live.remove(postIndex);
                    plantAdapter_fordeath.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                plantAdapter_fordeath.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void refreshData(){
        btn_death.setText("DEATH ( "+ary_death.size()+" )");
        btn_live.setText("LIVE ( "+ary_live.size()+" )");
        live_percent.setText((ary_live.size()*100)/(ary_live.size()+ary_death.size())+"%");
        death_percent.setText(100-((ary_live.size()*100)/(ary_live.size()+ary_death.size()))+"%");
        progress_live.setMax(ary_live.size()+ary_death.size());
        progress_death.setMax(ary_live.size()+ary_death.size());
        progress_live.setProgress(ary_live.size());
        progress_death.setProgress(ary_death.size());
    }



}
