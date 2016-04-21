package com.askhmer.chat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by soklundy on 4/20/2016.
 */
public class SharedPreferencesFile {
    public static final String PREFER_INTRO_KEY = "introKey";
    public static final String PREFER_FILE_NAME = "preferFileName";
    public static final String PERFER_VERIFY_KEY = "verifyKey";

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @param perferValue
     */
    public static void putBooleanSharedPreference(Context context, String perferFileName,String perferKey,Boolean perferValue){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(perferKey, perferValue);
        editor.commit();
    }

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @param perferValue
     */
    public static void putStringSharedPreference(Context context, String perferFileName,String perferKey,String perferValue){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(perferKey, perferValue);
        editor.commit();
    }

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @param perferValue
     */
    public static void putIntSharedPreference(Context context, String perferFileName,String perferKey,int perferValue){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(perferKey, perferValue);
        editor.commit();
    }

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @return
     */
    public static Boolean getBooleanSharedPreference(Context context, String perferFileName,String perferKey){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        return settings.getBoolean(perferKey, false);
    }

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @return
     */
    public static String getStringSharedPreference(Context context, String perferFileName,String perferKey){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        return settings.getString(perferKey, "");
    }

    /**
     *
     * @param context
     * @param perferFileName
     * @param perferKey
     * @return
     */
    public static int getIntSharedPreference(Context context, String perferFileName,String perferKey){
        SharedPreferences settings = context.getSharedPreferences(perferFileName, 0);
        return settings.getInt(perferKey, 0);
    }
}
