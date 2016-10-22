package com.askhmer.chat.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.askhmer.chat.listener.MessageListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Longdy on 9/19/2016.
 */
public class MySocket extends Application {

    private static MySocket mInstance;
    //web socket
    private static MessageListener messageListener;
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
            reSendMessage(message);
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
    public static synchronized MySocket getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public static void  reSendMessage(final String message){
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
                            webSocketClient.send(message);
                        }

                        @Override
                        public void onMessage(String s) {
                            int gid_uid[] = MessageGenerator.getMessagGroupId(s);
                            Log.e("GroupID",gid_uid[0] +" "+MySocket.getCurrent_group_id());
                            if (MySocket.getMessageListener() != null && MySocket.getCurrent_group_id() == gid_uid[0]) {
                                MySocket.getMessageListener().getMessageFromServer(s);
                            } else if(MySocket.getMessageListener() != null && MySocket.getCurrent_group_id() == gid_uid[1]){
                                MySocket.getMessageListener().getMessageFromServer(s);
                            }else{
                                MessageGenerator.myNotifyMessage(s,MyContext);
                            }
                        }
                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("MyConnection", "Service Socket Closed");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("MySocket", "Error Connection");
                        }
                    };
                    webSocketClient.connect();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
