package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Timer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashScreenActivity.this,LoginFormActivity.class);
                startActivity(myIntent);
                finish();
            }
        },5000);
    }

    private void Timer() {
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        mCountDownTimer=new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress((int)i*100/(10000/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
            }
        };
        mCountDownTimer.start();
    }
}