package com.askhmer.chat.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.SearchByID;
import com.askhmer.chat.adapter.ListFriendFacebookAdapter;
import com.askhmer.chat.adapter.SimpleAdpter;
import com.askhmer.chat.model.DataFriends;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private SharedPreferences sharedPreferencesAccessToken;
    private SharedPreferences sharedPreferences2;
    private SharedPreferences.Editor editor;

    private SharedPreferencesFile mSharedPref;
    private Activity mActivity;


    private ArrayList<String> fbid_list= new ArrayList<>();
    private String  facebook_id_data;
    private String myid;

    public ThreeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mSharedPref = SharedPreferencesFile.newInstance(mActivity, SharedPreferencesFile.PREFER_FILE_NAME);
        myid = mSharedPref.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        /*initialize facebook*/
        FacebookSdk.sdkInitialize(this.getActivity());
        callbackManager = CallbackManager.Factory.create();

        sharedPreferencesAccessToken = this.getActivity().getSharedPreferences("accessTokenFB", 0);
        editor = sharedPreferencesAccessToken.edit();

        //sharedPreferences2 = this.getContext().getSharedPreferences(SharedPreferencesFile.PREFER_FILE_NAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        setHasOptionsMenu(true);

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
                getActivity().startActivity(intent);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://medayi.com/");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                */
                SimpleAdpter adapter = new SimpleAdpter(getContext());
                DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setAdapter(adapter)
                        .setBackgroundColorResId(R.color.bg_main_color)
                        .setContentHolder(new GridHolder(3))
                        .setHeader(R.layout.header)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                switch (position) {
                                    case 0:
                                        sharedVia("com.facebook.katana");
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        sharedVia("com.facebook.orca");
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        sharedVia("jp.naver.line.android");
                                        dialog.dismiss();
                                        break;
                                    case 3:
                                        sharedVia("com.whatsapp");
                                        dialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        String spAccessToken = mSharedPref.getStringSharedPreference(SharedPreferencesFile.ACCESSTOKEN);

        /*load list friends onstartup when logged in*/
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            Gson gson = new Gson();
            String json = sharedPreferencesAccessToken.getString("dataAccessToken", "");
            if(AccessToken.getCurrentAccessToken() != null || json != null){
                accessToken = gson.fromJson(json,AccessToken.class);
                Log.i("DataOnAccess", String.valueOf(accessToken));
                if(spAccessToken != null){
                    getListFriends(accessToken);
                }
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
                             // friends = new ArrayList<DataFriends>();
                            Log.i("onResponseData", response.toString());
                            for (int a = 0; a < rawName.length(); a++) {
                               // friends.add(new DataFriends(rawName.getJSONObject(a).getString("id").toString(),
                               // rawName.getJSONObject(a).getString("name").toString()));

                                  String facebook_id = rawName.getJSONObject(a).getString("id").toString();
//                                friends.add(new DataFriends(facebook_id,
//                                rawName.getJSONObject(a).getString("name").toString()));
                                  fbid_list.add(facebook_id);
                            }

                            Gson gson = new Gson();
                            facebook_id_data= gson.toJson(fbid_list);
                            //Toast.makeText(getActivity(), "facebook ID :" +  facebook_id_data, Toast.LENGTH_LONG).show();
                            Log.i("facebook", facebook_id_data);
                            //call method list facebook friend
                            listfacebookfriend();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        fadapter = new ListFriendFacebookAdapter(friends);
//                        layoutManager = new LinearLayoutManager(getActivity());
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        recyclerView.setAdapter(fadapter);
                    }
                }).executeAsync();
    }
    /*list facebook friend from database  */

    private void listfacebookfriend() {

       // String url = "http://chat.askhmer.com/api/friend/listfriendByFacebookId/"+facebook_id_data+"/"+myid;
        String url = "http://chat.askhmer.com/api/friend/listsuggestfriend";
        JSONObject param=new JSONObject();
        try {
            param.put("userId",myid);
            param.put("currentCity","");
            param.put("userName","");
            param.put("rowPerPage",10);
            param.put("page",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST,url,param,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");
                        //list item
                        friends = new ArrayList<DataFriends>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            friends.add(new DataFriends(jsonArray.getJSONObject(i).getString("userId"),
                                                        jsonArray.getJSONObject(i).getString("userName"),
                                                         jsonArray.getJSONObject(i).getString("userPhoto")));

                        }
                      //  Toast.makeText(getContext(), "user_id_data"+jsonArray, Toast.LENGTH_SHORT).show();
                        Log.i("user_id_data",jsonArray.toString());
                    }else{
                        Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    fadapter = new ListFriendFacebookAdapter(friends);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(fadapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomDialog.hideProgressDialog();
//                Toast.makeText(getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend, menu);


        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setVisible(false);
        MenuItem more = menu.findItem(R.id.more);

        more.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(), "Click more", Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        super.onCreateOptionsMenu(menu, inflater);
    }


    private void sharedVia(String packageName) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setPackage(packageName);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://medayi.com/");
        try {
            startActivity(sharingIntent);
        }catch (ActivityNotFoundException e) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("Your device no have this application.")
                    .show();
        }
    }

}
