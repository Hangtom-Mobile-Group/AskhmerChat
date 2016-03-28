package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = Chat.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting the person name from previous screen
        Intent i = getIntent();
        String name = i.getStringExtra("Friend_name");
        String group_name = i.getStringExtra("friends");

        toolbar.setTitle(name);


        if(name==""){
            Toast.makeText(Chat.this, "Start chat with : " + group_name, Toast.LENGTH_SHORT).show();
        }
        if(group_name==""){
            Toast.makeText(Chat.this, "Start chat with : " + name, Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(Chat.this, "Start chat with : " + group_name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(Chat.this, "Start chat with : " + name, Toast.LENGTH_SHORT).show();

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


        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String msg = inputMsg.getText().toString();
                Message item = new Message();
                item.setMessage(msg);
                item.setFromName("you");
                item.setSelf(true);
                listMessages.add(item);

                adapter.notifyDataSetChanged();
                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<Message>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

    }


    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

            }
        });
    }
}
