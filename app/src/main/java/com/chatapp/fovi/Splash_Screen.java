package com.chatapp.fovi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    ProgressBar pb;
    int miProgreso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        TextView myTitle = (TextView)findViewById(R.id.txttitulo);
        ImageView logo= (ImageView)findViewById(R.id.imageView);
        pb = (ProgressBar) findViewById(R.id.progressBar3);
        pb.setScaleY(2f);

        new Thread(myThread).start();

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/sensei-medium.otf");
        myTitle.setTypeface(myFont);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation mover = AnimationUtils.loadAnimation(this,R.anim.mover);
        myTitle.startAnimation(alpha);
        logo.startAnimation(mover);
        openApp(true);

    }


    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen
                        .this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private Runnable myThread = new Runnable() {
        @Override
        public void run() {
            while(miProgreso<100){
                try {
                    myHandle.sendMessage(myHandle.obtainMessage());
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Handler myHandle = new Handler(){
            @Override
            public void handleMessage(Message msg){
                miProgreso++;
                pb.setProgress(miProgreso);
            }

        };
    };



}

