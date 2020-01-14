package trapleh.io.greenworld.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import trapleh.io.greenworld.MainActivity;
import trapleh.io.greenworld.R;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextPhoneNumber;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user !=null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        editTextPhoneNumber= findViewById(R.id.editTextPhoneNumber);
    }

    public void actionLogin(View view) {
        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
        Intent phoneAuthActivity = new Intent(this,PhoneAuthActivity.class);
        String phoneNo = editTextPhoneNumber.getText().toString();
        phoneAuthActivity.putExtra("phone_no",phoneNo);
        startActivity(phoneAuthActivity);
        finish();
    }
}
