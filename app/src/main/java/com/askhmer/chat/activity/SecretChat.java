package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.SecretChatRecyclerAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

//import com.askhmer.chat.listener.RecyclerItemClickListener;

public class SecretChat extends SwipeBackLib {


    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;
    private EditText edtSearchsecretchat;

    private ArrayList<Friends> friendtList = new ArrayList<>();
    private SecretChatRecyclerAdapter adapter;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;
    private String searchString;

    private Toolbar toolbar;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_chat);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("select a friend");

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
        edtSearchsecretchat = (EditText)findViewById(R.id.edtSearchsecretchat);
        edtSearchsecretchat.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_btn), null);
        // Setup layout manager for mBlogList and column count
        /**
         *
         */
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Bind adapter to recycler
        /***
         *
          */
//        mFriends = new ArrayList<>();
//
//
//        for (int i = 0; i < 15; i++) {
//            Friends item = new Friends();
//            item.setFriName("Friend : " + i);
//            item.setImg(""+R.drawable.ic_people);
//            item.setChatId("chat Id : 000" + i);
//            mFriends.add(item);
//        }
          listFriend();



//        Toast.makeText(SecretChat.this, ""+mFriends, Toast.LENGTH_SHORT).show();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

//           adapter = new SecretChatRecyclerAdapter(friendtList);
//           mRecyclerView.setAdapter(adapter);

        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(SecretChat.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent in = new Intent(SecretChat.this, Chat.class);
                        in.putExtra("Friend_name", friendtList.get(position).getFriName());
                        in.putExtra("friid", friendtList.get(position).getFriId());
                        startActivity(in);
                        Log.d("friend", friendtList.get(position).getFriName());
                        finish();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        edtSearchsecretchat.addTextChangedListener(new TextWatcher() {

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


    }

    private void listFriend() {

        String url = API.LISTFRIEND + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");
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
                        Toast.makeText(getApplicationContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    // CustomDialog.hideProgressDialog();
                    adapter = new SecretChatRecyclerAdapter(friendtList);
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
        searchString = edtSearchsecretchat.getText().toString();
        String url = "http://chat.askhmer.com/api/friend/searchfriend/" + searchString + "/"+ user_id;
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

                    adapter = new SecretChatRecyclerAdapter(friendtList);
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

