package com.askhmer.chat.fragments;

import android.app.Activity;
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
import com.askhmer.chat.activity.SearchByID;
import com.askhmer.chat.adapter.ListFriendFacebookAdapter;
import com.askhmer.chat.model.DataFriends;
import com.askhmer.chat.util.SharedPreferencesFile;
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
    private SharedPreferences sharedPreferences2;
    private SharedPreferences.Editor editor;

    private SharedPreferencesFile mSharedPref;
    private Activity mActivity;

    public ThreeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mSharedPref = SharedPreferencesFile.newInstance(mActivity, SharedPreferencesFile.PREFER_FILE_NAME);

        /*initialize facebook*/
        FacebookSdk.sdkInitialize(this.getActivity());
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = this.getActivity().getSharedPreferences("accessTokenFB", 0);
        editor = sharedPreferences.edit();

        //sharedPreferences2 = this.getContext().getSharedPreferences(SharedPreferencesFile.PREFER_FILE_NAME, 0);
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
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });
        invitebysms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getActivity(), InviteBySMS.class);
                getActivity().startActivity(intent); */
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://medayi.com/");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        /*load list friends onstartup when logged in*/
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("dataAccessToken", "");
            if(AccessToken.getCurrentAccessToken() != null || json != null){
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
                                friends.add(new DataFriends(rawName.getJSONObject(a).getString("id").toString(),
                                        rawName.getJSONObject(a).getString("name").toString()));
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
