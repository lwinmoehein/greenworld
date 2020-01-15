package trapleh.io.greenworld.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import trapleh.io.greenworld.R;

public class ImageViewDialogFragment extends DialogFragment {
    private String TAG = ImageViewDialogFragment.class.getSimpleName();
    PhotoView img;
    String url;
    ProgressBar image_preview_load;


    static public ImageViewDialogFragment newInstance(String url) {
        ImageViewDialogFragment f = new ImageViewDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_view, container, false);
        img=(PhotoView) v.findViewById(R.id.image_preview);
        image_preview_load=(ProgressBar)v.findViewById(R.id.image_preview_load);
        Glide.with(this).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       image_preview_load.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        image_preview_load.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getArguments().getString("url");
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }



    }