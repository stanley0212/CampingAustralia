package com.luvtas.campingau.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
    MediaController mediaController;
    RelativeLayout R_layout_video;
    VideoView videoView;

    private SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);
        imageView = findViewById(R.id.post_pic);
        image_url = getIntent().getStringExtra("shareimage");
        image_type = getIntent().getStringExtra("image_type");
        R_layout_video = findViewById(R.id.R_layout_video);
        previewImageView= findViewById(R.id.previewImageView);
        playButton = findViewById(R.id.playButton);
        videoView = findViewById(R.id.player_video);

        if(image_type == "video"){
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
        }


        Glide.with(getApplication()).load(image_url).into(imageView);
        findViewById(R.id.post_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(OpenImageActivity.this);
            }
        });
        slidrInterface = Slidr.attach(this);
    }
}
