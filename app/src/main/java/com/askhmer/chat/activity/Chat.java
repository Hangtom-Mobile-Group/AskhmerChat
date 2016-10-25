package com.askhmer.chat.activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.listener.AddStrickerToChat;
import com.askhmer.chat.listener.MessageListener;
import com.askhmer.chat.listener.SendAudioListener;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.BitmapEfficient;
import com.askhmer.chat.util.JsonConverter;
import com.askhmer.chat.util.MultipartUtility;
import com.askhmer.chat.util.MyAppp;
import com.askhmer.chat.util.ResizeWidthAnimator;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.ToolBarUtils;
import com.askhmer.chat.util.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class Chat extends SwipeBackLib implements MessageListener, SwipeRefreshLayout.OnRefreshListener, AddStrickerToChat,SendAudioListener {

    private static final int SHORT_DURATION_MS = 1500;
    private static final int LONG_DURATION_MS = 2750;

    //--todo send image
    private Bitmap bitmap;
    private String imgUrl;
    private int imageHeight;
    private int imageWidth;
    private String imagePath;
    private String[] uploadImgPath;
    private String picturePath = null;
    private static int RESULT_LOAD_IMAGE_PROFILE = 1;
    private boolean isChangeProfileImage;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    String img_path_send;

    //--todo for pagination
    private int current_page = 1;
    private int total_row = -1;
    private int total_page= -1;
    private int row_per_page = 10;

    private int count = 1;
    private int fix_total_row;
    private int fix_total_page;
    private int oneTime = 0;


    // LogCat tag
    private static final String TAG = Chat.class.getSimpleName();

    private Button btnSend,btn_retry;
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
    private String user_id, friendImageUrl;
    private String user_name;
    private boolean isGroup;
    private SharedPreferencesFile mSharedPrefer;
    private ImageView btnStker, btnWord, btnVoice,btnCamera,btnGallery;
    //private LinearLayout linearLayout, linearLayoutChatWord,
    private LinearLayout rootLayout;
    private LinearLayout linearLayoutVoice;
    private LinearLayout linearLayout, linearLayoutChatWord;
    private LinearLayout  layout_no_connection,llMsgCompose;
    private String allFirendId;
    //---  refresh
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    //-----refresh
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_cus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        user_name= mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERNAME);
        linearLayout = (LinearLayout) findViewById(R.id.show_item);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        linearLayoutChatWord = (LinearLayout) findViewById(R.id.layout_chat_word);
        linearLayoutVoice = (LinearLayout) findViewById(R.id.show_item_voice);


        layout_no_connection = (LinearLayout) findViewById(R.id.layout_no_connection);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        llMsgCompose = (LinearLayout) findViewById(R.id.llMsgCompose);


        // Getting the person name from previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Friend_name");
            friid = extras.getInt("friid");
            groupName = extras.getString("groupName");
            groupID = extras.getInt("groupID");
            friendImageUrl = extras.getString("friend_image_url");
            allFirendId = extras.getString("friendsID");
            isGroup=extras.getBoolean("isGroup");
        }

        if(groupID == 0){
            checkGroupChat();
        }else {
            listmesagebypage();
        }


       // Log.e("GroupnameFriendName",groupName +" & "+name);
        if(isGroup){
            roomName = groupName;
        }else{
            roomName = name;
        }
       //  Log.e("Home",roomName);

        //--todo update message seen
        updateSeen();


        /*toolbar.setTitle(roomName);*/
        TextView txtRoomName = (TextView) findViewById(R.id.txt_room_name);
        ImageView imageFriend = (ImageView) findViewById(R.id.layout_round);

        txtRoomName.setText(roomName);
        String imgPath = friendImageUrl;


        String imgPaht1 = API.UPLOADFILE +friendImageUrl;

        try{
            if(imgPath.contains("https://graph.facebook.com")){
                Picasso.with(this).load(imgPath).error(R.drawable.groupchat).into(imageFriend);
            }else if(imgPath.contains("http://chat.askhmer.com/resources/upload/file")){
                Picasso.with(this).load(imgPath).error(R.drawable.groupchat).into(imageFriend);
            }
            else{
                Picasso.with(this).load(imgPaht1).error(R.drawable.groupchat).into(imageFriend);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        //Event Menu Item Back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);


        utils = new Utils(getApplicationContext());

        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_chat);
        // sets the colors used in the refresh animation
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_bright, R.color.green_light,
                R.color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        btn_retry = (Button) findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGroupChat();
            }
        });


        if(inputMsg.isFocused()){
            btnSend.setBackgroundResource(R.drawable.btn_send_chat);
        }else{
            btnSend.setBackgroundResource(R.drawable.btn_send_micro);
        }

        inputMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    msg = inputMsg.getText().toString().trim();
                    if (!msg.isEmpty()) {
                        boolean isSelf = true;
                        String imgPro = "";

                        if (mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null) {
                            imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
                        }

                        String imgResource="";
                        if(imgPro.contains("graph.facebook.com") ||
                                imgPro.contains("http://chat.askhmer.com/resources/upload/file/")){
                            imgResource=imgPro;

                        }else{
                            imgResource= "http://chat.askhmer.com/resources/upload/file/"+imgPro;
                        }

                       // Log.e("ImageAC",imgResource);

                        Message m = new Message(user_id, msg, isSelf, imgResource, date, null);
                        listMessages.add(m);
                        // Log.e("img AC", "" + imgPro);
                        adapter.notifyDataSetChanged();

                    //insert message to server
                    addMessage();
                    // Sending message to web socket server
                    // Log.e("AllFirendId",allFirendId);
                    String sender_name="";
                    if(isGroup){
                        sender_name =groupName;
                    }else{
                        sender_name=user_name;
                    }
                    if (!allFirendId.equals("null")) {
                        sendMessageToServer(msg, user_id, allFirendId + "", imgResource, date, groupID + "", sender_name,isGroup);
                    } else {
                        Log.e("friid2", " " + friid);
                        sendMessageToServer(msg, user_id, friid + "", imgResource, date, groupID + "", sender_name,isGroup);
                    }
                    // Clearing the input filed once message was sent
                    inputMsg.setText("");
//                    if(groupName=="" ||groupName==null){
//                        checkGroupChat();
//                    }else {
//                       addMessage();
//                }
                }else{
                    hideKeyBoard();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.show_item_voice, new VoiceChat(adapter))
                            .commit();

                    linearLayoutVoice.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayoutChatWord.setVisibility(View.GONE);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage("Are you sure to delete this message?");
                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                       deleteMessage(user_id, listMessages.get(pos).getMsgId(),groupID,pos);
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

        btnStker = (ImageView) findViewById(R.id.btn_stker);
        btnStker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }
                hideKeyBoard();
                if (oneTime == 0) {
                    oneTime += 1;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.show_item, new Sticker())
                            .commit();
                }
                linearLayout.setVisibility(View.VISIBLE);
                linearLayoutChatWord.setVisibility(View.GONE);
                linearLayoutVoice.setVisibility(View.GONE);
            }
        });

        btnWord = (ImageView) findViewById(R.id.btn_word);
        btnWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }
                inputMsg.requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        linearLayout.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);

                linearLayoutChatWord.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutVoice.setVisibility(View.GONE);
            }
        });


        btnCamera = (ImageView) findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }

                //--intent to camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_LOAD_IMAGE_PROFILE);


                //---intent for get path of video
