package com.barkote.kiosk.emojify;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.barkote.kiosk.emojify.utils.Animation;

public class Splash extends AppCompatActivity {

    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAnmation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start();

            }
        },3000);

    }

    private void showAnmation() {

        imageView = findViewById(R.id.imageview);
        imageView.setImageResource(R.drawable.icon);
        Animation.popOut(imageView,1000);

    }

    private void start() {
        startActivity(new Intent(Splash.this,Emojify.class));
    }





}


