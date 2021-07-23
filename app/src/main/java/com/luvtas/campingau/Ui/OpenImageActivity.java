package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.luvtas.campingau.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class OpenImageActivity extends AppCompatActivity {

    ImageView imageView,previewImageView,playButton;
    RequestManager glide;
    String image_url, image_type;
    String[] imagesUri;
    MediaController mediaController;
    RelativeLayout R_layout_video;
    VideoView videoView;
    RecyclerView recyclerView;
    ImageButton imageButton;

    private SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);
        recyclerView = findViewById(R.id.rvImages);
        image_url = getIntent().getStringExtra("shareimage");
        image_type = getIntent().getStringExtra("image_type");
        imagesUri = getIntent().getStringArrayExtra("shareImages");
        R_layout_video = findViewById(R.id.R_layout_video);
        previewImageView= findViewById(R.id.previewImageView);
        playButton = findViewById(R.id.playButton);
        videoView = findViewById(R.id.player_video);
        imageButton = findViewById(R.id.ibBack);

        if(image_type.equals("video")){
            R_layout_video.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            try {
                mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                Uri video_path = Uri.parse(image_url);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video_path);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previewImageView.setVisibility(View.GONE);
                        playButton.setVisibility(View.GONE);
                        videoView.start();
                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playButton.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.something_gone_wrong), Toast.LENGTH_SHORT).show();
            }
        } else {
            R_layout_video.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(new CustomAdapter(imagesUri));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(OpenImageActivity.this);
            }
        });
        //slidrInterface = Slidr.attach(this);
    }

    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        String[] imagesArrayUri;

        public CustomAdapter(String[] imagesArrayUri) {
            this.imagesArrayUri = imagesArrayUri;
        }
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        static class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.ivPreview);
            }

            public ImageView getImageView() {
                return imageView;
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_post_detail_row_item, viewGroup, false);

            return new CustomAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OpenImageActivity.CustomAdapter.ViewHolder holder, int position) {
            Glide.with(holder.itemView).load(imagesArrayUri[position]).into(holder.imageView);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return imagesArrayUri.length;
        }
    }
}
