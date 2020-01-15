package trapleh.io.greenworld.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.model.Plant;
import trapleh.io.greenworld.model.Post;


public class Plant_Upload_Dialog extends Dialog {
    Context c;
    public Plant_Upload_Dialog(final Context context) {
        super(context);
        this.c=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_upload_dialog);
        Button btn_upload=findViewById(R.id.btn_upload_plant);
        EditText name,address;
        name=findViewById(R.id.p_name);
        address=findViewById(R.id.p_address);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference postReference= FirebaseDatabase.getInstance().getReference().child("user_id").child("LIVE_PLANT");

                String id=postReference.push().getKey();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                Plant plant=new Plant(id,name.getText().toString(),address.getText().toString(),"true",formatter.format(date));
                postReference.child(id).setValue(plant)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c,"Uploaded Plant",Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        });
            }
        });

    }//oncreate







}
