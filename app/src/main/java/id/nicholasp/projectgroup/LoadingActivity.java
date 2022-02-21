package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView loading = (ImageView) findViewById(R.id.imageView);
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.stop();
                Intent myIntent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        },1000);
    }
}