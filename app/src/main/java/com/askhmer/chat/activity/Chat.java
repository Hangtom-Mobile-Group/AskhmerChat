package com.askhmer.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.model.Message;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
