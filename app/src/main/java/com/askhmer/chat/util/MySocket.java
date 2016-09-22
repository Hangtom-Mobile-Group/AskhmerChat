package com.askhmer.chat.util;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.askhmer.chat.model.Message;
import com.codebutler.android_websockets.WebSocketClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Longdy on 9/19/2016.
 */
public class MySocket extends Application {

    //web socket
    private WebSocketClient client;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;

    private Utils utils;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Application", "OnConfiguration");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Application", "OnCreate");


        utils = new Utils(getApplicationContext());
        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

        if(user_id !=null){
            /**
             * Creating web socket client. This will have callback methods
             * */
            client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET), new WebSocketClient.Listener() {
                @Override
                public void onConnect() {
                    Log.e("error","on connect");
                    sendMessageToServer("", user_id, "",true);
                }


                /***
                 *  On receiving the message from web socket server
                 */

                @Override
                public void onMessage(String message) {
                    Log.e("onMessage", String.format("Got string message! %s", message));

                    parseMessage(message);

                }

                @Override
                public void onMessage(byte[] data) {
                    Log.d("Socket", String.format("Got binary message! %s",
                            bytesToHex(data)));

                    // Message will be in JSON format
//                    parseMessage(bytesToHex(data));
                }

                /***
                 * Called when the connection is terminated
                 */

                @Override
                public void onDisconnect(int code, String reason) {

                    String message = String.format(Locale.US,
                            "Disconnected! Code: %d Reason: %s", code, reason);

//                showToast(message);

                    // clear the session id from shared preferences
                    utils.storeSessionId(null);
                }

                @Override
                public void onError(Exception error) {
                    Log.e("Socket", "Error! : " + error);
                }

            }, null);

            client.connect();

        }


    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("Application", "OnLowMemory");
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("Application", "OnTerninate");
        if(client != null & client.isConnected()){
            client.disconnect();
        }
    }

    private void sendMessageToServer(String message, String userId,String reciever, boolean test) {
        String json = null;
        ArrayList<String> rec = new ArrayList<>();
        rec.add(reciever);
        JSONArray recievers=new JSONArray(rec);

        JSONObject jObj = new JSONObject();
        try {
            jObj.put("message", message);
            jObj.put("userid", userId);
            jObj.put("reciever", recievers);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.send(json);
    }

    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     * */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            String userid = jObj.getString("senderid");

            if(!userid.equals(user_id)){
                String message = jObj.getString("message");
                String imgPro = jObj.getString("img_profile");
                String date = jObj.getString("date");
                boolean isSelf = false;

                Log.e("img_profile",imgPro+", "+date);
                Message m = new Message(userid, message, isSelf, imgPro, date);
                // Appending the message to chat list
                appendMessage(m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        Activity activity = new Activity();
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //List Message
//                listMessages.add(m);

                //Refresh Adapter
//                adapter.notifyDataSetChanged();

                // Playing device's notification
//                playBeep();
            }
        });
    }


    /**
     * Plays device's default notification sound
     * */
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

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
