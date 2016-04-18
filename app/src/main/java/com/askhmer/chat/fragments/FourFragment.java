package com.askhmer.chat.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.util.CustomDialogSweetAlert;
import com.askhmer.chat.util.MutiLanguage;
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
        CustomDialogSweetAlert.showLoadingProcessDialog(getActivity());
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

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_setting:
                alertDiolag(getContext());
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void alertDiolag(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.alert_dialog_change_language);

        final Switch toggle = (Switch) dialog.findViewById(R.id.toggle_button);

        final MutiLanguage mutiLanguage = new MutiLanguage(getContext(),getActivity());
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            toggle.setChecked(false);
        }else {
            toggle.setChecked(true);
        }

        dialog.findViewById(R.id.save_change_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle.isChecked()) {
                    mutiLanguage.setLanguage("km");
                } else {
                    mutiLanguage.setLanguage("en");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