//                private static final int REQUEST_TAKE_GALLERY_VIDEO = 1;
//                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);


                //--intent open camera video
//                final int VIDEO = 1;
//                Intent intent = new Intent("android.media.action.VIDEO_CAMERA");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, VIDEO );


            }
        });

        btnGallery = (ImageView) findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }
                //--intent to gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_PROFILE);
            }
        });



//        Log.e("room",roomName);
        btnVoice = (ImageView) findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = inputMsg.getText().toString().trim();
                if (!msg.isEmpty()) {
                    btnSend.setBackgroundResource(R.drawable.btn_send_chat);
                }else{
                    btnSend.setBackgroundResource(R.drawable.btn_send_micro);
                }
                hideKeyBoard();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.show_item_voice, new VoiceChat(adapter))
                        .commit();

                linearLayoutVoice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutChatWord.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (linearLayout.getVisibility() == View.GONE && linearLayoutVoice.getVisibility() == View.GONE) {
            finish();
        }
        linearLayout.setVisibility(View.GONE);
        linearLayoutVoice.setVisibility(View.GONE);
        linearLayoutChatWord.setVisibility(View.VISIBLE);

        Log.e("chat","onBackPressed");
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
    private void sendMessageToServer(String message , String userId,String reciever, String imgPro,
                                     String date,String groupid,String username,boolean isGroup) {
        if (MyAppp.getWebSocketClient() != null) {
            String json = null;
            String recieverChange1=reciever.replace("[","");
            String recieverChange2=recieverChange1.replace("]", "");
            String allId[]=recieverChange2.split(",");
            ArrayList<String> rec = new ArrayList<String>();
            for(String id :allId){
                rec.add(id);
            }
            rec.remove(user_id);
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
                jObj.put("isGroup",isGroup);

                json = jObj.toString();
              //  Log.e("SendMessage",": "+json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           MyAppp.sendMessage(json);
            Log.e("btnsend",json);
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
                Message m = new Message(userid, message, isSelf, imgPro, date,null);
                // Appending the message to chat list
                appendMessage(m);
            }
//            else{
//               if(jObj.getString("message").contains("http://chat.askhmer.com/resources/upload/file/images")) {
//                    //---self from socket
//                    String message = jObj.getString("message");
//                    String imgPro = jObj.getString("img_profile");
//                    String date = jObj.getString("date");
//                    boolean isSelf = true;
//
//                    Log.e("img_profile", imgPro + ", " + date);
//                    Message m = new Message(userid, message, isSelf, imgPro, date,null);
//                    // Appending the message to chat list
//                   adapter.notifyDataSetChanged();
//                   appendMessage(m);
//
//                   showToast("New message : " + message);
//                }
//
//            }

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
     *
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
     */

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
            
            if(allFirendId !=null){
                params.put("receivers",allFirendId);
            }else{
                String group_member=user_id+","+friid;
                params.put("receivers",group_member);
            }

            if(isGroup){
                params.put("groupName",groupName);
                params.put("group",isGroup);
            }else{
                params.put("groupName","");
                params.put("group",isGroup);
            }

         //   Log.e("SendToServer",params.toString());

            String url = API.ADDMESSAGE;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
                              // listMessages.get(listMessages.size()-1).setMsgId(response.getInt("KEY"));
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
                } finally {
                    layout_no_connection.setVisibility(View.GONE);
                    listViewMessages.setVisibility(View.VISIBLE);
                    llMsgCompose.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listViewMessages.setVisibility(View.GONE);
                llMsgCompose.setVisibility(View.GONE);
                layout_no_connection.setVisibility(View.VISIBLE);
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

                        Log.d("onTpmList", "" + tmplistMessages);

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

                            int index = listViewMessages.getFirstVisiblePosition();
                            View v = listViewMessages.getChildAt(0);
                            int top = (v == null) ? 0 : (v.getTop() - listViewMessages.getPaddingTop());
                            listViewMessages.setSelectionFromTop(5, top);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
    }




    //---todo end list message

    /***
     * delete message
     */

    public void deleteMessage(String userId, int msgId, int groupId, final int pos){
        if(msgId==0){
            Toast.makeText(Chat.this, "You cannot delete current message!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject params;
//        Toast.makeText(Chat.this, "Deleted method" + userId + " " + msgId, Toast.LENGTH_SHORT).show();
        try {
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST,
                                                                  API.DELETEMESSAGE+userId+"/"+msgId+"/"+groupId,
                                                                  new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
//                            Log.d("love", response.toString());
                           Toast.makeText(Chat.this,"Delete message success!!!", Toast.LENGTH_LONG).show();
                            listMessages.remove(pos);
                            adapter.notifyDataSetChanged();
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
        //Log.d("FromSocket",message.toString());
        parseMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MyAppp.getWebSocketClient()==null){

        }
        MyAppp.setMessageListener(this);
        MyAppp.setCurrent_group_id(groupID);
    }

    @Override
    protected void onDestroy() {
        //--todo update message seen
        updateSeen();
        Log.e("chat", "onDestroy");
        MyAppp.setCurrent_group_id(0);
        MyAppp.setMessageListener(null);
        super.onDestroy();
        adapter.stopMedia();

        /* Delete all files */
        boolean isDeleted = false;
        isDeleted = deleteAllFilesInFolder(new File(Environment.getExternalStorageDirectory()+"/YsKMttBCM8McMedayiChat"));
        Log.e("isDeleteFile", "" + isDeleted);

    }
    //---todo update user and roomid to table seen
    public void updateSeen() {
        try {
            mSharedPrefer = SharedPreferencesFile.newInstance(Chat.this, SharedPreferencesFile.PREFER_FILE_NAME);
            user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

            String url ="http://chat.askhmer.com/api/seen/seentotruestatus/"+groupID+"/"+user_id;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.PUT, url,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("STATUS")==200) {
                                    Log.d("updateSeen", response.toString());
                                }
                            } catch (JSONException e) {
                                Toast.makeText(Chat.this, "Unsuccessfully Updated !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Chat.this, "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(Chat.this).addToRequestQueue(jsonRequest);
        } catch (Exception e) {
            Toast.makeText(Chat.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //-----------------------------------------


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
            adapter.notifyDataSetChanged();
           // Toast.makeText(getApplicationContext(), "Now we get new list !!", Toast.LENGTH_LONG).show();

        } else {
            final Snackbar snack = Snackbar.make(listViewMessages, "NO MORE DATA", Snackbar.LENGTH_LONG);
            View view = snack.getView();
            snack.getView().setVisibility(View.INVISIBLE);

            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            int marginTop = ToolBarUtils.getToolbarHeight(Chat.this);
            params.setMargins(0, marginTop , 0, 0);
            view.setLayoutParams(params);
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            view.setAlpha(0.6f);
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);

            snack.setCallback(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar snackbar) {
                    super.onShown(snackbar);

                    snackbar.getView().setVisibility(View.VISIBLE);
                }

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    snack.getView().setVisibility(View.GONE);
                }
            });
            snack.show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    snack.getView().setVisibility(View.GONE);
                }
            }, LONG_DURATION_MS);
        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        refreshList();
        handler.post(refreshing);
        swipeRefreshLayout.setRefreshing(false);

    }


    private boolean isRefreshing(){
        return swipeRefreshLayout.isRefreshing();
    }

    private final Runnable refreshing = new Runnable(){
        public void run(){
            try {
                // TODO : isRefreshing should be attached to your data request status
                if(isRefreshing()){
                    // re run the verification after 1 second
                    handler.postDelayed(this, 1000);
                }else{
                    // stop the animation after the data is fully loaded
                    swipeRefreshLayout.setRefreshing(false);
                    // TODO : update your list with the new data
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE_PROFILE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //------------------------
            String imgProfile = "";

            if (mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null) {
                imgProfile = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
            }
//            Toast.makeText(Chat.this, "This is URI = "+selectedImage, Toast.LENGTH_LONG).show();
            Message m = new Message(user_id, null, true, imgProfile, date,selectedImage);
            listMessages.add(m);
            Log.e("img AC", "" + imgProfile);

            adapter.notifyDataSetChanged();
            //-----------------------

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            imageHeight = options.outHeight;
            imageWidth = options.outWidth;

            int rotateImage = getCameraPhotoOrientation(Chat.this, selectedImage, picturePath);

            if (rotateImage == 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(0);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, imageWidth, imageHeight, true);
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } else if (rotateImage == 90) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, imageWidth, imageHeight, true);
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } else if (rotateImage == 180) {
                Matrix matrix = new Matrix();
                matrix.postRotate(180);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, imageWidth, imageHeight, true);
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } else if (rotateImage == 270) {
                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, imageWidth, imageHeight, true);
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            }


