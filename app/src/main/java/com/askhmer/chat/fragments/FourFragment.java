package com.askhmer.chat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.EmailPassword;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.util.CustomDialogSweetAlert;
import com.askhmer.chat.util.MutiLanguage;

import java.util.ArrayList;


public class FourFragment extends Fragment {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private String subName;
    LinearLayout profile;
    TextView txtLangauge;
    TextView txtLogout;
    TextView txtchangeemailpwd;

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Tab", "Tab4");
        // Inflate the layout for this fragment
        View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

        setHasOptionsMenu(true);

        profile = (LinearLayout) fourFragmentView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), UserProfile.class);
                startActivity(intent);
            }
        });

        txtLangauge = (TextView) fourFragmentView.findViewById(R.id.txt_switch_langauge);
        txtLangauge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDiolag(getActivity());
            }
        });


        txtchangeemailpwd = (TextView) fourFragmentView.findViewById(R.id.txtchangeemailpwd);
        txtchangeemailpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmailPassword.class);
                getActivity().startActivity(intent);
            }
        });

        txtLogout= (TextView) fourFragmentView.findViewById(R.id.txt_logout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        return  fourFragmentView;
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
               /* alertDiolag(getActivity());*/
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

        final RadioGroup toggle = (RadioGroup) dialog.findViewById(R.id.radio_language);
        final int selectedId = toggle.getCheckedRadioButtonId();
        RadioButton radioBtnEn = (RadioButton)dialog.findViewById(R.id.radio_english);
        RadioButton radioBtnKm = (RadioButton)dialog.findViewById(R.id.radio_khmer);

        final MutiLanguage mutiLanguage = new MutiLanguage(getContext(),getActivity());
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            radioBtnEn.setChecked(true);
        }else {
            radioBtnKm.setChecked(true);
        }

        dialog.findViewById(R.id.save_change_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle.getCheckedRadioButtonId() == R.id.radio_khmer) {
                    mutiLanguage.setLanguage("km");
                } else {
                    mutiLanguage.setLanguage("en");
                }
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
