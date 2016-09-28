package com.askhmer.chat.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lach Phalleak on 9/28/2016.
 */

public class NotificationGenerator extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private String message;
    private String username;
    private int groupid;
    private int userid;

    public NotificationGenerator(Context context,String message,String username,int groupid,int userid) {
        super();
        this.context = context;
        this.message=message;
        this.username=username;
        this.groupid=groupid;
        this.userid=userid;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        super.onPostExecute(result);
        try {
            Uri path = Uri.parse("android.resource://com.askhmer.chat/raw/notification");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setContentTitle(username)
                            .setContentText(message)
                            .setSound(path)
                            .setLargeIcon(result);
            // Creates an explicit intent for an Activity in your app
            Intent intent=new Intent(context,Chat.class);
            intent.putExtra("groupID",groupid);
            intent.putExtra("Friend_name",username);
            intent.putExtra("friid",userid);
            intent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent contentIntent = PendingIntent.getActivity(context,(int)System.currentTimeMillis(),
                    intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(0x0000FF, 1000, 1000);
            mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
            NotificationManager mNotificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on
            //playBeep();
            mNotificationManager.notify(userid, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            return R.mipmap.askhmer_logo;

        } else {
            return R.mipmap.askhmer_logo;
        }
    }
}