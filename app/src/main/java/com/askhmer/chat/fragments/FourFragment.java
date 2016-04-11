package com.askhmer.chat.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.util.CustomDialog;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;



public class FourFragment extends Fragment {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private String subName;
    LinearLayout profile;

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver resolver = getActivity().getContentResolver();
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
        CustomDialog.showProgressDialog(getActivity());
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
               CustomDialog.hideProgressDialog();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Tab", "Tab4");
        // Inflate the layout for this fragment
        View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

        setHasOptionsMenu(true);



        RecyclerViewHeader header = (RecyclerViewHeader) fourFragmentView.findViewById(R.id.header);

        profile = (LinearLayout) fourFragmentView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), UserProfile.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView) fourFragmentView.findViewById(R.id.friend_in_contact);

        mAdapter = new ContactAdapter(contactList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        header.attachTo(recyclerView, true);


        return fourFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.menu_more, menu);
         super.onCreateOptionsMenu(menu, inflater);
    }

}