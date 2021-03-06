package com.askhmer.chat.introFragments;

import android.content.Intent;
import android.databinding.tool.util.BrNameUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.PhoneLogIn;
import com.askhmer.chat.util.MutiLanguage;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.github.paolorotolo.appintro.AppIntro2;

/**
 * Created by soklundy on 4/19/2016.
 * lib url: https://github.com/PaoloRotolo/AppIntro
 */
public class MyIntro extends AppIntro2 {

    private SharedPreferencesFile mSharedPref;


    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        mSharedPref = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new IntroOne());
        addSlide(new IntroTwo());
        addSlide(new IntroThree());
        setDepthAnimation();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
       // addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        /*showSkipButton(false);*/
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(50);

    }

    @Override
    public void onDonePressed() {

        /* add by thoeurn */

        Intent intent = getIntent();
        String getVerify = intent.getStringExtra("fragmentSetting");

        if(getVerify != null && getVerify.equals("fragmentFour")) {
            this.finish();
        } else {

        /* end add by thoeurn*/

            loadMainActivity();
            this.finish();

            final RadioGroup toggle = (RadioGroup) findViewById(R.id.radio_language);
            final MutiLanguage mutiLanguage = new MutiLanguage(getApplicationContext(), this);
            String lang = mutiLanguage.getLanguageCurrent();

            if (toggle.getCheckedRadioButtonId() == R.id.radio_english) {
                mutiLanguage.setLanguage("en");
            }else {
                mutiLanguage.setLanguage("km");
            }

        }

        mSharedPref.putBooleanSharedPreference(SharedPreferencesFile.PREFER_INTRO_KEY,true);

    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, PhoneLogIn.class);
        startActivity(intent);
    }

}
