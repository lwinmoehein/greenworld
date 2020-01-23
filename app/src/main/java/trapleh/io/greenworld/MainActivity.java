package trapleh.io.greenworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import trapleh.io.greenworld.fragment.MainFragment;
import trapleh.io.greenworld.fragment.PlantFragment;
import trapleh.io.greenworld.fragment.PlasticFragment;
import trapleh.io.greenworld.fragment.profileFragment;
import trapleh.io.greenworld.ui.TabAdapter;


public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        adapter.addFragment(new MainFragment(), "home");
        adapter.addFragment(new PlasticFragment(), "plastic");
        adapter.addFragment(new PlantFragment(), "plants");
        adapter.addFragment(new profileFragment(), "profile");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Log.i("user",user.getDisplayName());
        Log.i("user photo url",user.getPhotoUrl().toString());
    }
}
