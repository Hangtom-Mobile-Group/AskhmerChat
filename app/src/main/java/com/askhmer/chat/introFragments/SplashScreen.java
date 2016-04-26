package com.askhmer.chat.introFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.MainActivityTab;
import com.askhmer.chat.activity.PhoneLogIn;
import com.askhmer.chat.util.SharedPreferencesFile;

/**
 * Created by soklundy on 4/20/2016.
 */
public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = null;

                // Restore preferences
                boolean intro = SharedPreferencesFile.getBooleanSharedPreference(getApplicationContext(),
                        SharedPreferencesFile.PREFER_FILE_NAME,SharedPreferencesFile.PREFER_INTRO_KEY);

                boolean verify = SharedPreferencesFile.getBooleanSharedPreference(getApplicationContext(),
                        SharedPreferencesFile.PREFER_FILE_NAME,SharedPreferencesFile.PERFER_VERIFY_KEY);

                if(intro == true && verify ==false) {
                    mainIntent = new Intent(SplashScreen.this, PhoneLogIn.class);
                }else if(intro == true && verify == true) {
                    mainIntent = new Intent(SplashScreen.this, MainActivityTab.class);
                } else {
                    mainIntent = new Intent(SplashScreen.this, MyIntro.class);
                }

                /* Create an Intent that will start the Menu-Activity. */
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in );
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.splash_screen_layout);
        l.clearAnimation();
        l.startAnimation(anim);
    }
}
