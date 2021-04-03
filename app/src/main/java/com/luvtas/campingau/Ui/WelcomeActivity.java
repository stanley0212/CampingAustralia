package com.luvtas.campingau.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.luvtas.campingau.R;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;  //設定等待跳轉時間

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
//        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //image = findViewById(R.id.image);
//        title = findViewById(R.id.title);
//        slogan = findViewById(R.id.slogan);

        //image.setAnimation(topAnim);
//        title.setAnimation(topAnim);
//        slogan.setAnimation(bottomAnim);

        //sessionManager = new SessionManager(this);
        //sessionManager.checkLogin();

        //HashMap<String, String> user = sessionManager.getUserDetail();
        //String mname = user.get(sessionManager.NAME);
        //if(!mname.isEmpty()){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class); //MainActivity為主要檔案名稱
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
