package trapleh.io.greenworld.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.activity.CommentActivity;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.LikeStatic;
import trapleh.io.greenworld.statics.PostStatic;
import trapleh.io.greenworld.statics.UserStatic;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post> postArrayList=new ArrayList<>();
    ;

    public PostAdapter(List<Post> postArrayList) {
        this.postArrayList = postArrayList;
        this.setHasStableIds(true);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_item, viewGroup, false);
        PostViewHolder postViewHolder=new PostViewHolder(view);
        return postViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull PostViewHolder menuViewHolder, int i) {
        Post post=postArrayList.get(i);
        menuViewHolder.bindPostUi(post,this,postArrayList);
    }

    @Override public int getItemCount() {
        return postArrayList.size();

    }

    public void changeIndexData(Post post, int i) {
        postArrayList.get(i).setLikes(post.getLikes());
        postArrayList.get(i).setComments(post.getComments());
        postArrayList.get(i).setPosttitle(post.getPosttitle());
        notifyDataSetChanged();
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView username,posttext,liketext,commenttext,date;
        private ImageView userprofile,postimage,likeBtn,commentBtn;
        public PostViewHolder(View view){
            super(view);
            this.username=view.findViewById(R.id.post_user_name);
            this.posttext=view.findViewById(R.id.post_item_text);
            this.liketext=view.findViewById(R.id.post_item_like_text);
            this.commenttext=view.findViewById(R.id.post_item_comment_text);
            this.date=view.findViewById(R.id.post_item_date);

            this.userprofile=view.findViewById(R.id.post_user_profile);
            this.postimage=view.findViewById(R.id.post_item_image);

            //like and comment
            this.likeBtn=view.findViewById(R.id.post_item_like_btn);
            this.commentBtn=view.findViewById(R.id.post_item_comment_btn);



        }

        public void bindPostUi(Post post, PostAdapter adapter,List<Post> posts) {

            //bind data
            Glide.with(username.getContext()).load(post.getProfileUrl()).into(this.userprofile);
            this.username.setText(post.getUsername());
            this.posttext.setText(post.getPosttitle());
            this.liketext.setText(Integer.toString(post.getLikes()));
            this.commenttext.setText(Integer.toString(post.getComments()));
            if(!post.isHasimage()){
                postimage.setVisibility(View.GONE);
            }else{
                postimage.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(post.getImageurl())
                        .resize(300, 200)
                        .centerCrop().placeholder(R.drawable.post_img_placeholder)
                        .into(postimage);
            }
            PrettyTime p = new PrettyTime();
            date.setText(p.format(new Date((long)post.getPosteddate())));
            //end of binding
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
            this.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent( (v.getContext()),CommentActivity.class);
                    intent.putExtra("post",post);
                    ((Activity)v.getContext()).startActivity(intent);
                 }
            });

        }
    }

}