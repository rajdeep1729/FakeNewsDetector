package com.example.rajdeep.fakenewsdetetor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.rajdeep.fakenewsdetector.R;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 4000;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageView imageView  = findViewById(R.id.splashscreen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        imageView.startAnimation(animation);
        ImageView imageView1  = findViewById(R.id.brand);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        imageView1.startAnimation(animation1);

        progressBar = (ProgressBar)findViewById(R.id.spinkit);

        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,FirstActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}