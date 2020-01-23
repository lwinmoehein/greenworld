package trapleh.io.greenworld.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.helper.PermissionHelper;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.PostStatic;
import trapleh.io.greenworld.statics.UserStatic;

public class NewPostActivity extends AppCompatActivity {
    private ImageView backArrow,selectedImage;
    private TextView postButton;
    private EditText postText;
    private Button selectImage,postBtn;

    private String strPostText;
    private Uri uriPostImgPath;

    ProgressDialog progressDialog;


    DatabaseReference postReference= PostStatic.postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        backArrow=findViewById(R.id.post_back_arrow);
        postButton=findViewById(R.id.post_btn);
        selectImage=findViewById(R.id.selectImage);
        postBtn=findViewById(R.id.uploadPost);
        postText=findViewById(R.id.post_text);
        selectedImage=findViewById(R.id.post_image);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });
    }

    private void selectImg() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionHelper.checkImgPickPermission(this);
            }
            TedBottomPicker.with(this)
                    .setPeekHeight(1200)
                    .setTitle("please pick an image")
                    .showTitle(true)
                    .setGalleryTileBackgroundResId(R.color.colorPrimary)
                    .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                        @Override
                        public void onImageSelected(Uri uri) {
                            if(uri!=null) {
                                selectedImage.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(uri)
                                        .into(selectedImage);
                                uriPostImgPath = uri;
                            }
                         }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot Pick Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadPost() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        //prepare data

        strPostText=postText.getText().toString();


        if(uriPostImgPath!=null){
            StorageReference childRef = PostStatic.postImageRef.child(UUID.randomUUID().toString()+".jpg");
            childRef.putFile(uriPostImgPath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                    Double.isNaN(bytesTransferred);
                    bytesTransferred *= 100.0d;
                    double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                    Double.isNaN(totalByteCount);
                    bytesTransferred /= totalByteCount;
                    ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
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
                            progressDialog.dismiss();
                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String id=postReference.push().getKey();
                                    Post post=new Post(id, UserStatic.currentUser.getUid(),UserStatic.currentUser.getDisplayName()
                                            ,UserStatic.currentUser.getPhotoUrl().toString(),strPostText,true,uri.toString(),0,0,ServerValue.TIMESTAMP);
                                    postReference.child(id).setValue(post);
                                    Toast.makeText(getApplicationContext(),"Uploaded new Post",Toast.LENGTH_LONG).show();
                                    finish();


                                }
                            })
                            ;
                        }
                    });
        }else {
            String id=postReference.push().getKey();
            Post post=new Post(id,
                    UserStatic.currentUser.getUid(),UserStatic.currentUser.getDisplayName()
                    ,UserStatic.currentUser.getPhotoUrl().toString(),strPostText,false,"",0,0,ServerValue.TIMESTAMP);
            postReference.child(id).setValue(post)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Uploaded new Post",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                }
            });



        }

    }
}
