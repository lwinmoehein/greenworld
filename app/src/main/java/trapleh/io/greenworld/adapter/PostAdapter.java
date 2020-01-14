package trapleh.io.greenworld.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post> postArrayList=new ArrayList<>();
    ;

    public PostAdapter(List<Post> postArrayList) {
        this.postArrayList = postArrayList;
        Log.d("Menu Adapter","menu adapter initiated");
    }

    @NonNull
    @Override public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_item_image, viewGroup, false);
        PostViewHolder postViewHolder=new PostViewHolder(view);
        return postViewHolder;
    }

    @Override public void onBindViewHolder(@NonNull PostViewHolder menuViewHolder, int i) {
        Post post=postArrayList.get(i);
        menuViewHolder.bindPostUi(post);
    }

    @Override public int getItemCount() {
        return postArrayList.size();
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView username,posttext,liketext,commenttext,date;
        private ImageView userprofile,postimage;
        public PostViewHolder(View view){
            super(view);
            this.username=view.findViewById(R.id.post_user_name);
            this.posttext=view.findViewById(R.id.post_item_text);
            this.liketext=view.findViewById(R.id.post_item_like_text);
            this.commenttext=view.findViewById(R.id.post_item_comment_text);
            this.date=view.findViewById(R.id.post_item_date);

            this.userprofile=view.findViewById(R.id.post_user_profile);
            this.postimage=view.findViewById(R.id.post_item_image);

        }

        public void bindPostUi(Post post) {
            this.username.setText(post.getUsername());
            this.posttext.setText(post.getPosttitle());
            this.liketext.setText(Integer.toString(post.getLikes()));
            this.commenttext.setText(Integer.toString(post.getComments()));
            if(!post.isHasimage()){
                postimage.setVisibility(View.GONE);
            }else{
                Picasso.get()
                        .load(post.getImageurl())
                        .resize(300, 200)
                        .centerCrop().placeholder(R.drawable.post_img_placeholder)
                        .into(postimage);
            }
            //this.date.setText(new String(post.getPosteddate()));
        }
    }

}