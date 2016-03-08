package com.chatapp.fovi;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


public class ViewImageActivity extends ActionBarActivity {

    ImageView imageView;
    TextView tx;
    TextView txN;
    Uri imageUri;
    private static final int DELAY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = (ImageView) findViewById(R.id.imageViewAct);
        tx = (TextView) findViewById(R.id.tx);
        imageUri = getIntent().getData();
        txN = (TextView) findViewById(R.id.textViewNam);

        Picasso.with(this).load(imageUri.toString()).into(imageView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, DELAY);

        new CountDownTimer(DELAY,1000){
            public void onTick(long a){
                tx.setText("ARCHIVO ELIMINADO EN.... "+String.valueOf(a/1000)+" seg");
            }
            public void onFinish(){
                tx.setVisibility(View.INVISIBLE);
                txN.setText("ÑAM....ÑAM!!");
                Drawable drawable  = getResources().getDrawable(R.drawable.logovisor);
                imageView.setImageDrawable(drawable);
            }
        }.start();

    }

}