//            if (imageHeight > 400 && imageWidth > 400) {
//                bitmap = BitmapEfficient.decodeSampledBitmapFromFile(picturePath, 400, 400);
//            } else {
//                bitmap = BitmapFactory.decodeFile(picturePath);
//            }
            // ivsend.setImageBitmap(bitmap);
            isChangeProfileImage = true;

            //----upload image
            if (isChangeProfileImage) {
                new UploadTask().execute(picturePath);
            }
            //----upload image
        }
    }

    // upload image process background
    private class UploadTask extends AsyncTask<String, Void, Void> {
        String url = API.UPLOADIMAGE;
        String charset = "UTF-8";
        String responseContent = null;
        File file = null;

        @Override
        protected Void doInBackground(String... params) {
            sendFileToServer(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
            file = BitmapEfficient.persistImage(bitmap, getApplicationContext());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (responseContent != null) {
                try {
                    JSONObject object = new JSONObject(responseContent);
                    if (object.getBoolean("STATUS") == true) {
                        imgUrl = object.getString("IMG");
                        uploadImgPath = imgUrl.split("file/");
                        imagePath = uploadImgPath[1];
                        Log.i("send_image", "http://chat.askhmer.com/resources/upload/file/" + imagePath);
                        img_path_send ="http://chat.askhmer.com/resources/upload/file/" + imagePath;
//                        Toast.makeText(Chat.this, "Change Successfully !", Toast.LENGTH_SHORT).show();
                        //--todo show image and send to server and adapter
                        showImage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //.makeText(Chat.this, "Uploaded Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // upload large file size
        public void sendFileToServer(String filePath) {
            try {
                MultipartUtility multipart = new MultipartUtility(url, charset);
                multipart.addFilePart("image", file);
                List<String> response = multipart.finish();
                Log.e("UploadProcess",response.toString());
                for (String line : response) {
                    if (line != null) {
                        responseContent = line;
                        break;
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

    }

    //---send send image

    public void showImage(){

        msg = img_path_send;
        boolean isSelf = true;
        String imgPro = "";

        if (mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null) {
            imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
        }

        Message m = new Message(user_id, msg, isSelf, imgPro, date,null);
        //  listMessages.add(m);

        Log.e("img AC", "" + imgPro);
        adapter.notifyDataSetChanged();
        //insert message to server
        addMessage();
        // Sending message to web socket server
        sendMessageToServer(msg, user_id, friid + "", imgPro, date, groupID + "", user_name,isGroup);

    }



    //---TODO method getCameraPhotoOrientation

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    //----todo end getCameraPhotoOrientation



    //--todo end send photo

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void addStrickerToConversation(String stkerUrl) {
        String imgPro = null;
        boolean isSelf = true;
        String resultStkerSend = "http://chat.askhmer.com/resources/upload/file/sticker/" + stkerUrl;
        msg = resultStkerSend;
        if(mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null){
            imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
        }

        Message m = new Message(user_id, msg, isSelf, imgPro, date, null);

        listMessages.add(m);
        Log.e("img AC", "" + imgPro);

        adapter.notifyDataSetChanged();

        //insert message to server
        addMessage();
        // Sending message to web socket server
        String sender_name="";
        if(isGroup){
            sender_name = groupName;
        }else{
            sender_name=user_name;
        }
        if (allFirendId != null) {
            sendMessageToServer(msg, user_id, allFirendId + "", imgPro, date, groupID + "", sender_name,isGroup);
        }else{
            sendMessageToServer(msg, user_id, friid + "", imgPro, date, groupID + "", sender_name,isGroup);
        }

        // Clearing the input filed once message was sent
        inputMsg.setText("");
    }


    /*
        Send Audio Response Block
     */
    @Override
    public void sendAudio(String audio) {
        if (audio != null) {
            JSONObject jsonAudio = null;
            try {
                jsonAudio = new JSONObject(audio);
                if (jsonAudio.getBoolean("STATUS") == true) {
                    msg = "http://chat.askhmer.com"+jsonAudio.getString("IMG");

                    boolean isSelf = true;
                    String imgPro = "";
                    if (mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH) != null) {
                        imgPro = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);
                    }

                    Message m = new Message(user_id, msg, isSelf, imgPro, date,null);
                    listMessages.add(m);

                    adapter.notifyDataSetChanged();
                    addMessage();
                    String sender_name="";
                    if(isGroup){
                        sender_name = groupName;
                    }else{
                        sender_name=user_name;
                    }
                    if(allFirendId !=null){
                        sendMessageToServer(msg, user_id, allFirendId + "", imgPro, date, groupID + "", sender_name,isGroup);
                    }else{
                        sendMessageToServer(msg, user_id, friid + "", imgPro, date, groupID + "", sender_name,isGroup);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("onSendAudio","Failed to send audio.");
            Toast.makeText(Chat.this, "Failed to send audio.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setAudioTime(final TextView audioTime, final String timeStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                audioTime.setText(timeStr);
            }
        });
    }

    @Override
    public void changeImageButton(ImageButton imageButton,int r1,int r2) {
        if(imageButton !=null){
            int resource= (int) imageButton.getTag();
            if(resource== R.drawable.playbuttonleft || resource==R.drawable.stopbuttonleft){
                imageButton.setImageResource(r1);
                imageButton.setTag(r1);
            }else if(resource==R.drawable.playbuttonright || resource==R.drawable.stopbuttonright){
                imageButton.setImageResource(r2);
                imageButton.setTag(r2);
            }
        }
    }

    @Override
    public void setAudioStatusTextView(final TextView textView, final String text) {
        if(textView !=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(text);
                }
            });
        }
    }

    @Override
    public void setCoverLayoutWidth(final RelativeLayout audioLayout, final LinearLayout coverLayout, final long duration, final int width) {
        if(audioLayout ==null) {
            return;
        }
            runOnUiThread(new Runnable() {
                int mw = 0;

                @Override
                public void run() {
                    if (width == -1) {
                        mw = audioLayout.getWidth();
                    } else {
                        mw = 0;
                    }
                    ResizeWidthAnimator anim = new ResizeWidthAnimator(coverLayout, mw);
                    anim.setDuration(duration);
                    coverLayout.startAnimation(anim);
                }
            });

    }

    @Override
    public void longItemClick(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
        alertDialogBuilder.setTitle(R.string.confirmation);
        alertDialogBuilder.setMessage("Are you sure to delete this message?");
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteMessage(user_id, listMessages.get(pos).getMsgId(),groupID,pos);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean deleteAllFilesInFolder(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteAllFilesInFolder(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }





}
