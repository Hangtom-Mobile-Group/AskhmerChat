package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.GroupChatRecyclerAdapter;
import com.askhmer.chat.model.Friends;

import java.util.ArrayList;
import java.util.List;

public class GroupChat extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;

    private Toolbar toolbar;

    private String data = "";
    private GroupChatRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

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



        /*// Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListener(
                        this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View itemView, int position) {
                                GroupChat.this.position = position;
                                Intent in = new Intent(GroupChat.this, Chat.class);
                                startActivity(in);
                            }
                        }));
*/
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
                selectedDone();
                Intent in = new Intent(GroupChat.this, Chat.class);
                in.putExtra("friends",data);
                startActivity(in);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void selectedDone(){

        List<Friends> stList = ((GroupChatRecyclerAdapter) adapter)
                .getmFriendtist();

        for (int i = 0; i < stList.size(); i++) {
            Friends singleFriend = stList.get(i);
            if (singleFriend.isSelected() == true) {

                data = data + "\n" + singleFriend.getFriName().toString();
            }
        }
        Toast.makeText(GroupChat.this, "Chat with : "+data, Toast.LENGTH_SHORT).show();
    }
}
