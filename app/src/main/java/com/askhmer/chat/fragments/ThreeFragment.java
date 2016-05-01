package com.askhmer.chat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.InviteBySMS;
import com.askhmer.chat.activity.SearchByID;
import com.askhmer.chat.adapter.ListFriendFacebookAdapter;
import com.askhmer.chat.model.DataFriends;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ThreeFragment extends Fragment {
    private View searchbyid;
    private View invitebysms;

    private CallbackManager callbackManager;

    private LoginButton btnfb;
    private Button custombtnfb;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    private AccessToken accessToken;
    private ArrayList<DataFriends> friends;
    private ListFriendFacebookAdapter fadapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ThreeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*initialize facebook*/
        FacebookSdk.sdkInitialize(this.getActivity());
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = this.getActivity().getSharedPreferences("accessTokenFB", 0);
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);


        searchbyid = threeFragmentView.findViewById(R.id.searchbyid);
        invitebysms = threeFragmentView.findViewById(R.id.invitebysms);
        recyclerView = (RecyclerView)threeFragmentView.findViewById(R.id.lstfriendsfb);

        searchbyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchByID.class);
                startActivity(intent);
            }
        });
        invitebysms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviteBySMS.class);
                getActivity().startActivity(intent);
            }
        });

        /*load list friends onstartup when logged in*/
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("dataAccessToken", "");
            if(AccessToken.getCurrentAccessToken() != null && json != null){
                accessToken = gson.fromJson(json,AccessToken.class);
                Log.i("DataOnAccess", String.valueOf(accessToken));
                getListFriends(accessToken);
            }
        }
        return threeFragmentView;
    }

    /*list friends who used the app through token*/
    public void getListFriends(AccessToken accessToken){
        GraphRequestAsyncTask request = new GraphRequest(accessToken,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            friends = new ArrayList<DataFriends>();
                            Log.i("onResponseData",response.toString());
                            for (int a = 0; a < rawName.length(); a++) {
                                friends.add(new DataFriends(rawName.getJSONObject(0).getString("id").toString(),
                                        rawName.getJSONObject(0).getString("name").toString()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fadapter = new ListFriendFacebookAdapter(friends);
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(fadapter);
                    }
                }).executeAsync();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_addfriend, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
