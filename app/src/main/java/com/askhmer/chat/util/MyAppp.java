package com.askhmer.chat.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.askhmer.chat.fragments.TwoFragment;
import com.askhmer.chat.listener.MessageListener;
import com.askhmer.chat.listener.NewMessageListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Longdy on 9/19/2016.
 */
public class MyAppp extends Application {

    private static MyAppp mInstance;
    //web socket
    private static MessageListener messageListener;
    private static NewMessageListener newMessageListener;
    private static WebSocketClient webSocketClient;
    private static int current_group_id;
    private static Context MyContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try{
            stopService(new Intent(this, MyService.class));
        }catch (Exception e){

        }finally {
            startService(new Intent(this, MyService.class));
        }
    }

    //-----set and get message listener

    public static void setMessageListener(Context context){
        MyContext=context;
        messageListener= (MessageListener) context;
    }
    public static MessageListener getMessageListener(){
        return  messageListener;
    }





    //-----set and get web socket Client

    public static void setWebSocketClient(WebSocketClient client){
        webSocketClient=client;
    }
    public static WebSocketClient getWebSocketClient(){
        return webSocketClient;
    }



    //----send message
    public static   void sendMessage(String message){
        try{
            webSocketClient.send(message);
        }catch(Exception e){
            e.printStackTrace();
            Log.i("Send Message","Send Failed");
            ArrayList<String> messages=new ArrayList<String>();
            messages.add(message);
            reSendMessage(messages);
        }
    }

    //---set and get group chat ID

    public static void setCurrent_group_id(int groupid){
        current_group_id=groupid;
    }

    public static int getCurrent_group_id(){
        return current_group_id;
    }


    //--check internet connection
    public static synchronized MyAppp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public static void  reSendMessage(final ArrayList<String> message){
        // Log.d("Initial Socket","Initialize Socket");
        if(webSocketClient != null){
            webSocketClient.close();
        }
        webSocketClient=null;
        if(webSocketClient==null){
            SharedPreferencesFile  mSharedPrefer = SharedPreferencesFile.newInstance(MyContext, SharedPreferencesFile.PREFER_FILE_NAME);
            final String user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
            if(user_id != null) {
                Log.d("ServiceUserId",user_id);
                URI uri = null;
                try {
                    uri = new URI(WsConfig.URL_WEBSOCKET);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    webSocketClient = new WebSocketClient(uri, new Draft_17(), null, 10000) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("MyConnection", "Application Socket Connected");
                            for(String msg : message){
                                webSocketClient.send(msg);
                            }
                            message.clear();
                        }
                        @Override
                        public void onMessage(String s) {
                            int gid_uid[] = MessageGenerator.getMessagGroupId(s);
                            Log.e("GroupID",gid_uid[0] +" "+ MyAppp.getCurrent_group_id());
                            if (MyAppp.getMessageListener() != null && MyAppp.getCurrent_group_id() == gid_uid[0]) {
                                MyAppp.getMessageListener().getMessageFromServer(s);
                            } else if(MyAppp.getMessageListener() != null && MyAppp.getCurrent_group_id() == gid_uid[1]){
                                MyAppp.getMessageListener().getMessageFromServer(s);
                            }else{
                                MessageGenerator.myNotifyMessage(s,MyContext);
                            }

                            if(MyAppp.getNewMessageListener() !=null){
                                MyAppp.getNewMessageListener().getNewMessageInRoom(gid_uid[0]);
                            }else {
                                Toast.makeText(MyContext,"Null Object New Messge Listener", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("MyConnection", "Service Socket Closed");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("MyAppp", "Error Connection");
                        }
                    };
                    webSocketClient.connect();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setNewMessageListener(TwoFragment fragment){
        newMessageListener= fragment;
    }
    public static NewMessageListener getNewMessageListener() {
        return newMessageListener;
    }

}
