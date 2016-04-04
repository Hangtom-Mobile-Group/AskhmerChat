package com.askhmer.chat.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;

import java.util.ArrayList;
import java.util.List;


public class FourFragment extends Fragment {


    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));

            Contact item = new Contact();
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
//             Log.i("INFO  : ",  id + " = " + name);

            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("INFO  : ", name + " = " + phoneNumber);

                item.setName(name);
                item.setPhoneNumber(phoneNumber);
                contactList.add(item);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



            Log.d("Tab", "Tab4");
            // Inflate the layout for this fragment
            View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

//
//            ContentResolver resolver = getActivity().getContentResolver();
//            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
//
//                Contact item = new Contact();
//                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
////             Log.i("INFO  : ",  id + " = " + name);
//
//                while (phoneCursor.moveToNext()) {
//                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    Log.i("INFO  : ", name + " = " + phoneNumber);
//
//                    item.setName(name);
//                    item.setPhoneNumber(phoneNumber);
//                    contactList.add(item);
//                }
/*
            Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ? ", new String[] {id}, null);

            while (emailCursor.moveToNext()){

                String email =   emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                  Log.i("email :", email);
            }
*/

            recyclerView = (RecyclerView) fourFragmentView.findViewById(R.id.friend_in_contact);

            mAdapter = new ContactAdapter(contactList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            return fourFragmentView;

        }


}
