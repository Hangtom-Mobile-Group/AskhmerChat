package com.askhmer.chat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.GroupChat;
import com.askhmer.chat.activity.SecretChat;
import com.askhmer.chat.adapter.ChatRoomAdapter;
import com.askhmer.chat.adapter.SecretChatRecyclerAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.HideToolBarListener;
import com.askhmer.chat.listener.HidingScrollListener;
import com.askhmer.chat.listener.HidingViewScrollListener;
import com.askhmer.chat.listener.NewMessageListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.ChatRoom;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.MessageConvertor;
import com.askhmer.chat.util.MyAppp;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.ToolBarUtils;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TwoFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,NewMessageListener {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close;

    private Button chatNow,btn_retry;

    private com.github.clans.fab.FloatingActionButton fab12;
    private com.github.clans.fab.FloatingActionButton fab22;

    private View hideLayout;

    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;
    private LinearLayout firstShow,layout_no_connection;
    private SecretChatRecyclerAdapter adapter;
    private int groupID;
    private String user_id;
    private int room_id;
    private SharedPreferencesFile mSharedPrefer;
    private FrameLayout fragment_tow_layout;
    private FloatingActionMenu menu2;
    private String imagePath;
    private String userProfile;
    private  String textSearch;
    private EditText edSearchChat;

    private ArrayList<ChatRoom> mChatRoom;

    private ChatRoomAdapter chatRoomAdapter;

    //--- todo  refresh

    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    private HideToolBarListener hideToolBarListener;

    //-----todo refresh

    private LinearLayout layoutBtnTop;



    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefer = SharedPreferencesFile.newInstance(getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        userProfile = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.IMGPATH);

        try {
            Log.e("user profile F2 t",""+userProfile);
            if(userProfile == null){
                userProfile(user_id);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        // todo  check room and list
        //checkGroupChat();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View twoFragmentView = inflater.inflate(R.layout.fragment_two, container, false);

        // todo  check room and list
        /*checkGroupChat();*/

        setHasOptionsMenu(true);


        chatNow = (Button) twoFragmentView.findViewById(R.id.btn_chat_now);

        chatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add data to table seen
                addSeen();
                Intent in = new Intent(getActivity(), SecretChat.class);
                startActivity(in);
//                startActivityForResult(in, 10001);
            }
        });

        mRecyclerView = (RecyclerView) twoFragmentView.findViewById(R.id.list_chat);
        firstShow = (LinearLayout) twoFragmentView.findViewById(R.id.layout_first);
        layout_no_connection = (LinearLayout) twoFragmentView.findViewById(R.id.layout_no_connection);


        // Bind adapter to recycler
        mFriends = new ArrayList<>();
        mChatRoom = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int paddingTop = ToolBarUtils.getToolbarHeight(getActivity()) + ToolBarUtils.getTabsHeight(getActivity());

        mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), paddingTop, mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());

        mRecyclerView.addOnScrollListener(new HidingScrollListener(getActivity()) {

            @Override
            public void onMoved(int distance) {

                hideToolBarListener = (HideToolBarListener) getActivity();
                hideToolBarListener.callHideToolBar(distance);

            }

            @Override
            public void onShow() {
                hideToolBarListener.callOnShow();
            }

            @Override
            public void onHide() {
                hideToolBarListener.callOnHide();
            }

        });


        btn_retry = (Button) twoFragmentView.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearData();
                chatRoomAdapter.clearData();
                listChatRoom(getDialogLoading(true));
            }
        });


        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent in = new Intent(getActivity(), Chat.class);
                        if(mChatRoom.get(position).isGroup()){
                            in.putExtra("Friend_name", "");
                            in.putExtra("groupName", mChatRoom.get(position).getRoomName());
                            in.putExtra("isGroup", mChatRoom.get(position).isGroup());
                        }else {
                            in.putExtra("Friend_name", mChatRoom.get(position).getRoomName());
                            in.putExtra("isGroup", mChatRoom.get(position).isGroup());
                            in.putExtra("groupName","");

                        }
