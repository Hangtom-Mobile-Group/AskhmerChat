package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.SecretChatRecyclerAdapter;
import com.askhmer.chat.listener.RecyclerItemClickListener;
import com.askhmer.chat.model.Friends;

import java.util.ArrayList;

public class SecretChat extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_chat);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("select a friend");

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
            mFriends.add(item);
        }

//        Toast.makeText(SecretChat.this, ""+mFriends, Toast.LENGTH_SHORT).show();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        SecretChatRecyclerAdapter adapter = new SecretChatRecyclerAdapter(mFriends);
        mRecyclerView.setAdapter(adapter);

        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListener(
                        this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View itemView, int position) {
                                SecretChat.this.position = position;
                                Intent in = new Intent(SecretChat.this, Chat.class);
                                in.putExtra("Friend_name", mFriends.get(position).getFriName());
                                startActivity(in);
                                Log.d("friend", mFriends.get(position).getFriName());
                                finish();
                            }
                        }));
    }
}

