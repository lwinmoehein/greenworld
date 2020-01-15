package trapleh.io.greenworld.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.activity.LoginActivity;
import trapleh.io.greenworld.statics.UserStatic;


public class profileFragment extends Fragment {
    private TextView textViewDisplayName;
    private CircleImageView profileImage;
    private Button buttonLogout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewDisplayName = view.findViewById(R.id.textViewDisplayName);
        profileImage = view.findViewById(R.id.profile_image);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        textViewDisplayName.setText(UserStatic.currentUser.getDisplayName());
        Glide.with(this).load(UserStatic.currentUser.getPhotoUrl()).into(profileImage);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileViewButtonSheetDialog profileViewButtonSheetDialog = new ProfileViewButtonSheetDialog();
                profileViewButtonSheetDialog.show(getActivity().getSupportFragmentManager(),"profile_button_sheet");

            }
        });

    }
}
