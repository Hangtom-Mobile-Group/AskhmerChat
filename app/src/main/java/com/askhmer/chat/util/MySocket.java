package com.askhmer.chat.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.askhmer.chat.activity.CameraActivity;
import com.askhmer.chat.listener.MessageListener;
import com.askhmer.chat.listener.SaveImageListener;

import org.java_websocket.client.WebSocketClient;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Longdy on 9/19/2016.
 */
public class MySocket extends Application {

    private static MySocket mInstance;
    //web socket
    private static MessageListener messageListener;
    private static WebSocketClient webSocketClient;
    private static int current_group_id;

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
    public static void sendMessage(String message){
        try{
            webSocketClient.send(message);
        }catch(Exception e){
            e.printStackTrace();
            Log.i("Send Message","Send Failed");
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



}
