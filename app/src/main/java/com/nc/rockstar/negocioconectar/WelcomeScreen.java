package com.nc.rockstar.negocioconectar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome_Intent = new Intent(WelcomeScreen.this, loginActivity.class);
                startActivity(welcome_Intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

}
