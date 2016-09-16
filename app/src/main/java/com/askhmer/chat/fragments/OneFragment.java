package com.askhmer.chat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.SearchByID;
import com.askhmer.chat.adapter.ExpandableListAdapter;
import com.askhmer.chat.adapter.FriendAdapter;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment {


    final String TAG = "TAG";
    String myid;
    private SharedPreferencesFile mSharedPrefer;

    private List<Friends> friendtList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExpandableListAdapter adapter;
    private String textSearch;
    private LinearLayout firstShow;
    private Button btnAddFriend;
    private EditText edSearchfriend;

    private FriendAdapter adapterSearch;


    public OneFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefer = SharedPreferencesFile.newInstance(getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        myid = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);



        edSearchfriend = (EditText) oneFragmentView.findViewById(R.id.edSearchfriend);
        edSearchfriend.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) {

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            adapter.clearData();
                            adapter.notifyDataSetChanged();
                            listSearchFriend();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 2000);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });




        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) oneFragmentView.findViewById(R.id.recycler_view);
        firstShow = (LinearLayout) oneFragmentView.findViewById(R.id.layout_first_friend);
        btnAddFriend = (Button) oneFragmentView.findViewById(R.id.btn_add_now);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SearchByID.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ExpandableListAdapter(friendtList);
        adapter.clearData();
        listfriend();


        return oneFragmentView;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflater.inflate(R.menu.menu_friend, menu);
        // super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend, menu);


//        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.notification).getActionView();
//        TextView mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
//        mCounter.setText("12");


        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered

                textSearch = newText;
                adapter.clearData();
                listSearchFriend();

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                //Toast.makeText(ActivitySearchSub.this, query , Toast.LENGTH_SHORT).show();

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void listfriend() {

        //***************===<< begin new style >>====******************************

        //String url = "http://10.0.3.2:8080/ChatAskhmer/api/friend/listfriendById/"+ myid;
       // String url = API.LISTFRIEND + myid;
        String url = API.LISTFREINDBYID + myid;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");

                        Friends itemH = new Friends();
                        itemH.setType(ExpandableListAdapter.HEADER);
                        itemH.setHeader("My Profile");

                        friendtList.add(itemH);

                        Friends item = new Friends();
                        item.setType(ExpandableListAdapter.CHILD);
                        item.setFriId(jsonArray.getJSONObject(0).getInt("userId"));
                        item.setFriName(jsonArray.getJSONObject(0).getString("userName"));
                        item.setChatId(jsonArray.getJSONObject(0).getString("userNo"));
                        item.setImg(jsonArray.getJSONObject(0).getString("userPhoto"));
                        item.setIsFriend(jsonArray.getJSONObject(0).getBoolean("friend"));
                        friendtList.add(item);

                        Friends itemH2 = new Friends();
                        itemH2.setType(ExpandableListAdapter.HEADER);
                        itemH2.setHeader("My Friends");

                        friendtList.add(itemH2);

                        //list item
                        for (int i = 1; i < jsonArray.length(); i++) {
                            Friends itemFri = new Friends();
                            itemFri.setType(ExpandableListAdapter.CHILD);
                            itemFri.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            itemFri.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                            itemFri.setChatId(jsonArray.getJSONObject(i).getString("userNo"));
                            itemFri.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                            itemFri.setIsFriend(jsonArray.getJSONObject(i).getBoolean("friend"));
//                            friItem.invisibleChildren.add(itemFri);
                            friendtList.add(itemFri);
                        }

/*
                        Friends myProfile = new Friends(ExpandableListAdapter.HEADER,"My Profile");
                        myProfile.invisibleChildren = new ArrayList<>();
                        Friends item = new Friends();
                        item.setType(ExpandableListAdapter.CHILD);
                        item.setFriId(jsonArray.getJSONObject(0).getInt("userId"));
                        item.setFriName(jsonArray.getJSONObject(0).getString("userName"));
                        item.setChatId(jsonArray.getJSONObject(0).getString("userNo"));
                        item.setImg(jsonArray.getJSONObject(0).getString("userPhoto"));
                        item.setIsFriend(jsonArray.getJSONObject(0).getBoolean("friend"));
                        myProfile.invisibleChildren.add(item);
                        friendtList.add(myProfile);

                        Friends friItem = new Friends(ExpandableListAdapter.HEADER,"My Friends");
                        friItem.invisibleChildren = new ArrayList<>();

                        //list item
                        for (int i = 1; i < jsonArray.length(); i++) {
                            Friends itemFri = new Friends();
                                itemFri.setType(ExpandableListAdapter.CHILD);
                                itemFri.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                                itemFri.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                                itemFri.setChatId(jsonArray.getJSONObject(i).getString("userNo"));
                                itemFri.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                                itemFri.setIsFriend(jsonArray.getJSONObject(i).getBoolean("friend"));
                            friItem.invisibleChildren.add(itemFri);
                        }
                        friendtList.add(friItem);

                        */
                    }else{
                        Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    // CustomDialog.hideProgressDialog();

//                               adapter = new FriendAdapter(friendtList);
                   adapter.notifyDataSetChanged();
                   recyclerView.setAdapter(adapter);

                    if (friendtList.size() == 0) {
                        firstShow.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        firstShow.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // CustomDialog.hideProgressDialog();
                    Toast.makeText(getContext(),"No internet connection!!!", Toast.LENGTH_LONG).show();
            }
        });
        // Add request queue
       // VolleySingleton.getsInstance().addToRequestQueue(jsonRequest);     ***** it not work
       MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);


        //***************===<< end new style >>====******************************
    }


    /**
     * search
     */

    private void listSearchFriend() {

        textSearch =  edSearchfriend.getText().toString();
        String url = API.SEARCHFRIEND + textSearch + "/"+ myid;
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
                            item.setIsFriend(jsonArray.getJSONObject(i).getBoolean("friend"));
                            friendtList.add(item);
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // e.printStackTrace();
                } finally {

                    adapterSearch = new FriendAdapter(friendtList);
                    adapterSearch.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterSearch);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomDialog.hideProgressDialog();
//                adapterSearch.clearData();
                adapter.clearData();
                listfriend();
                //   Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }

}
