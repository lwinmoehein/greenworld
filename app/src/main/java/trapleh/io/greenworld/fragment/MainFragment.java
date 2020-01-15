package trapleh.io.greenworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import trapleh.io.greenworld.MainActivity;
import trapleh.io.greenworld.R;
import trapleh.io.greenworld.activity.NewPostActivity;
import trapleh.io.greenworld.adapter.PostAdapter;
import trapleh.io.greenworld.model.Post;
import trapleh.io.greenworld.statics.PostStatic;

public class MainFragment extends Fragment {
    RecyclerView recyclerPosts;
    FloatingActionButton addPost;
    PostAdapter adapterPosts;
    List<Post> posts=new ArrayList<>();
    List<String> postIds=new ArrayList<>();

    DatabaseReference postReference= PostStatic.postRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerPosts=view.findViewById(R.id.recycler_posts);
        addPost=view.findViewById(R.id.add_post);

        adapterPosts=new PostAdapter(posts);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPosts.setAdapter(adapterPosts);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),NewPostActivity.class);
                startActivity(intent);
            }
        });

        postReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post=dataSnapshot.getValue(Post.class);
                if(!postIds.contains(post.getId())){
                    posts.add(0,post);
                    postIds.add(0,post.getId());
                    adapterPosts.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = (Post) dataSnapshot.getValue(Post.class);
                if(postIds.contains(post.getId())){
                    adapterPosts.changeIndexData(post,postIds.indexOf(post.getId()));
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
}