package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class FriendProfile extends SwipeBackLib {

    TextView tvfriend_name,tvPhone,tvEmail,tvHomeTown;
    ImageView imgfriend_profile;
    private int friid;
    private String path;

    private int groupID;
    private String friend_name;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            friid = extras.getInt("friid");
        }
        tvfriend_name = (TextView) findViewById(R.id.friend_name);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvHomeTown = (TextView) findViewById(R.id.tvHomeTown);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(FriendProfile.this, Chat.class);
//                in.putExtra("friid", friid);
//                startActivity(in);

                checkGroupID();
            }
        });



        imgfriend_profile = (ImageView)findViewById(R.id.friend_profile);
        imgfriend_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FriendProfile.this, ViewPhoto.class);
                in.putExtra("image", path);
                startActivity(in);

            }
        });

         String url = API.VIEWFRIEND + friid;
         GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("DATA")) {
                                JSONObject object = response.getJSONObject("DATA");
                                friend_name = object.getString("userName");
                                tvfriend_name.setText(object.getString("userName"));
                                tvPhone.setText(object.getString("userPhoneNum"));
                                tvEmail.setText(object.getString("userEmail"));
                                tvHomeTown.setText(object.getString("userHometown"));
                                //path =API.UPLOADFILE + object.getString("userPhoto");

                                String path1= object.getString("userPhoto");
                                boolean found = path1.contains("facebook");
                                Log.d("found", "Return : " + found);
                                String imgPaht1 = API.UPLOADFILE +path1;
                                String imgPaht2 = path1;
                                if( found == false){
                                    path = imgPaht1;
                                    Picasso.with(getApplicationContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(imgfriend_profile);
                                }else{
                                    path = imgPaht2;
                                    Picasso.with(getApplicationContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(imgfriend_profile);
                                }
                            }
                            else{
                                Toast.makeText(FriendProfile.this, "No Friend Found !", Toast.LENGTH_SHORT).show();
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();

                        } finally {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendProfile.this,"error",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(FriendProfile.this).addToRequestQueue(objectRequest);
    }





    public void checkGroupID(){

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        String url = API.CHECKCHATROOM+ user_id + "/"+ friid;
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            groupID = response.getInt("MESSAGE_ROOM_ID");
                            Log.e("group id", groupID + "");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } finally {
                    Intent in = new Intent(FriendProfile.this, Chat.class);
                    in.putExtra("Friend_name",friend_name);
                    in.putExtra("friid", friid);
                    in.putExtra("groupID", groupID);
                    startActivity(in);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
                Log.e("error","error");
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(objectRequest);

    }
}
