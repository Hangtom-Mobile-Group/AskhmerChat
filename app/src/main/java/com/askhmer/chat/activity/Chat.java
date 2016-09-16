package com.askhmer.chat.activity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.JsonConverter;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.Utils;
import com.askhmer.chat.util.WsConfig;
import com.codebutler.android_websockets.WebSocketClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class Chat extends SwipeBackLib {

    // LogCat tag
    private static final String TAG = Chat.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;
    private List<Message> lsMsg;

    private Utils utils;

    //web socket
    private WebSocketClient client;


    //Toobar
    private Toolbar toolbar;

    private String roomName;

    // Client name
    private String name;
    private int  friid;
    private String msg;
    private  String groupName;
    private int groupID;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;

    private SwipeBackLayout mSwipeBackLayout;

    private String date = currentDateTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mSwipeBackLayout = getSwipeBackLayout();

        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change from Navigation menu item image to arrow back image of toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


        // Getting the person name from previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Friend_name");
            friid = extras.getInt("friid");
            groupName = extras.getString("groupName");
            groupID = extras.getInt("groupID");
        }


        if(groupID == 0){
            checkGroupChat();
        }else listHistoryMsg(groupID, user_id);


        if(groupName != null){
            roomName = groupName;
        }
        if(name != null){
            roomName = name;
        }

        toolbar.setTitle(roomName);

        //Event Menu Item Back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        utils = new Utils(getApplicationContext());

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();

                if (!msg.isEmpty()) {

                    boolean isSelf = true;
                    String imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);;
                    Message m = new Message(user_id, msg, isSelf, imgPro, date);

                    listMessages.add(m);

                    adapter.notifyDataSetChanged();

                    // Sending message to web socket server
                    sendMessageToServer(msg, user_id, friid+"",imgPro, date);

                    // Clearing the input filed once message was sent
                    inputMsg.setText("");
                    addMessage();

//                    if(groupName=="" ||groupName==null){
//                        checkGroupChat();
//                    }else {
//                       addMessage();
//                }

                }
            }
        });

        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.lbl_date_message);
                if (textView.getVisibility() == View.VISIBLE) {
                    Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_down);
                    textView.startAnimation(slide_down);
                    textView.setVisibility(View.GONE);
                } else {
                    // textView.animate().translationY(view.getHeight()).alpha(1.0f).setDuration(300);
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up);
                    textView.startAnimation(slide_up);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });


        listViewMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            int pos;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
/*
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage("Are you sure to delete this message?");
                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        deleteMessage(user_id, listMessages.get(pos).getMsgId());
                        listMessages.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                });

                alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

*/
                return true;
            }
        });

//        Log.e("room",roomName);

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
                Log.d(TAG, String.format("Got binary message! %s",
                        bytesToHex(data)));

                // Message will be in JSON format
                parseMessage(bytesToHex(data));
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
                Log.e(TAG, "Error! : " + error);
            }

        }, null);

        client.connect();
    }

    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
//                playBeep();
            }
        });
    }

    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message , String userId,String reciever, String imgPro, String date) {
        if (client != null && client.isConnected()) {
            String json = null;
            ArrayList<String> rec = new ArrayList<>();
            rec.add(reciever);
            JSONArray recievers=new JSONArray(rec);

            JSONObject jObj = new JSONObject();
            try {
                jObj.put("message", message);
                jObj.put("userid", userId);
                jObj.put("reciever", recievers);
                jObj.put("img_profile",imgPro);
                jObj.put("date", date);

                json = jObj.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            client.send(json);
            Log.e("send",json);
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
/*
            else {
                String message = jObj.getString("message");
                boolean isSelf = true;

                String imgPro = "http://chat.askhmer.com/resources/upload/file/user/868ac24e-ac5c-4885-a097-0196d0b62509.jpg";
                Message m = new Message(userid, message, isSelf, imgPro, date);

                // Appending the message to chat list
//                appendMessage(m);
            }
*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(client != null & client.isConnected()){
            client.disconnect();
        }
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
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

    private String currentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm");
        Date date = new Date();
        return dateFormat.format(date).toUpperCase();
    }

    /**
     * create group chat two*/

    private  void createGroupChat(){

        String url = API.ADDFIRSTMSGPERSONALCHAT+ user_id + "/"+ friid + "/"+msg;

        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("STATUS")) {
                        JSONObject object = response.getJSONObject("DATA");
                    }
                    else{
                        Toast.makeText(Chat.this, "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }}
                catch (JSONException e) {
                    e.printStackTrace();

                } finally {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chat.this,"error",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(Chat.this).addToRequestQueue(objectRequest);

    }

    /**
     * create group chat multi
     */

    /**
     * addMessage
     */
    private void addMessage(){
        JSONObject params;
        try {

            params = new JSONObject();
            params.put("msgId", "");
            params.put("roomId", groupID);
            params.put("userId", user_id);
            params.put("message", msg);
            params.put("stickerUrl","");
            params.put("msgDate", "");
            params.put("msgTime", "");
            params.put("userName", roomName);
            params.put("userProfile","");


             String url = API.ADDMESSAGE;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
//                            Log.d("love", response.toString());
//                            Toast.makeText(Chat.this, "add success :"+ response.toString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Chat.this, "Unsuccessfully add message !!", Toast.LENGTH_LONG).show();
                    } finally {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
        } catch (JSONException e) {
            Toast.makeText(Chat.this, "ERROR_MESSAGE_JSONOBJECT" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Chat.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * check group chat
     */
    private  void checkGroupChat(){
       String url = API.CHECKCHATROOM+ user_id + "/"+ friid;
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            groupID = response.getInt("MESSAGE_ROOM_ID");
                            listHistoryMsg(groupID, user_id);
                          //   addMessage();
                        }
                        else{
                             createGroupChat();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } finally {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chat.this,"error",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(Chat.this).addToRequestQueue(objectRequest);

    }

    /***
     *
     * @param roomId
     * @param userId
     *
     */
    public void listHistoryMsg(int roomId,  String userId){
        GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, API.LISTMESSAGE + roomId + "/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(),Message.class);
                        listMessages.addAll(lsMsg);

                        for (Message msg : lsMsg) {
                            Log.e("usertest", msg.getUserName());
                            Log.e("userid", msg.getUserId());
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorCustom", error.getMessage());
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
    }

    /***
     * delete message
     */

    public void deleteMessage(String userId, int msgId){
        JSONObject params;
        Toast.makeText(Chat.this, "Deleted method"+userId+" "+msgId, Toast.LENGTH_SHORT).show();
        try {
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, API.DELETEMESSAGE+userId+"/"+msgId, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
//                            Log.d("love", response.toString());
//                            Toast.makeText(Chat.this, "add success :"+ response.toString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Chat.this, "Unsuccessfully Delete !!", Toast.LENGTH_LONG).show();
                    } finally {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Chat.this, "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(Chat.this).addToRequestQueue(jsonRequest);
        }
        catch (Exception e) {
            Toast.makeText(Chat.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
