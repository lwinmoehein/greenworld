package trapleh.io.greenworld.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.model.Plant;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder>{

    private List<Plant> plantArrayList =new ArrayList<>();
    ;

    public PlantAdapter(List<Plant> plantArrayList) {
        this.plantArrayList = plantArrayList;
    }

    @NonNull
    @Override public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plant_item, viewGroup, false);
        PlantViewHolder plantViewHolder=new PlantViewHolder(view);
        return plantViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull PlantViewHolder menuViewHolder, int i) {
        Plant post= plantArrayList.get(i);
        menuViewHolder.bindPostUi(post);
    }

    @Override public int getItemCount() {
        return plantArrayList.size();
    }


    static class PlantViewHolder extends RecyclerView.ViewHolder {
        private TextView plant_name, plant_address, plant_date;
        private ImageView plant_delete;
        public PlantViewHolder(View view){
            super(view);
            this.plant_name =view.findViewById(R.id.plant_name);
            this.plant_address =view.findViewById(R.id.plant_address);
            this.plant_date =view.findViewById(R.id.plant_date);
            plant_delete=view.findViewById(R.id.plant_img);
        }

        public void bindPostUi(Plant plant) {
            this.plant_name.setText(plant.getName());
            this.plant_address.setText(plant.getAddress());
            this.plant_date.setText(plant.getDate());
            if(plant.getStatus().equals("true")){
                this.plant_delete.setVisibility(View.VISIBLE);
            }else{
                this.plant_delete.setVisibility(View.GONE);
            }
        }
    }

}