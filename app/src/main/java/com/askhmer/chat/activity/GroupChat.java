package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.GroupChatRecyclerAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class GroupChat extends SwipeBackLib {

    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;
    private List<Friends> friendtList = new ArrayList<>();
    private ArrayList<Friends> listFriend;
    private EditText edtSearchfri;

    private Toolbar toolbar;
    private String groupChatName;
    private  int groupID;
    EditText input1;

    private String data = "";
    private GroupChatRecyclerAdapter adapter;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;
    private String searchString;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("select friends");

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

        edtSearchfri = (EditText)findViewById(R.id.edtSearchfri);
        edtSearchfri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_btn), null);


        // Setup layout manager for mBlogList and column count
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Bind adapter to recycler
        //  mFriends = new ArrayList<>();

        //list item
//        for (int i = 0; i < 15; i++) {
//            Friends item = new Friends();
//            item.setFriName("Friend : " + i);
//            item.setImg(""+R.drawable.ic_people);
//            item.setChatId("chat Id : 000" + i);
//            item.setIsSelected(false);
//            mFriends.add(item);
//        }

        listFriend();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.scrollToPosition(0);

        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        //------------------------------------------------------
//        adapter = new GroupChatRecyclerAdapter(mFriends);
//        mRecyclerView.setAdapter(adapter);


        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(GroupChat.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));




        edtSearchfri.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                adapter.clearData();
                listSearchFriend();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        /*
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Friends fri = new Friends();
                if(fri.isSelected()){
                    findViewById(R.id.action_done).setVisibility(View.VISIBLE);
                    Toast.makeText(GroupChat.this, "Test", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_chat_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_done:
                inputGroupName();
//                Toast.makeText(GroupChat.this, "ssss", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void inputGroupName(){
        LayoutInflater factory = LayoutInflater.from(GroupChat.this);

        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.input_group_chat_name, null);

        input1 = (EditText) textEntryView.findViewById(R.id.et_group_name);

        input1.setText("", TextView.BufferType.EDITABLE);

        selectedDone();


        if(data == ""){
            Toast.makeText(GroupChat.this, "Please select your friends!!!", Toast.LENGTH_SHORT).show();
        }else {


            final AlertDialog.Builder alert = new AlertDialog.Builder(GroupChat.this);

            alert.setIcon(R.drawable.chat_img);
            alert.setTitle("Input your group chat name!!!").setView(textEntryView).setPositiveButton("Save",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton) {


                            /**
                             * block code create group chat
                             */
                            groupChatName = input1.getText().toString();
                            createGroupChat();

                            Log.i("AlertDialog", "TextEntry 1 Entered " + groupChatName);
                            Log.d("data", data);

//                            Intent in = new Intent(GroupChat.this, Chat.class);
//                            in.putExtra("friends",data);
//                            in.putExtra("groupName",groupChatName);
//                            in.putExtra("groupID",groupID);
//                            startActivity(in);
                            finish();

                        }
                    }).setNegativeButton("Cancle",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                        }
                    });
            alert.show();
        }

    }

    public void selectedDone(){

        List<Friends> stList = ((GroupChatRecyclerAdapter) adapter)
                .getmFriendtist();
//        List<Friends> stList = listFriend;
        ArrayList<Integer> listUserId = new ArrayList<Integer>();

        for (int i = 0; i < stList.size(); i++) {
            Friends singleFriend = stList.get(i);
            if (singleFriend.isSelected() == true) {
               // data = data + "\n" + singleFriend.getFriId();\
                listUserId.add(singleFriend.getFriId());
            }

        }
        listUserId.add(Integer.valueOf(user_id.toString()));
        Gson gson = new Gson();
        data = gson.toJson(listUserId);
    }



    /**
     * group chat
     */

    private  void createGroupChat(){
        final String groupname = groupChatName;
        String allid = data;
        String url = API.BASEURL+"message/creategroupchat?roomName="+groupname+"&userId="+allid;
        url = url.replaceAll(" ", "%20");
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("STATUS") == 200) {
                        //JSONObject object = response.getJSONObject("DATA");
                        groupID =  response.getInt("DATA");
                        Toast.makeText(getApplicationContext(),"This is group ID :" + groupID,Toast.LENGTH_LONG).show();

                        Intent in = new Intent(GroupChat.this, Chat.class);
                        in.putExtra("friendsID",data);
                        in.putExtra("groupName",groupChatName);
                        in.putExtra("groupID",groupID);
                        startActivity(in);

                    }
                    else{
                        Toast.makeText(GroupChat.this, "Create group not sucess", Toast.LENGTH_SHORT).show();
                    }}
                catch (JSONException e) {
                    e.printStackTrace();

                } finally {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupChat.this,"error",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(GroupChat.this).addToRequestQueue(objectRequest);

    }

    /**
     * list fri
     */

    private void listFriend() {

        String url = API.LISTFRIEND + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");
                        //list item
                        for (int i = 1; i < jsonArray.length(); i++) {
                            Friends item = new Friends();
                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            item.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                            item.setChatId(jsonArray.getJSONObject(i).getString("userNo"));
                            item.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                            friendtList.add(item);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    // CustomDialog.hideProgressDialog();
                    adapter = new GroupChatRecyclerAdapter(friendtList);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);


    }


    /**
     * list search friend
     */

    private void listSearchFriend() {
        searchString = edtSearchfri.getText().toString();
        String url = API.SEARCHFRIEND + searchString + "/"+ user_id;
        url = url.replaceAll(" ", "%20");
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("RES_DATA")) {
                        JSONArray jsonArray = response.getJSONArray("RES_DATA");
                        //list item
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Friends item = new Friends();
                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            item.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                            item.setChatId(jsonArray.getJSONObject(i).getString("userNo"));
                            item.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                            friendtList.add(item);
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // e.printStackTrace();
                } finally {

                    adapter = new GroupChatRecyclerAdapter(friendtList);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomDialog.hideProgressDialog();
                adapter.clearData();
                listFriend();
                //   Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }

}
