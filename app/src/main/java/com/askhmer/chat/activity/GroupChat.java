package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.GroupChatRecyclerAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;
import java.util.List;

public class GroupChat extends SwipeBackActivity {

    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;
    private ArrayList<Friends> listFriend;
    private EditText edtSearchfri;


    private Toolbar toolbar;
    private String groupChatName;

    private String data = "";
    private GroupChatRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("select friends");

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

        edtSearchfri = (EditText)findViewById(R.id.edtSearchfri);
        edtSearchfri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_btn), null);


        // Setup layout manager for mBlogList and column count
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Bind adapter to recycler
        mFriends = new ArrayList<>();

        //list item
        for (int i = 0; i < 15; i++) {
            Friends item = new Friends();
            item.setFriName("Friend : " + i);
            item.setImg(R.drawable.ic_people);
            item.setChatId("chat Id : 000" + i);
            item.setIsSelected(false);
            mFriends.add(item);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.scrollToPosition(0);

        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new GroupChatRecyclerAdapter(mFriends);
        mRecyclerView.setAdapter(adapter);


        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(GroupChat.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));


        /*
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Friends fri = new Friends();
                if(fri.isSelected()){
                    findViewById(R.id.action_done).setVisibility(View.VISIBLE);
                    Toast.makeText(GroupChat.this, "Test", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_chat_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_done:
                inputGroupName();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void inputGroupName(){
        LayoutInflater factory = LayoutInflater.from(GroupChat.this);

        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.input_group_chat_name, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.et_group_name);

        input1.setText("", TextView.BufferType.EDITABLE);

        selectedDone();

        if(data == ""){
            Toast.makeText(GroupChat.this, "Please select your friends!!!", Toast.LENGTH_SHORT).show();
        }else {

            final AlertDialog.Builder alert = new AlertDialog.Builder(GroupChat.this);

            alert.setIcon(R.drawable.signup);
            alert.setTitle("Input your group chat name!!!").setView(textEntryView).setPositiveButton("Save",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton) {

                            groupChatName = input1.getText().toString();

                            Log.i("AlertDialog", "TextEntry 1 Entered " + groupChatName);
                            Log.d("data",data);

                            Intent in = new Intent(GroupChat.this, Chat.class);
                            in.putExtra("friends",data);
                            in.putExtra("groupName",groupChatName);
                            startActivity(in);
                            finish();

                        }
                    }).setNegativeButton("Cancle",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            alert.show();

        }

    }

    public void selectedDone(){

        /*List<Friends> stList = ((GroupChatRecyclerAdapter) adapter)
                .getmFriendtist()*/;
        List<Friends> stList = listFriend;

        for (int i = 0; i < stList.size(); i++) {
            Friends singleFriend = stList.get(i);
            if (singleFriend.isSelected() == true) {
                data = data + "\n" + singleFriend.getFriName().toString();
            }
        }
    }
}
