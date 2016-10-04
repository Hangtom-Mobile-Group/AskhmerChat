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
import android.widget.AbsListView;
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
import com.askhmer.chat.listener.MessageListener;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.JsonConverter;
import com.askhmer.chat.util.MySocket;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class Chat extends SwipeBackLib implements MessageListener{



    private int current_page = 1;
    private int total_row = -1;
    private int total_page= -1;
    private int row_per_page = 10;

    private int count = 1;
    private int fix_total_row;
    private int fix_total_page;


    // LogCat tag
    private static final String TAG = Chat.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;
    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private List<Message> tmplistMessages;
    private ListView listViewMessages;
    private Utils utils;
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
    private String user_name;
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
        user_name= mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERNAME);


        // Getting the person name from previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Friend_name");
            friid = extras.getInt("friid");
            groupName = extras.getString("groupName");
            groupID = extras.getInt("groupID");
        }

        Log.i("Group Id",groupID+"");

        if(groupID == 0){
            checkGroupChat();
            Toast.makeText(Chat.this, "group id :"+groupID, Toast.LENGTH_SHORT).show();
        }else {
            //listHistoryMsg(groupID, user_id);
            listmesagebypage();
            //listHistoryMsg(groupID,user_id,current_page,total_row,total_page,row_per_page);
            Toast.makeText(Chat.this, "list :"+groupID, Toast.LENGTH_SHORT).show();
        }


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

        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);



        //----todo scroll up


        listViewMessages.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.i("SCROLLING DOWN", "TRUE");
                   // Toast.makeText(Chat.this, "SCROLLING DOWN", Toast.LENGTH_SHORT).show();
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {

                    refreshList();
                    Log.i("SCROLLING UP", "TRUE");
                   // Toast.makeText(Chat.this, "SCROLLING UP", Toast.LENGTH_SHORT).show();
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });

        //----todo scroll up


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    boolean isSelf = true;
                    String imgPro = "";

                    if(mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null){
                        imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
                    }

                    Message m = new Message(user_id, msg, isSelf, imgPro, date);

                    listMessages.add(m);
                    Log.e("img AC", "" + imgPro);

                    adapter.notifyDataSetChanged();

                    //insert message to server
                    addMessage();
                    // Sending message to web socket server
                        sendMessageToServer(msg, user_id, friid + "", imgPro, date,groupID+"",user_name);

                    // Clearing the input filed once message was sent
                    inputMsg.setText("");

//                    if(groupName=="" ||groupName==null){
//                        checkGroupChat();
//                    }else {
//                       addMessage();
//                }

                }
            }
        });
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
            }
        });
    }

    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message , String userId,String reciever, String imgPro, String date,String groupid,String username) {
        if (MySocket.getWebSocketClient() != null) {
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
                jObj.put("groupid",groupid);
                jObj.put("username",username);


                json = jObj.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MySocket.sendMessage(json);
            Log.e("send",json);
        }else{
            Log.i("Null Websocket","NUll");
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

            String userid = jObj.getString("userid");

            if(!userid.equals(user_id)){
                String message = jObj.getString("message");
                String imgPro = jObj.getString("img_profile");
                String date = jObj.getString("date");
                boolean isSelf = false;

                Log.e("img_profile",imgPro+", "+date);
                Message m = new Message(userid, message, isSelf, imgPro, date);
                // Appending the message to chat list
                appendMessage(m);
                showToast("New message : " + message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
                Toast.makeText(Chat.this,"There is something wrong.",Toast.LENGTH_LONG).show();
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
                           // listHistoryMsg(groupID, user_id);
                            listHistoryMsg(groupID,user_id,current_page,total_row,total_page,row_per_page);
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
//    public void listHistoryMsg(int roomId,  String userId){
//        GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, API.LISTMESSAGE + roomId + "/" + userId, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.has("DATA")) {
//                        List<Message> lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(),Message.class);
//                        listMessages.addAll(lsMsg);
//                        adapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("errorCustom", error.getMessage());
//            }
//        });
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
//    }


    //--todo list message

    public void listHistoryMsg(int roomId,  String userId, int current_page,int total_row,int total_page,int row_per_page){

        String url = "http://chat.askhmer.com/api/message/list_message_by_roomId/"+roomId+"/"+userId+"/"+current_page+"/"+total_row+"/"+total_page+"/"+row_per_page;
        GsonObjectRequest gson = new GsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        if (count <= 1) {
                            JSONArray jsonArray = response.getJSONArray("DATA");
                            fix_total_row = jsonArray.getJSONObject(0).getInt("totalRow");
                            fix_total_page = jsonArray.getJSONObject(0).getInt("totalPage");
                            count++;
                        }
//                        List<Message> lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(), Message.class);
//                        listMessages.addAll(lsMsg);
//                        adapter.notifyDataSetChanged();

                        Log.d("onTpmList", ""+tmplistMessages);

                        if (tmplistMessages == null) {
                            try {
                                Log.d("First", "True");
                                List<Message> lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(), Message.class);
                                listMessages.addAll(lsMsg);
                                adapter = new MessagesListAdapter(Chat.this, listMessages);
                                adapter.notifyDataSetChanged();
                                listViewMessages.setAdapter(adapter);
                                tmplistMessages = new ArrayList<>();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }else{
                            Log.d("Second", "True");
                            tmplistMessages.clear();
                            List<Message> lsMsg = new JsonConverter().toList(response.getJSONArray("DATA").toString(), Message.class);
                            tmplistMessages.addAll(lsMsg);
                            tmplistMessages.addAll(listMessages);
                            listMessages.clear();
                            listMessages = new ArrayList<>(tmplistMessages);
                            adapter = new MessagesListAdapter(Chat.this, listMessages);
                            adapter.notifyDataSetChanged();
                            listViewMessages.setAdapter(adapter);
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




    //---todo end list message

    /***
     * delete message
     */

    public void deleteMessage(String userId, int msgId){
        JSONObject params;
        Toast.makeText(Chat.this, "Deleted method" + userId + " " + msgId, Toast.LENGTH_SHORT).show();
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

    //append message retrieve from server
    @Override
    public void getMessageFromServer(String message) {
        parseMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySocket.setMessageListener(this);
        MySocket.setCurrent_group_id(groupID);
    }

    @Override
    protected void onDestroy() {
        MySocket.setCurrent_group_id(0);
        MySocket.setMessageListener(null);
        super.onDestroy();
    }


    public void listmesagebypage(){
        if(current_page <= 1){
            Log.i("1", groupID + " " + user_id + " " + current_page + " " + total_row + " " + total_page + " " + row_per_page);
            listHistoryMsg(groupID, user_id, current_page, total_row, total_page, row_per_page);
        }else{
            Log.i("2",groupID+" "+user_id+" "+current_page+" "+fix_total_row+" "+fix_total_page+" "+row_per_page);
            listHistoryMsg(groupID, user_id, current_page, fix_total_row, fix_total_page, row_per_page);
        }
    }

    private void refreshList() {
        if (current_page < fix_total_page) {
           current_page++;
            listmesagebypage();
           // Toast.makeText(getApplicationContext(), "Now we get new list !!", Toast.LENGTH_LONG).show();

        } else {
          //  Toast.makeText(getApplicationContext(), "No data!!", Toast.LENGTH_SHORT).show();
        }
    }
}
