package trapleh.io.greenworld.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.helper.PermissionHelper;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.LikeStatic;
import trapleh.io.greenworld.statics.PostStatic;
import trapleh.io.greenworld.statics.UserStatic;

public class CommentActivity extends AppCompatActivity {
    private ImageView backArrow,postUserProfile,postImage;
    private TextView postText,postLikes,postComments,postDate,postUserName;
    private Button postBtn;
    private ImageView likeBtn,commentBtn;

    static  Post post=null;

    ProgressDialog progressDialog;


    DatabaseReference postReference= PostStatic.postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        post=(Post)getIntent().getSerializableExtra("post");

        postUserProfile=findViewById(R.id.post_user_profile);
        postImage=findViewById(R.id.post_item_image);
        postText=findViewById(R.id.post_item_text);
        postLikes=findViewById(R.id.post_item_like_text);
        postComments=findViewById(R.id.post_item_comment_text);
        postUserName=findViewById(R.id.post_user_name);
        postDate=findViewById(R.id.post_item_date);

        likeBtn=findViewById(R.id.post_item_like_btn);
        commentBtn=findViewById(R.id.post_item_comment_btn);

        postUserName.setText(post.getUsername());
        postText.setText(post.getPosttitle());
        postLikes.setText(Integer.toString(post.getLikes()));
        postComments.setText(Integer.toString(post.getComments()));
        PrettyTime p = new PrettyTime();
        postDate.setText(p.format(new Date((long)post.getPosteddate())));

        setLikeActions();
        addListeners();

        Glide.with(getApplicationContext()).load(post.getProfileUrl()).into(postUserProfile);


        if(!post.isHasimage()){
            postImage.setVisibility(View.GONE);
        }else{
            postImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(post.getImageurl())
                    .resize(300, 200)
                    .centerCrop().placeholder(R.drawable.post_img_placeholder)
                    .into(postImage);
        }


        postBtn=findViewById(R.id.comment);
        backArrow=findViewById(R.id.post_back_arrow);

        post=(Post)getIntent().getSerializableExtra("post");

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

    }

    private void addListeners() {
        PostStatic.postRef.child(post.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                postLikes.setText(Integer.toString(post.getLikes()));
                postComments.setText(Integer.toString(post.getComments()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setLikeActions() {
        LikeStatic.likeRef.child(post.getId()).child(UserStatic.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    likeBtn.setImageResource(R.drawable.ic_unlike);
                }else {
                    likeBtn.setImageResource(R.drawable.ic_like);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //listeners
        this.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeBtn.setEnabled(false);
                if(likeBtn.getDrawable().getConstantState() == v.getContext().getResources().getDrawable( R.drawable.ic_like).getConstantState()){
                    Toast.makeText(v.getContext(),"You Unliked this post",Toast.LENGTH_LONG).show();
                    LikeStatic.likeRef.child(post.getId()).
                            child(UserStatic.currentUser.getUid())
                            .setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PostStatic.postRef.child(post.getId()).runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Post post = mutableData.getValue(Post.class);
                                    if (post == null) {
                                        return Transaction.success(mutableData);
                                    }

                                    post.setLikes(post.getLikes()-1);

                                    // Set value and report transaction success
                                    mutableData.setValue(post);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b,
                                                       DataSnapshot dataSnapshot) {
                                    // Transaction completed
                                    likeBtn.setEnabled(true);
                                    Log.d("unliked", "postTransaction:onComplete:" + databaseError);
                                }
                            });
                            likeBtn.setImageResource(R.drawable.ic_unlike);

                        }
                    });
                }else{
                    Toast.makeText(v.getContext(),"You Liked this post",Toast.LENGTH_LONG).show();
                    LikeStatic.likeRef.child(post.getId()).child(UserStatic.currentUser.getUid()).setValue(UserStatic.currentUser.getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    likeBtn.setImageResource(R.drawable.ic_like);
                                    PostStatic.postRef.child(post.getId()).runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Post post = mutableData.getValue(Post.class);
                                            if (post == null) {
                                                return Transaction.success(mutableData);
                                            }

                                            post.setLikes(post.getLikes()+1);

                                            // Set value and report transaction success
                                            mutableData.setValue(post);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                               DataSnapshot dataSnapshot) {
                                            // Transaction completed
                                            likeBtn.setEnabled(true);
                                            Log.d("unliked", "postTransaction:onComplete:" + databaseError);
                                        }
                                    });
                                }
                            });

                }
            }
        });
    }


    private void uploadPost() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        //prepare data

            String id=postReference.push().getKey();
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
