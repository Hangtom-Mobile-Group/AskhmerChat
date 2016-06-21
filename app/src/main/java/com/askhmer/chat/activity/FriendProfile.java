package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.askhmer.chat.R;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FriendProfile extends SwipeBackActivity {

    TextView tvfriend_name,tvPhone,tvEmail,tvHomeTown;
    ImageView imgfriend_profile;
    private int friid;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


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
                Intent in = new Intent(FriendProfile.this, Chat.class);
                in.putExtra("friid", friid);
                startActivity(in);
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

       // String url = "http://10.0.3.2:8080/ChatAskhmer/api/friend/viewfriendById/" + friid;
         String url = API.VIEWFRIEND + friid;
         GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("DATA")) {
                                JSONObject object = response.getJSONObject("DATA");
                                tvfriend_name.setText(object.getString("userName"));
                                tvPhone.setText(object.getString("userPhoneNum"));
                                tvEmail.setText(object.getString("userEmail"));
                                tvHomeTown.setText(object.getString("userHometown"));
                               // path ="http://10.0.3.2:8080/ChatAskhmer/resources/upload/file/" + object.getString("userPhoto");
                                path =API.UPLOADFILE + object.getString("userPhoto");
                                Picasso.with(getApplicationContext())
                                        .load(path)
                                        .placeholder(R.drawable.icon_user)
                                        .error(R.drawable.icon_user)
                                        .into(imgfriend_profile);
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
}
