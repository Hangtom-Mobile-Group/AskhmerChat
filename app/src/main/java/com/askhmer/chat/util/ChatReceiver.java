package com.askhmer.chat.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by DELL E6420 on 9/22/2016.
 */
public class ChatReceiver extends BroadcastReceiver {
    Context context;

    public static final String SMS_CONTENT = "sms_content";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("app", "Network connectivity change");
        if(intent.getExtras()!=null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");


                //-------------connect socket

                //--------------------------------

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");



            }
        }
    }

}
