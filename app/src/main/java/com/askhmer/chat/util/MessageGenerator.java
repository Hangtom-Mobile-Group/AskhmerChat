package com.askhmer.chat.util;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lach Phalleak on 9/26/2016.
 */
public class MessageGenerator {
    public static void sendMessageToServer(String message, String userId,String reciever,WebSocketClient client) {
        String json = null;
        ArrayList<String> rec = new ArrayList<>();
        rec.add(reciever);
        JSONArray recievers=new JSONArray(rec);
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("message", message);
            jObj.put("userid", userId);
            jObj.put("reciever", recievers);
            jObj.put("img_profile","");
            jObj.put("date","");
            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.send(json);
    }
}
