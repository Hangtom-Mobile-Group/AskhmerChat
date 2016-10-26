package com.askhmer.chat.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.askhmer.chat.activity.VerifyCode;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();
    public static final String SMS_CONTENT = "sms_content";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String sender;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        // Here you have the sender(phone number)
                        sender = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        // you have the sms content in the msgBody
                        if(sender.equals("Medayi")) {
                             Intent fireActivityIntent = new Intent(context, VerifyCode.class);
                             fireActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             fireActivityIntent.putExtra(SMS_CONTENT, msgBody);
                             context.startActivity(fireActivityIntent);
                         }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }
}