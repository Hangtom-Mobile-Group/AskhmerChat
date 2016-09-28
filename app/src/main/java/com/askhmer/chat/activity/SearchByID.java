package com.askhmer.chat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.SearchUserNameOrNumApt;
import com.askhmer.chat.model.User;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.JsonConverter;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class SearchByID extends SwipeBackLib {

    private SearchUserNameOrNumApt searchUserIdApt = null;
    ArrayList<User> users = new ArrayList<User>();
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_id);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferencesFile mSharedPerfer = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        final String idUserUseApp = mSharedPerfer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

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

        final LinearLayout clearFocus;
        final EditText edtSearchID;
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Animation slide_up;

        clearFocus = (LinearLayout)findViewById(R.id.clearFocus);
        clearFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus.clearFocus();
            }
        });

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        edtSearchID = (EditText)findViewById(R.id.edtSearchID);
        edtSearchID.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.search_btn), null, null, null);
        edtSearchID.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String resultEditText = edtSearchID.getText().toString();

                if (!resultEditText.isEmpty()) {
                    GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, API.SEARCHUSER + resultEditText + "/" + idUserUseApp, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.has("USER_DETAIL")) {
                                    List<User> luser = new JsonConverter().toList(response.getJSONArray("USER_DETAIL").toString(),User.class);
                                    users.clear();
                                    users.addAll(luser);
                                    searchUserIdApt.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           /* Log.d("errorCustom", error.getMessage());*/
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
                }else{
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        searchUserIdApt = new SearchUserNameOrNumApt(users);
        recyclerView.setAdapter(searchUserIdApt);
    }

}
