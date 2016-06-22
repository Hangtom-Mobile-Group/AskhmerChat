package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat extends SwipeBackActivity {

    // LogCat tag
    private static final String TAG = Chat.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages = new ArrayList<Message>();;

    private ListView listViewMessages;

    private Utils utils;
    private Message item = new Message();

    //web socket
    private WebSocketClient client;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";

    //Toobar
    private Toolbar toolbar;

    private String roomName = null;
    private String userName = null;

    // Client name
    private String friendName = null;
    private int  friid;
    private String msg;
    private  String groupName = null;
    private int groupID;
    String user_id;
    private SharedPreferencesFile mSharedPrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setDragEdge(SwipeBackLayout.DragEdge.TOP);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting the person name from previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            friendName = extras.getString("Friend_name");
            friid = extras.getInt("friid");
            groupName = extras.getString("groupName");
            groupID = extras.getInt("groupID");
        }

        Toast.makeText(getApplicationContext(),"group id love love : "+groupID,Toast.LENGTH_LONG).show();

//
//        Intent i = getIntent();
//        String friends = i.getStringExtra("friends");
//        groupName = i.getStringExtra("groupName");


    //    Toast.makeText(Chat.this, "Start chat with : " + name, Toast.LENGTH_SHORT).show();
        Toast.makeText(Chat.this, "Start chat with group : " + groupName, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(Chat.this, "friend name : " + friends, Toast.LENGTH_SHORT).show();

        if(friendName==""|| friendName==null){
            toolbar.setTitle(groupName);
            roomName = groupName;
            Toast.makeText(Chat.this, "Start chat in group : " + groupName, Toast.LENGTH_SHORT).show();
        }
        if(groupName=="" ||groupName==null){
            toolbar.setTitle(friendName);
            roomName = friendName;
            Toast.makeText(Chat.this, "Start chat with : " + friendName, Toast.LENGTH_SHORT).show();
        }


    //        Toast.makeText(Chat.this, "Start chat with : " + groupName, Toast.LENGTH_SHORT).show();
    //        Toast.makeText(Chat.this, "Start chat with : " + name, Toast.LENGTH_SHORT).show();

        // Change from Navigation menu item image to arrow back image of toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        listHistoryMsg(1, 1);
        Log.d("data",listMessages+"");

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                  msg = inputMsg.getText().toString().trim();

                if (!msg.isEmpty()) {
                    Message item = new Message();
                    item.setMessage(msg);
                    item.setUserName(userName);
                    item.setUserId(1);
                    listMessages.add(item);
                    adapter.notifyDataSetChanged();

                    // Sending message to web socket server
                    sendMessageToServer(utils.getSendMessageJSON(msg));

                    // Clearing the input filed once message was sent
                    inputMsg.setText("");
                    if(groupName=="" ||groupName==null){
                        checkGroupChat();
                    }else {
                       addMessage();
                }

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage(R.string.information_massage_later);
                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
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
                return true;
            }
        });

        /**
         * Creating web socket client. This will have callback methods
         * */
        client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET
                + URLEncoder.encode(roomName)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {

            }

            /**
             * On receiving the message from web socket server
             * */
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);

            }

            @Override
            public void onMessage(byte[] data) {
                Log.d(TAG, String.format("Got binary message! %s",
                        bytesToHex(data)));

                // Message will be in JSON format
                parseMessage(bytesToHex(data));
            }

            /**
             * Called when the connection is terminated
             * */
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);

                showToast(message);

                // clear the session id from shared preferences
                utils.storeSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

                showToast("Error! : " + error);
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
                playBeep();
            }
        });
    }

    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            client.send(message);
        }
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

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = friendName;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                Message m = new Message(fromName, message, 1);

                // Appending the message to chat list
                appendMessage(m);

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }

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

        String url = "http://10.0.3.2:8080/ChatAskhmer/api/message/addfirstmsgpersonalchat/"+ user_id + "/"+ friid + "/"+msg;
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
            params.put("stickerUrl","string");
            params.put("msgDate", "2016-06-10T06:16:27.040Z");
            params.put("msgTime", "2016-06-10T06:16:27.040Z");
            params.put("userName", "string");
            params.put("userProfile","string");


             String url = "http://10.0.3.2:8080/ChatAskhmer/api/message/add_message";
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
//                            Log.d("love", response.toString());
//                            Toast.makeText(Chat.this, "add success :"+ response.toString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Chat.this, "Unsuccessfully Edited !!", Toast.LENGTH_LONG).show();
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
       String url = "http://10.0.3.2:8080/ChatAskhmer/api/chathistory/checkChatRoom/"+ user_id + "/"+ friid;
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            groupID = response.getInt("MESSAGE_ROOM_ID");
                           addMessage();
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
    public void listHistoryMsg(int roomId,  int userId){
        Log.d("list", "method");
        Toast.makeText(Chat.this, "List method", Toast.LENGTH_LONG).show();
        GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, API.LISTMESSAGE + roomId + "/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        Log.d("has ", response.getJSONArray("DATA").toString());
                        List<Message> lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(),Message.class);
                        listMessages.addAll(lsMsg);

                        for (Message msg : lsMsg) {
                            Log.d("usertest", msg.getUserName());
                        }
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


}
