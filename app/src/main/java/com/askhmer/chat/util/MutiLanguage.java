package com.askhmer.chat.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

/**
 * Created by soklundy on 4/12/2016.
 */
public class MutiLanguage {

    private Context context;
    private Activity activity;

    /**
     *
     * @param context
     * @param activity
     */
    public MutiLanguage(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    /***
     * using to get Language
     * @return String en || km
     */
    public String getLanguageCurrent (){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Configuration config = context.getResources().getConfiguration();
        String lang = settings.getString("LANG", "");
        return lang;
    }


    /***
     *
     * @param countryCode
     */
    public void setLanguage(String countryCode){
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("LANG", countryCode).commit();
        setLangRecreate(countryCode);
    }


    /***
     *
     * @param langval
     */
    private void setLangRecreate(String langval) {
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        activity.recreate();;
    }

    /***
     *  check Language when app start up
     */
    public void StartUpCheckLanguage() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
        Configuration config = context.getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        Log.d("Hello", lang);
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }

}
