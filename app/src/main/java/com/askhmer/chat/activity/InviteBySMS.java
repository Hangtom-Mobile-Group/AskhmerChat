package com.askhmer.chat.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.util.CustomDialogSweetAlert;
import com.askhmer.chat.util.MutiLanguage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class InviteBySMS extends AppCompatActivity {

    private ArrayList<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private String subName;

    private RelativeLayout smsclearfocus;
    private EditText edtSearchID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_by_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smsclearfocus = (RelativeLayout)findViewById(R.id.smslayout);
        smsclearfocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsclearfocus.clearFocus();
            }
        });
        edtSearchID = (EditText)findViewById(R.id.edtSearchID);
        edtSearchID.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_btn), null);

        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            String id =  cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));

            Contact item = new Contact();
            Cursor phoneCursor =   resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

            subName = name.substring(0,1);

/*
            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            int randomColor = Color.rgb(r, g, b);
*/
            int[] androidColors = getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//             Log.i("INFO  : ",  id + " = " + name);

            while (phoneCursor.moveToNext()){
                String phoneNumber =   phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("INFO  : ", name + " = " + phoneNumber);

                item.setBgColor(randomAndroidColor);
                item.setSubName(subName);
                item.setName(name);
                item.setPhoneNumber(phoneNumber);
                contactList.add(item);
            }

        }

        //----------------------------------------------------
        CustomDialogSweetAlert.showLoadingProcessDialog(this);
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                CustomDialogSweetAlert.hideLoadingProcessDialog();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);

        //-------------------------------------------------------

        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase());
            }

        });
        recyclerView = (RecyclerView)findViewById(R.id.friend_in_contact);

        mAdapter = new ContactAdapter(contactList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}


