package com.askhmer.chat.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.SearchByID;
import com.askhmer.chat.adapter.ListFriendFacebookAdapter;
import com.askhmer.chat.adapter.SimpleAdpter;
import com.askhmer.chat.listener.HideToolBarListener;
import com.askhmer.chat.model.DataFriends;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ThreeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //private View searchbyid;
   // private View invitebysms;

    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    private LinearLayout layout_no_connection,layout_header,layout_head;
    private Button btn_retry;
    //private AccessToken accessToken;
    private ListFriendFacebookAdapter fadapter;
    private SharedPreferences sharedPreferencesAccessToken;
    private SharedPreferences.Editor editor;
    private SharedPreferencesFile mSharedPref;
   // private Activity mActivity;


   // private ArrayList<String> fbid_list= new ArrayList<>();
  //  private String  facebook_id_data;
    private String myid;
    private boolean has;
    private int currentPage=1;



    //--- todo  refresh

    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    private HideToolBarListener hideToolBarListener;

    //-----todo refresh


    public ThreeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity mActivity = getActivity();
        mSharedPref = SharedPreferencesFile.newInstance(mActivity, SharedPreferencesFile.PREFER_FILE_NAME);
        myid = mSharedPref.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        /*initialize facebook*/
        FacebookSdk.sdkInitialize(this.getActivity());

        sharedPreferencesAccessToken = this.getActivity().getSharedPreferences("accessTokenFB", 0);
        editor = sharedPreferencesAccessToken.edit();

        //sharedPreferences2 = this.getContext().getSharedPreferences(SharedPreferencesFile.PREFER_FILE_NAME, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        setHasOptionsMenu(true);

        View searchbyid = threeFragmentView.findViewById(R.id.searchbyid);
        View invitebysms = threeFragmentView.findViewById(R.id.invitebysms);
        recyclerView = (RecyclerView)threeFragmentView.findViewById(R.id.lstfriendsfb);
        layout_header = (LinearLayout) threeFragmentView.findViewById(R.id.layout_header);
        layout_head = (LinearLayout) threeFragmentView.findViewById(R.id.layout_head);
        layout_no_connection = (LinearLayout) threeFragmentView.findViewById(R.id.layout_no_connection);
        layoutManager = new LinearLayoutManager(getActivity());

        //todo refesh
        swipeRefreshLayout = (SwipeRefreshLayout) threeFragmentView.findViewById(R.id.swipe_refresh_layout_sug_friend);
        // sets the colors used in the refresh animation
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_bright, R.color.green_light,
                R.color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(this);



        registerRecyclerListener();
        searchbyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchByID.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        btn_retry = (Button) threeFragmentView.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listfacebookfriend();
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
        listfacebookfriend();

        String spAccessToken = mSharedPref.getStringSharedPreference(SharedPreferencesFile.ACCESSTOKEN);
        /*load list friends onstartup when logged in*/
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            Gson gson = new Gson();
            String json = sharedPreferencesAccessToken.getString("dataAccessToken", "");
            if(AccessToken.getCurrentAccessToken() != null || json != null){
                AccessToken accessToken = gson.fromJson(json,AccessToken.class);
                Log.i("DataOnAccess", String.valueOf(accessToken));
                if(spAccessToken != null){
                  //  getListFriends(accessToken);
                }
            }
        }
        return threeFragmentView;
    }




    /*list friends who used the app through token*/
   /* public void getListFriends(AccessToken accessToken){
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
                           // listfacebookfriend();

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
    */
    /*list facebook friend from database  */

    private void listfacebookfriend() {
        currentPage=1;
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
                                        has=true;
                                        JSONArray jsonArray = response.getJSONArray("DATA");
                                        //list item
                                        ArrayList<DataFriends> friends = new ArrayList<DataFriends>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            friends.add(new DataFriends(jsonArray.getJSONObject(i).getString("userNo"),
                                                        jsonArray.getJSONObject(i).getString("userName"),
                                                        jsonArray.getJSONObject(i).getString("userPhoto"),
                                                        jsonArray.getJSONObject(i).getInt("userId"),
                                                        jsonArray.getJSONObject(i).getString("gender")
                                                        ));

                        }
                                        fadapter = new ListFriendFacebookAdapter(friends);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(fadapter);
                      //  Toast.makeText(getContext(), "user_id_data"+jsonArray, Toast.LENGTH_SHORT).show();
                      //  Log.i("user_id_data",jsonArray.toString());
                    }else{
                                        has=false;
                       // Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_header.setVisibility(View.VISIBLE);
                                    layout_head.setVisibility(View.VISIBLE);
                                    layout_no_connection.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                recyclerView.setVisibility(View.GONE);
                layout_header.setVisibility(View.GONE);
                layout_head.setVisibility(View.GONE);
                layout_no_connection.setVisibility(View.VISIBLE);
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
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.askhmer.chat&hl=en");
        try {
            startActivity(sharingIntent);
        }catch (ActivityNotFoundException e) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("Your device no have this application.")
                    .show();
        }
    }

    public  void registerRecyclerListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView1, int newState) {
                super.onScrollStateChanged(recyclerView1, newState);
                int pastVisiblesItems=0, visibleItemCount=0, totalItemCount=0;
                boolean hasStopped = newState == recyclerView1.SCROLL_STATE_SETTLING;
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                if(hasStopped) {
                   // Log.i("MyScroll", "My Scroll Stopped Now");
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(has==true){
                            currentPage++;
                            String url = "http://chat.askhmer.com/api/friend/listsuggestfriend";
                            JSONObject param=new JSONObject();
                            try {
                                param.put("userId",myid);
                                param.put("currentCity","");
                                param.put("userName","");
                                param.put("rowPerPage",10);
                                param.put("page",currentPage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST,url,param,new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    ArrayList<DataFriends> friend=null;
                                    try {
                                        if (response.has("DATA")) {
                                            has=true;
                                            JSONArray jsonArray = response.getJSONArray("DATA");
                                            //list item
                                            friend = new ArrayList<DataFriends>();
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                friend.add(new DataFriends(jsonArray.getJSONObject(i).getString("userNo"),
                                                        jsonArray.getJSONObject(i).getString("userName"),
                                                        jsonArray.getJSONObject(i).getString("userPhoto"),
                                                        jsonArray.getJSONObject(i).getInt("userId"),
                                                        jsonArray.getJSONObject(i).getString("gender")));

                                            }
                                            fadapter.appendList(friend);
                                            recyclerView.setAdapter(fadapter);
                                            //  Toast.makeText(getContext(), "user_id_data"+jsonArray, Toast.LENGTH_SHORT).show();
                                            //  Log.i("user_id_data",jsonArray.toString());
                                        }else{
                                            has=false;
                                           // Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } finally {

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
                    }
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            fadapter.clearData();
            listfacebookfriend();
            fadapter.notifyDataSetChanged();
            handler.post(refreshing);
            swipeRefreshLayout.setRefreshing(false);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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

    //--- end refresh new data
}
