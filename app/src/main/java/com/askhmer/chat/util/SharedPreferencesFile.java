package com.askhmer.chat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by soklundy on 4/20/2016.
 */
public class SharedPreferencesFile {
    public static final String PREFER_INTRO_KEY = "introKey";
    public static final String PREFER_FILE_NAME = "preferFileName";
    public static final String PREFER_FILE_NAME2 = "preferFileName2";
    public static final String PERFER_VERIFY_KEY = "verifyKey";
    public static final String PERFER_LOGIN_FACEBOOK_KEY = "loginFacebookKeyForScreen";
    public static final String USERIDKEY = "userId";

    private Context mContext;
    private static SharedPreferencesFile mInstance = null;
    private SharedPreferences mSettings = null;
    private SharedPreferences.Editor mEditor = null;


    public SharedPreferencesFile(Context context, String sharedPrefName) {
        mContext = context;
        mSettings = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public static SharedPreferencesFile newInstance(Context context, String sharedPrefName ) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesFile(context, sharedPrefName);
        }
        return mInstance;
    }

    /**
     * @param perferKey
     * @param perferValue
     */
    public void putBooleanSharedPreference(String perferKey, boolean perferValue){
        mEditor.putBoolean(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferKey
     * @param perferValue
     */
    public void putStringSharedPreference(String perferKey,String perferValue){
        mEditor.putString(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferFileName
     * @param perferKey
     * @param perferValue
     */
    public void putIntSharedPreference(String perferFileName,String perferKey,int perferValue){
        mEditor.putInt(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferKey
     * @return
     */
    public boolean getBooleanSharedPreference(String perferKey){
        return mSettings.getBoolean(perferKey, false);
    }

    /**
     * @param perferKey
     * @return
     */
    public String getStringSharedPreference(String perferKey){
        return mSettings.getString(perferKey, null);
    }

    /**
     * @param perferFileName
     * @param perferKey
     * @return
     */
    public int getIntSharedPreference(String perferFileName, String perferKey){
        return mSettings.getInt(perferKey, 0);
    }
}