//
                        in.putExtra("groupID",mChatRoom.get(position).getRoomId());
                        in.putExtra("friid", mChatRoom.get(position).getFriId());
                        in.putExtra("friendsID",mChatRoom.get(position).getMemberID());
                        in.putExtra("friend_image_url", mChatRoom.get(position).getImgUrl());
                        /*startActivity(in);*/
                        startActivityForResult(in, 10001);
//                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivityForResult(in, 10001);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }

                    @Override
                    public void onLongClick(final View view, final int position) {
                        try{
                            groupID =  mChatRoom.get(position).getRoomId();
                        }catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Delete Conversation")
                                    .setMessage("Are you sure you want to delete conversation?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteConversation();
                                            mChatRoom.remove(position);
                                            chatRoomAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_delete)
                                    .show();

                    }
                }));

        chatRoomAdapter = new ChatRoomAdapter(mChatRoom);

        adapter = new SecretChatRecyclerAdapter(mFriends);
        adapter.clearData();
        adapter.notifyDataSetChanged();

        //todo refesh
        swipeRefreshLayout = (SwipeRefreshLayout) twoFragmentView.findViewById(R.id.swipe_refresh_layout);
        // sets the colors used in the refresh animation
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_bright, R.color.green_light,
                R.color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(this);


        hideLayout = twoFragmentView.findViewById(R.id.hiden_layout);

        menu2 = (FloatingActionMenu) twoFragmentView.findViewById(R.id.menu2);

        fab12 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab12);
        fab22 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab22);


        fab12.setOnClickListener(this);
        fab22.setOnClickListener(this);

//Fab button listener
        menu2.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
//                    fragment_tow_layout.setBackgroundColor(Color.parseColor("#82000000"));
                } else {
//                    fragment_tow_layout.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

//Hide/show layout but top and fab button

        layoutBtnTop = (LinearLayout) twoFragmentView.findViewById(R.id.layout_btn_top);
        layoutBtnTop.setPadding(layoutBtnTop.getPaddingLeft(),paddingTop, layoutBtnTop.getPaddingRight(), layoutBtnTop.getPaddingBottom());
        mRecyclerView.setOnScrollListener(new HidingViewScrollListener() {
            @Override
            public void onHide() {
                layoutBtnTop.animate().translationY(-layoutBtnTop.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                menu2.animate().translationY(menu2.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            public void onShow() {
                layoutBtnTop.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                menu2.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });

        final LinearLayout layoutMarket = (LinearLayout) twoFragmentView.findViewById(R.id.layout_market_chat);
        final Button norChat = (Button) twoFragmentView.findViewById(R.id.btn_nor_chat);
        final Button marketChat = (Button) twoFragmentView.findViewById(R.id.btn_market_chat);

        norChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketChat.setBackgroundColor(getResources().getColor(R.color.btncenter));
                norChat.setBackgroundColor(getResources().getColor(R.color.btnstart));
                marketChat.setTextColor(Color.BLACK);
                norChat.setTextColor(Color.WHITE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                layoutMarket.setVisibility(View.GONE);
                if (mChatRoom.size()<=0){
                    firstShow.setVisibility(View.VISIBLE);
                }
            }
        });


        marketChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                norChat.setBackgroundColor(getResources().getColor(R.color.btncenter));
                marketChat.setBackgroundColor(getResources().getColor(R.color.btnstart));
                norChat.setTextColor(Color.BLACK);
                marketChat.setTextColor(Color.WHITE);
                layoutMarket.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                firstShow.setVisibility(View.GONE);
            }
        });

        MyAppp.setNewMessageListener(this);

        return twoFragmentView;
    }

    //--------refresh new data

    @Override
    public void onRefresh() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            chatRoomAdapter.clearData();
            checkGroupChat(getDialogLoading(true));
            chatRoomAdapter.notifyDataSetChanged();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflater.inflate(R.menu.menu_friend, menu);
        // super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem more = menu.findItem(R.id.more);

        more.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(), "Click more", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                if (!(newText.equals("") ||  newText.isEmpty() || newText == null)){
                    textSearch = newText;
                    adapter.clearData();
                    listSearchGroupChat();
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                //Toast.makeText(ActivitySearchSub.this, query , Toast.LENGTH_SHORT).show();
                return true;
            }

        };

