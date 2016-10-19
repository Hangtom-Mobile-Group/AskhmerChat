package com.askhmer.chat.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
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
    private String image_url;

    public NotificationGenerator(Context context,String message,String username,int groupid,int userid,String image_url) {
        super();
        this.context = context;
        this.message=message;
        this.username=username;
        this.groupid=groupid;
        this.userid=userid;
        this.image_url=image_url;
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
                            .setContentText(messageGenerator())
                            .setSound(path)
                            .setLargeIcon(result);
            // Creates an explicit intent for an Activity in your app
            Intent intent=new Intent(context,Chat.class);
            intent.putExtra("groupID",groupid);
            intent.putExtra("Friend_name",username);
            intent.putExtra("friid",userid);
            intent.putExtra("friend_image_url",image_url);
            intent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent contentIntent = PendingIntent.getActivity(context,(int)System.currentTimeMillis(),
                    intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(0x0000FF, 1000, 1000);
            mBuilder.setSmallIcon(getNotificationIcon());
            NotificationManager mNotificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on
            //playBeep();
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if(isScreenOn==false)
            {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                wl.acquire(6000);
                PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
                wl_cpu.acquire(6000);
            }
            mNotificationManager.notify(userid, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notify : R.mipmap.ic_launcher;
    }

    public  String messageGenerator(){
        if(message.contains("chat.askhmer.com/resources/upload/file/sticker")){
            return "sent sticker";
        }else if(message.contains("chat.askhmer.com/resources/upload/file/thumnails") ||
                 message.contains("chat.askhmer.com/resources/upload/file/images")){
            return "sent photo";
        }else if(message.contains("chat.askhmer.com/resources/upload/file/audio")){
            return "sent audio file";
        }else{
            return message;
        }
    }
}