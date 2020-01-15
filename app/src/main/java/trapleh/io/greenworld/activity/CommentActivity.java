package trapleh.io.greenworld.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.adapter.CommentAdapter;
import trapleh.io.greenworld.helper.PermissionHelper;
import trapleh.io.greenworld.model.Comment;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.CommentStatic;
import trapleh.io.greenworld.statics.LikeStatic;
import trapleh.io.greenworld.statics.PostStatic;
import trapleh.io.greenworld.statics.UserStatic;

public class CommentActivity extends AppCompatActivity {
    private ImageView backArrow,postUserProfile,postImage;
    private TextView postText,postLikes,postComments,postDate,postUserName;
    private ImageButton btnSendComment;
    private ImageView likeBtn,btnComment;

    EditText edtComment;
    String strComment;

    static  Post post=null;

    ProgressDialog progressDialog;

    List<Comment> commentList=new ArrayList<>();
    List<String> commentIds=new ArrayList<>();
    CommentAdapter commentAdapter=null;
    RecyclerView commentRecycler=null;


    DatabaseReference comentReference= CommentStatic.commentRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        post=(Post)getIntent().getSerializableExtra("post");

        commentRecycler=findViewById(R.id.recycler_comments);


        postUserProfile=findViewById(R.id.post_user_profile);
        postImage=findViewById(R.id.post_item_image);
        postText=findViewById(R.id.post_item_text);
        postLikes=findViewById(R.id.post_item_like_text);
        postComments=findViewById(R.id.post_item_comment_text);
        postUserName=findViewById(R.id.post_user_name);
        postDate=findViewById(R.id.post_item_date);

        likeBtn=findViewById(R.id.post_item_like_btn);
        btnComment=findViewById(R.id.post_item_comment_btn);

        edtComment=findViewById(R.id.edt_comment_text);

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


        btnSendComment=findViewById(R.id.btn_send_comment);
        backArrow=findViewById(R.id.post_back_arrow);

        post=(Post)getIntent().getSerializableExtra("post");

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComment();
            }
        });

        commentAdapter=new CommentAdapter(commentList);
        commentRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentRecycler.setAdapter(commentAdapter);

        //bind recycler
        CommentStatic.commentRef.child(post.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment=dataSnapshot.getValue(Comment.class);
                if(!commentIds.contains(comment.getId())){
                    commentList.add(0,comment);
                    commentIds.add(0,comment.getId());
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = (Comment) dataSnapshot.getValue(Comment.class);
                if(commentIds.contains(comment.getId())){
                    Toast.makeText(getApplicationContext(),Integer.toString(comment.getLikes()),Toast.LENGTH_LONG).show();
                    commentAdapter.changeIndexData(comment,commentIds.indexOf(comment.getId()));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadComment() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        //prepare data
        strComment=edtComment.getText().toString();
        Toast.makeText(getApplicationContext(),post.getId(),Toast.LENGTH_LONG).show();

        String id=comentReference.child(post.getId()).push().getKey();
        Comment comment=new Comment(id,post.getId(),UserStatic.currentUser.getUid(),UserStatic.currentUser.getDisplayName(),UserStatic.currentUser.getPhotoUrl().toString(),strComment,0,ServerValue.TIMESTAMP);

        comentReference.child(post.getId()).
                child(id).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {@Override
                    public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();

                        //
            PostStatic.postRef.child(post.getId()).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Post post = mutableData.getValue(Post.class);
                    if (post == null) {
                        return Transaction.success(mutableData);
                    }

                    post.setComments(post.getComments()+1);

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
                         //
                        Toast.makeText(getApplicationContext(),"Uploaded new comment",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

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


}