/*
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchClickListener searchClickListener = (SearchClickListener) getContext();

                searchClickListener.callDisableIcon(View.VISIBLE);
                return false;
            }
        });
*/

        searchView.setOnQueryTextListener(queryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void deleteConversation(){
        try {
            String url = API.DELETEMCONVERSATION+"/"+user_id+"/"+groupID;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
//                            Log.d("love", response.toString());
                        Toast.makeText(getContext(),"DELETE CONVERSATION SUCCED", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Unsuccessfully Delete !!", Toast.LENGTH_LONG).show();
                    } finally {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
        } catch (Exception e) {
            Toast.makeText(getContext(), "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * check group chat
     */
    private  void checkGroupChat(final Dialog dialog){
        String url = API.CHECKCHATROOM+ user_id + "/"+ user_id;
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            room_id = response.getInt("MESSAGE_ROOM_ID");
                        }
                        else{
                            room_id = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } finally {
                    listChatRoom(dialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // checkGroupChat(dialog);
                dialog.dismiss();
                mRecyclerView.setVisibility(View.GONE);
                firstShow.setVisibility(View.GONE);
                layout_no_connection.setVisibility(View.VISIBLE);
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(objectRequest);

    }

    private void listChatRoom(final Dialog dialog) {
        String url = API.LISTHISTORYCHATROOM + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {

                        JSONArray jsonArray = response.getJSONArray("DATA");
                        Log.d("Data_F2", ": " + response.toString());
                        //list item
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChatRoom item = new ChatRoom();

                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            if(jsonArray.getJSONObject(i).getString("roomName").equals("")) {
                                item.setRoomName(jsonArray.getJSONObject(i).getString("userName"));
                                item.setImgUrl(jsonArray.getJSONObject(i).getString("userPhoto"));
                                item.setGroup(false);
                            }else{
                                item.setRoomName(jsonArray.getJSONObject(i).getString("roomName"));
                                item.setImgUrl("user/d00f3132-d132-4f8b-89b2-e0e5d05a3fc1.jpg");
                                item.setGroup(true);
                            }
                            if(jsonArray.getJSONObject(i).getInt("is_seen") == 1){
                                item.isSeen();
                            }
                            if(jsonArray.getJSONObject(i).getString("message").contains("http://chat.askhmer.com/resources/upload/file/sticker")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("who_send_name")+" sent you the sticker.");
                            }else if(jsonArray.getJSONObject(i).getString("message").contains("http://chat.askhmer.com/resources/upload/file/thumnails")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("who_send_name")+" sent you the image.");
                            }else if(jsonArray.getJSONObject(i).getString("message").contains(".mp3")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("who_send_name")+" sent you the voice message.");
                            }else {
                               // item.setCurrentMsg(MessageConvertor.emojisDecode(jsonArray.getJSONObject(i).getString("message")));
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("message"));
                            }
                            item.setRoomId(jsonArray.getJSONObject(i).getInt("roomId"));
                            item.setCounterMsgNotSeen(jsonArray.getJSONObject(i).getInt("count_message"));
                            item.setMsgDate(jsonArray.getJSONObject(i).getString("msgDate"));
                            item.setMsgTime(jsonArray.getJSONObject(i).getString("msgTime"));
                            item.setCounterMember(jsonArray.getJSONObject(i).getInt("counter_member"));
                            item.setMemberID(jsonArray.getJSONObject(i).getString("member_in_room"));

                            mChatRoom.add(item);
                            Log.e("Data_F2",": "+item);
                        }
                    }else{
                        Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    hideToolBarListener.callOnShow();
                    chatRoomAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(chatRoomAdapter);

                    if (mChatRoom.size() == 0) {
                        layout_no_connection.setVisibility(View.GONE);
                        firstShow.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        layout_no_connection.setVisibility(View.GONE);
                        firstShow.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //listChatRoom(dialog);
                dialog.dismiss();
                mRecyclerView.setVisibility(View.GONE);
                firstShow.setVisibility(View.GONE);
                layout_no_connection.setVisibility(View.VISIBLE);
            }
        });
        // Add request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);


        //***************===<< end new style >>====******************************
    }


    private void listGroupChat() {
        String url = API.LISTCHATROOM + user_id +"/"+room_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");
                        //list item
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Friends item = new Friends();
                            Log.d("room", jsonArray.getJSONObject(i).getString("roomName"));

                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            if(jsonArray.getJSONObject(i).getString("roomName").equals("") ) {
                                item.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                                item.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                            }else{
                                item.setFriName(jsonArray.getJSONObject(i).getString("roomName"));
                                item.setImg("user/d00f3132-d132-4f8b-89b2-e0e5d05a3fc1.jpg");
                            }
                            item.setRoomId(jsonArray.getJSONObject(i).getInt("roomId"));
                            mFriends.add(item);
                            Log.d("TAG",item.toString());
                        }
                    }else{
                        Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    adapter = new SecretChatRecyclerAdapter(mFriends);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);

                    if (mFriends.size() == 0) {
                        firstShow.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        firstShow.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomDialog.hideProgressDialog();
//                Toast.makeText(getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
        // Add request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);


        //***************===<< end new style >>====******************************
    }


    /**
     * list search group chat
     */
    private void listSearchGroupChat() {
//        textSearch = edSearchChat.getText().toString();
        String url ="http://chat.askhmer.com/api/chathistory/searchchatroom/"+textSearch+"/"+user_id;
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
                            Log.d("room", jsonArray.getJSONObject(i).getString("roomName"));

                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            if(jsonArray.getJSONObject(i).getString("roomName").equals("") ) {
                                item.setFriName(jsonArray.getJSONObject(i).getString("userName"));
                                item.setImg(jsonArray.getJSONObject(i).getString("userPhoto"));
                            }else{
                                item.setFriName(jsonArray.getJSONObject(i).getString("roomName"));
                                item.setImg("user/d00f3132-d132-4f8b-89b2-e0e5d05a3fc1.jpg");
                            }
                            item.setRoomId(jsonArray.getJSONObject(i).getInt("roomId"));
                            mFriends.add(item);
                            Log.d("TAG", item.toString());
                        }
                    }else{
                        Toast.makeText(getContext(), "No CROUP CHAT Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    adapter = new SecretChatRecyclerAdapter(mFriends);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chatRoomAdapter.clearData();
                chatRoomAdapter.notifyDataSetChanged();
                listChatRoom(getDialogLoading(true));
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }



    //---------------------------------------------------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String text = "";
        switch (id){

            case R.id.fab12:
                Intent in = new Intent(getActivity(), SecretChat.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menu2.close(true);
                break;
            case R.id.fab22:
                Intent in2 = new Intent(getActivity(), GroupChat.class);
                startActivity(in2);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menu2.close(true);
                break;
        }
    }


    // Load image from server
    public void userProfile(String user_id) {
        //String url = "http://10.0.3.2:8080/ChatAskhmer/api/user/viewUserById/" + user_id;
        Log.e("user Profile method",""+user_id);
        String url = API.VIEWUSERPROFILE + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("STATUS")) {
                        JSONObject object = response.getJSONObject("DATA");
                        imagePath = object.getString("userPhoto");

                        String str=imagePath;
                        boolean found = str.contains("facebook");
                        Log.d("found","Return : "+ found);

                        String imgPaht1 = API.UPLOADFILE +imagePath;
                        String imgPaht2 = imagePath;

                        if( found == false){
                            mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.IMGPATH, imgPaht1);
                            Log.e("img_profile F2",imgPaht1);
                        }else{
                            mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.IMGPATH, imgPaht2);
                            Log.e("img_profile F2", imgPaht2);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "There is Something Wrong !!", Toast.LENGTH_LONG).show();
            }
        });
        // Add request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }






    //---todo add user and roomid to table seen
    public void addSeen() {
        try {
            mSharedPrefer = SharedPreferencesFile.newInstance(getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
            user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
            String url ="http://chat.askhmer.com/api/seen/"+room_id+"/"+user_id;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("STATUS")==200) {
                                    Log.d("addSeen", response.toString());
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Unsuccessfully Added !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
        } catch (Exception e) {
            Toast.makeText(getContext(), "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //--------------------------------------------------------------------

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                hideToolBarListener = (HideToolBarListener) getActivity();

                chatRoomAdapter.clearData();
                chatRoomAdapter.notifyDataSetChanged();

                layoutBtnTop.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                menu2.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }catch (NullPointerException e){

            }finally {
                checkGroupChat(getDialogLoading(true));
            }
        }
    }

    private void setUpFirstData(boolean notThread) {
        try {
            hideToolBarListener = (HideToolBarListener) getActivity();

            /*chatRoomAdapter.clearData()*/
            chatRoomAdapter.removeData();
            chatRoomAdapter.notifyDataSetChanged();

            layoutBtnTop.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            menu2.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }catch (NullPointerException e){

        }finally {
            checkGroupChat(getDialogLoading(notThread));
        }
    }

    private void requestNewChatInRoom(int roomId) {
        String url = API.LISTCHATROOMNOT + user_id + "/" + roomId;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ChatRoom item = null;
                try {
                    if (response.has("DATA")) {
                        JSONArray jsonArray = response.getJSONArray("DATA");
                        //list item
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item = new ChatRoom();

                            item.setFriId(jsonArray.getJSONObject(i).getInt("userId"));
                            /*String roomName = jsonArray.getJSONObject(i).getString("roomName");*/
                            String isGroup = jsonArray.getJSONObject(i).getString("group");
                            if(isGroup.equals("false")) {
                                item.setRoomName(jsonArray.getJSONObject(i).getString("userName"));
                                item.setImgUrl(jsonArray.getJSONObject(i).getString("userProfile"));
                                item.setGroup(false);
                            }else{
                                item.setRoomName(jsonArray.getJSONObject(i).getString("groupName"));
                                item.setImgUrl("user/d00f3132-d132-4f8b-89b2-e0e5d05a3fc1.jpg");
                                item.setGroup(true);
                            }
                            if(jsonArray.getJSONObject(i).getString("message").contains("http://chat.askhmer.com/resources/upload/file/sticker")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("userName")+" sent you the sticker.");
                            }else if(jsonArray.getJSONObject(i).getString("message").contains("http://chat.askhmer.com/resources/upload/file/thumnails")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("who_send_name")+" sent you the image.");
                            }else if(jsonArray.getJSONObject(i).getString("message").contains(".mp3")){
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("who_send_name")+" sent you the voice message.");
                            }else {
                                item.setCurrentMsg(jsonArray.getJSONObject(i).getString("message"));
                            }
                            item.setRoomId(jsonArray.getJSONObject(i).getInt("roomId"));
                            item.setCounterMsgNotSeen(jsonArray.getJSONObject(i).getInt("count_message"));
                            item.setMsgDate(jsonArray.getJSONObject(i).getString("msgDate"));
                            item.setMsgTime(jsonArray.getJSONObject(i).getString("msgTime"));
                            item.setCounterMember(jsonArray.getJSONObject(i).getInt("counter_member"));
                            item.setMemberID(jsonArray.getJSONObject(i).getString("receivers"));
                        }
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    hideToolBarListener.callOnShow();
                    chatRoomAdapter.changeData(item);
                    chatRoomAdapter.notifyDataSetChanged();

                    if (mChatRoom.size() == 0) {
                        layout_no_connection.setVisibility(View.GONE);
                        firstShow.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        layout_no_connection.setVisibility(View.GONE);
                        firstShow.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mRecyclerView.setVisibility(View.GONE);
                firstShow.setVisibility(View.GONE);
                layout_no_connection.setVisibility(View.VISIBLE);
            }
        });
        // Add request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }

    private Dialog getDialogLoading(boolean show) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        if (show == true) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("fragment2", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideKeyBoard();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        MyAppp.setNewMessageListener(this);
        Log.e("fragment2", "onResume");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10001) {
            try {

                chatRoomAdapter.clearData();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            checkGroupChat(getDialogLoading(true));
            chatRoomAdapter.notifyDataSetChanged();
            layoutBtnTop.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            menu2.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    public void stopHandler() {
        Log.e("show_123", "move");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("show_123", "onStop");
    }

    @Override
    public void getNewMessageInRoom(int roomId) {
        requestNewChatInRoom(roomId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyAppp.setNewMessageListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyAppp.setNewMessageListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyAppp.setNewMessageListener(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyAppp.setNewMessageListener(null);
    }
}
