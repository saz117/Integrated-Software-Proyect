package com.example.proyectonotaspis.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonotaspis.R;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoginRegister.class));

/*
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent loginIntent = new Intent(MainActivity.this, LoginRegister.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    */}
}
