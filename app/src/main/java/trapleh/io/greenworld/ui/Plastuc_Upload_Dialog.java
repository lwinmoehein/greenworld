package trapleh.io.greenworld.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.model.Plant;
import trapleh.io.greenworld.statics.UserStatic;


public class Plastuc_Upload_Dialog extends Dialog {
    Context c;
    public Plastuc_Upload_Dialog(final Context context) {
        super(context);
        this.c=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plastic_upload_dialog);


    }//oncreate

}
