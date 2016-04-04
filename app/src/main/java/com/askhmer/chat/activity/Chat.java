package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    String group_name = i.getStringExtra("groupName");
    String friends = i.getStringExtra("friends");

    Toast.makeText(Chat.this, "Start chat with : " + name, Toast.LENGTH_SHORT).show();
    Toast.makeText(Chat.this, "Start chat with group : " + group_name, Toast.LENGTH_SHORT).show();
    Toast.makeText(Chat.this, "friend name : " + friends, Toast.LENGTH_SHORT).show();

    if(name==""|| name==null){
        toolbar.setTitle(group_name);
        Toast.makeText(Chat.this, "Start chat with : " + group_name, Toast.LENGTH_SHORT).show();
    }
    if(group_name==""||group_name==null){
        toolbar.setTitle(name);
        Toast.makeText(Chat.this, "Start chat with group : " + friends, Toast.LENGTH_SHORT).show();
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
            item.setFromName(currentDateTime());
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

    listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view.findViewById(R.id.lbl_date_message);
            if (textView.getVisibility() == View.VISIBLE) {
                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                textView.startAnimation(slide_down);
                textView.setVisibility(View.INVISIBLE);
            }else {
               // textView.animate().translationY(view.getHeight()).alpha(1.0f).setDuration(300);
                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                textView.startAnimation(slide_up);
                textView.setVisibility(View.VISIBLE);
            }
        }
    });
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

    private String currentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm");
        Date date = new Date();
        return dateFormat.format(date).toUpperCase();
    }
}
