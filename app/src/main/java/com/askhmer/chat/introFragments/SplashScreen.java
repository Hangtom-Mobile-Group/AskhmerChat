package com.askhmer.chat.introFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.MainActivityTab;
import com.askhmer.chat.activity.PhoneLogIn;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by soklundy on 4/20/2016.
 */
public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private SharedPreferencesFile mSharedPref;



    public String friend_add;
    public String badgeCount = null;
    public String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();

        mSharedPref  = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPref.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);



        getCountFriendAdd();







        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = null;

                // Restore preferences
                boolean intro = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.PREFER_INTRO_KEY);

                boolean verify = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.PERFER_VERIFY_KEY);

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
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate );
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.splash_screen_layout);
        l.clearAnimation();
        l.startAnimation(anim);
    }




    /**
     * count number of friend add me
     */
    public void getCountFriendAdd() {
        String url ="http://chat.askhmer.com/api/friend/countFriendAdd/"+user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        badgeCount = String.valueOf(response.getInt("DATA"));
                        //ShortcutBadger.applyCount(getApplicationContext(), badgeCount);
                        //  Toast.makeText(MainActivityTab.this, badgeCount+"", Toast.LENGTH_SHORT).show();
                        Log.d("BAD", badgeCount + "");
                    } else {
                        Toast.makeText(SplashScreen.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    friend_add = mSharedPref.putStringSharedPreference(SharedPreferencesFile.FRIEND_ADD,badgeCount);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashScreen.this, "There is Something Wrong !!", Toast.LENGTH_LONG).show();
                Log.d("ravyerror",error.toString());
            }
        });
        // Add request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }




}
