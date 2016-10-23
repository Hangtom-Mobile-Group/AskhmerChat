package com.askhmer.chat.util;

import android.content.Context;

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

    public static void myNotifyMessage(String msg, Context context){
        //    Log.e("ReceiveMessage",msg);
        JSONObject jsonObject=null;
        String message="";
        int userid=0;
        int groupid=0;
        String username="";
        String image_url="";
        ArrayList<Integer> rc=new ArrayList<Integer>();
        try {
            jsonObject=new JSONObject(msg);
            JSONArray recievers=jsonObject.getJSONArray("reciever");
            for(int i=0;i < recievers.length();i++){
                rc.add(recievers.getInt(i));
            }
            message=jsonObject.getString("message");
            userid=jsonObject.getInt("userid");
            groupid=jsonObject.getInt("groupid");
            username=jsonObject.getString("username");
            image_url=jsonObject.getString("img_profile");
        }catch (Exception e)
        {

        }
        rc.add(userid);
        String reciever = rc.toString().replaceAll("[ ]","");
        // Log.e("MyReceiver",reciever);
        String []param={image_url};
        new NotificationGenerator(context,message,username,groupid,userid,image_url,reciever).execute(param);
    }

    public static int[] getMessagGroupId(String msg){
        JSONObject jsonObject=null;
        int gid_uid[]=new int[2];
        try {
            jsonObject=new JSONObject(msg);
            gid_uid[0]=jsonObject.getInt("groupid");
            gid_uid[1]=jsonObject.getInt("userid");

        }catch (Exception e)
        {
            return null;
        }
        return gid_uid;
    }
}
