package com.luvtas.campingau.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.luvtas.campingau.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class OpenImageActivity extends AppCompatActivity {

    ImageView imageView;
    RequestManager glide;
    String image_url;

    private SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);
        imageView = findViewById(R.id.post_pic);
        image_url = getIntent().getStringExtra("shareimage");
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
