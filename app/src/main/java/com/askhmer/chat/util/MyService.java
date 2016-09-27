package com.askhmer.chat.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Lach Phalleak on 9/24/2016.
 */
public class MyService  extends Service{
    private static WebSocketClient client;
    public static Boolean registered = false;
    private ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        connectivityReceiver=new ConnectivityReceiver();
        super.onCreate();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(connectivityReceiver, filter);
        }catch (Exception e){}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(client != null){
            client.close();
        }
        try {
                unregisterReceiver(connectivityReceiver);
        }catch (Exception e){

        }
        registered=false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initailizeWebsocketClient();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyIBinder extends Binder {

        public MyService getMyService(){
            return MyService.this;
        }

    }
    public class ConnectivityReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    Log.i("WIFI","HAS");
                    if(client== null){
                          SharedPreferencesFile  mSharedPrefer = SharedPreferencesFile.newInstance(context.getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                          final String user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                          if(user_id != null){
                              URI uri=null;
                              try {
                                  uri=new URI(WsConfig.URL_WEBSOCKET);
                              } catch (URISyntaxException e) {
                                  e.printStackTrace();
                              }
                              client=new WebSocketClient(uri,new Draft_17()) {
                                  @Override
                                  public void onOpen(ServerHandshake serverHandshake) {
                                      Log.d("MyConnection", "Connected");
                                      MessageGenerator.sendMessageToServer("", user_id, "",client);
                                  }

                                  @Override
                                  public void onMessage(String s) {
                                      int group_id= getMessagGroupId(s);
                                      if(MySocket.getMessageListener() != null && MySocket.getCurrent_group_id()==group_id){
                                          MySocket.getMessageListener().getMessageFromServer(s);
                                      }else{
                                          myNotify(s);
                                      }
                                  }

                                  @Override
                                  public void onClose(int i, String s, boolean b) {
                                      Log.d("MyConnection", "Closed");
                                  }

                                  @Override
                                  public void onError(Exception e) {

                                  }
                              };
                              client.connect();
                              MySocket.setWebSocketClient(client);
                          }
                      }
                }else{
                    Log.i("WIFI","NO");
                    if(client != null) {
                        client.close();
                    }
                    client=null;
                }
            }
        }
    }
    public void playBeep() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void myNotify(String msg){
        JSONObject jsonObject=null;
        String message="";
        int userid=0;
        int groupid=0;
        String username="";
        try {
            jsonObject=new JSONObject(msg);
            message=jsonObject.getString("message");
            userid=jsonObject.getInt("userid");
            groupid=jsonObject.getInt("groupid");
            username=jsonObject.getString("username");
        }catch (Exception e)
        {

        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.askhmer_logo)
                        .setContentTitle(username)
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.askhmer_logo));
        // Creates an explicit intent for an Activity in your app
        Intent intent=new Intent(this,Chat.class);
        intent.putExtra("groupID",groupid);
        intent.putExtra("Friend_name",username);
        intent.putExtra("friid",userid);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent contentIntent = PendingIntent.getActivity(this,(int)System.currentTimeMillis(),
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setLights(0x0000FF, 1000, 100);
        NotificationManager mNotificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on
        playBeep();
        mNotificationManager.notify(userid, mBuilder.build());
    }

    public void initailizeWebsocketClient(){
        client=null;
        if(client==null){
            SharedPreferencesFile  mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
            final String user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
            if(user_id != null) {
                URI uri = null;
                try {
                    uri = new URI(WsConfig.URL_WEBSOCKET);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                client = new WebSocketClient(uri, new Draft_17()) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d("MyConnection", "Connected");
                        MessageGenerator.sendMessageToServer("", user_id, "",client);
                    }

                    @Override
                    public void onMessage(String s) {
                        int group_id= getMessagGroupId(s);
                        if(MySocket.getMessageListener() != null && MySocket.getCurrent_group_id()==group_id){
                            MySocket.getMessageListener().getMessageFromServer(s);
                        }else{
                            myNotify(s);
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                };
                client.connect();
                MySocket.setWebSocketClient(client);
            }
        }
    }
    public int getMessagGroupId(String msg){
        JSONObject jsonObject=null;
        int group_id=0;
        try {
            jsonObject=new JSONObject(msg);
            group_id=jsonObject.getInt("groupid");
        }catch (Exception e)
        {
            return 0;
        }
        return group_id;
    }

}
