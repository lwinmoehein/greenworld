package trapleh.io.greenworld.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import trapleh.io.greenworld.MainActivity;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.model.User;
import trapleh.io.greenworld.statics.UserStatic;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText editTextName;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = "Phone";
    private String userId = "user id no";
    DatabaseReference mDatabase = UserStatic.userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        if (user.getDisplayName() != null){
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        editTextName = findViewById(R.id.editTextName);

    }


    public void actionRegister(View view) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editTextName.getText().toString())
                .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/greenplanet-62377.appspot.com/o/users%2Fman.png?alt=media&token=17e78b68-6077-4901-bc92-3af43bfe1835"))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(getApplicationContext(),"User profile updated.",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


    }
}
