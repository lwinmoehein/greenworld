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
import trapleh.io.greenworld.model.Comment;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.CommentLikeStatic;
import trapleh.io.greenworld.statics.CommentStatic;
import trapleh.io.greenworld.statics.LikeStatic;
import trapleh.io.greenworld.statics.PostStatic;
import trapleh.io.greenworld.statics.UserStatic;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private List<Comment> commentList=new ArrayList<>();
    ;

    public CommentAdapter(List<Comment> comments) {
        this.commentList = comments;
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
    @Override public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_item, viewGroup, false);
        CommentAdapter.CommentViewHolder commentviewholder=new CommentViewHolder(view);
        return commentviewholder;
    }

    @Override public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder postViewHolder, int i) {
        Comment comment=commentList.get(i);
        postViewHolder.bindPostUi(comment,this,commentList);
    }

    @Override public int getItemCount() {
        return commentList.size();

    }

    public void changeIndexData(Comment comment, int i) {
        this.commentList.get(i).setLikes(comment.getLikes());
        notifyDataSetChanged();
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView username,posttext,liketext,date;
        private ImageView userprofile,postimage,likeBtn;
        public CommentViewHolder(View view){
            super(view);
            this.username=view.findViewById(R.id.post_user_name);
            this.posttext=view.findViewById(R.id.post_item_text);
            this.liketext=view.findViewById(R.id.post_item_like_text);
            this.date=view.findViewById(R.id.post_item_date);

            this.userprofile=view.findViewById(R.id.post_user_profile);
            this.postimage=view.findViewById(R.id.post_item_image);

            //like and comment
            this.likeBtn=view.findViewById(R.id.post_item_like_btn);



        }

        public void bindPostUi(Comment comment, CommentAdapter adapter,List<Comment> posts) {

            //bind data
            Glide.with(username.getContext()).load(comment.getProfileurl()).into(this.userprofile);
            this.username.setText(comment.getUsername());
            this.posttext.setText(comment.getCommenttitle());
            this.liketext.setText(Integer.toString(comment.getLikes()));
            PrettyTime p = new PrettyTime();
            date.setText(p.format(new Date((long)comment.getPosteddate())));
            //end of binding
            CommentLikeStatic.likeRef.child(comment.getId()).child(UserStatic.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        CommentLikeStatic.likeRef.child(comment.getId()).
                                child(UserStatic.currentUser.getUid())
                                .setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(),"You Unliked this post",Toast.LENGTH_LONG).show();


                                CommentStatic.commentRef.child(comment.getPostid()).child(comment.getId()).runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        Comment c = mutableData.getValue(Comment.class);
                                        if (c == null) {
                                            return Transaction.success(mutableData);
                                        }

                                        c.setLikes(c.getLikes()-1);

                                        // Set value and report transaction success
                                        mutableData.setValue(c);
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
                        CommentLikeStatic.likeRef.child(comment.getId()).child(UserStatic.currentUser.getUid()).setValue(UserStatic.currentUser.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(),"You Liked this post",Toast.LENGTH_LONG).show();

                                        likeBtn.setImageResource(R.drawable.ic_like);
                                        CommentStatic.commentRef.child(comment.getPostid()).child(comment.getId()).runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                Comment c = mutableData.getValue(Comment.class);
                                                if (c == null) {
                                                    return Transaction.success(mutableData);
                                                }

                                                c.setLikes(c.getLikes()+1);

                                                // Set value and report transaction success
                                                mutableData.setValue(c);
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

}