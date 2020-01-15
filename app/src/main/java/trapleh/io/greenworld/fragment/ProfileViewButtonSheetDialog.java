package trapleh.io.greenworld.fragment;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.helper.PermissionHelper;
import trapleh.io.greenworld.statics.UserStatic;

public class ProfileViewButtonSheetDialog extends BottomSheetDialogFragment {

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_click_button_sheet_layout,container,false);
        Button buttonProfileView = view.findViewById(R.id.buttonProfileView);
        Button buttonUploadProfile = view.findViewById(R.id.buttonUploadProfile);
        buttonProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mFileUri = UserStatic.currentUser.getPhotoUrl().toString();
                FragmentManager ft = getActivity().getSupportFragmentManager();
                ImageViewDialogFragment newFragment = ImageViewDialogFragment.newInstance(mFileUri);
                newFragment.show(ft, "slideshow");
            }
        });
        buttonUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });
        return view;
    }

    private void selectImg() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionHelper.checkImgPickPermission(getActivity());
            }
            TedBottomPicker.with(getActivity())
                    .setPeekHeight(1200)
                    .setTitle("please pick an image")
                    .showTitle(true)
                    .setGalleryTileBackgroundResId(R.color.colorPrimary)
                    .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                        @Override
                        public void onImageSelected(Uri uri) {
                            if(uri!=null) {
                                progressDialog=new ProgressDialog(getActivity());
                                progressDialog.setMessage("Uploading...");
                                progressDialog.show();
                                uploadImage(uri);

                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Cannot Pick Image", Toast.LENGTH_SHORT).show();
        }

    }

    private  void uploadImage(Uri uriPostImgPath){
        StorageReference childRef = UserStatic.userImageRef.child(UUID.randomUUID().toString()+".jpg");
        childRef.putFile(uriPostImgPath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                Double.isNaN(bytesTransferred);
                bytesTransferred *= 100.0d;
                double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                bytesTransferred /= totalByteCount;
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Uploaded ");
                stringBuilder.append((int) bytesTransferred);
                stringBuilder.append("%");
                progressDialog.setMessage(stringBuilder.toString());

            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                UserStatic.currentUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(),"Profile update success",Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });



                            }
                        })
                        ;
                    }
                });
    }
}
