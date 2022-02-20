package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    ImageView logo;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = findViewById(R.id.logo);
        Timer();
        fadeInAndHideImage(logo);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent myIntent = new Intent(SplashScreenActivity.this,LoginFormActivity.class);
//                startActivity(myIntent);
//                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                finish();
//            }
//        },4000);
    }
    private void fadeInAndHideImage(final ImageView img)
    {
        // Fade Animation code
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(2500); // How long it takes for the animation to complete in milliseconds

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            // Once the animation is done, set the visibility of the logo to GONE and navigate to the MainActivity after the set amount of time.
            public void onAnimationEnd(Animation animation)
            {
                /* New Handler to start the Menu-Activity
                 * and close this Splash-Screen after some seconds.*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        startActivity(new Intent(getApplicationContext(), LoginFormActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();

                    }
                }, 3000);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }
    private void Timer() {
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        mCountDownTimer=new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress((int)i*100/(10000/2000));

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