package trapleh.io.greenworld.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import trapleh.io.greenworld.R;
import trapleh.io.greenworld.adapter.PostAdapter;
import trapleh.io.greenworld.model.Post;

public class MainFragment extends Fragment {
    RecyclerView recyclerPosts;
    PostAdapter adapterPosts;
    List<Post> posts=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerPosts=view.findViewById(R.id.recycler_posts);

        posts.add(new Post("id","u","hola","po","adfadfa",true,3,3,"3.3.3"));
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());

        adapterPosts=new PostAdapter(posts);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPosts.setAdapter(adapterPosts);



    }
}