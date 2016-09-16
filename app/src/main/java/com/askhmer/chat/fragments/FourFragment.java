package com.askhmer.chat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.ContactUs;
import com.askhmer.chat.activity.EmailPassword;
import com.askhmer.chat.activity.PhoneLogIn;
import com.askhmer.chat.activity.PrivacyStatement;
import com.askhmer.chat.activity.TermOfUse;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.introFragments.MyIntro;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.MutiLanguage;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FourFragment extends Fragment {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private String subName;
    LinearLayout profile;
    TextView txtLangauge;
    TextView txtLogout;
    TextView tvUserName;
    TextView  tvUserID;
    TextView txtchangeemailpwd;

    TextView txtContact;
    TextView txtHowToUseApp;
    TextView txtPrivacy;
    TextView txtTermOfUse;

    ImageView layout_round;

    String user_id;
    private SharedPreferencesFile mSharedPrefer;

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefer = SharedPreferencesFile.newInstance(getContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


//        Toast.makeText(getContext(),"This is user id :"+ user_id, Toast.LENGTH_SHORT).show();

/*
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
*/

       // getUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Tab", "Tab4");
        // Inflate the layout for this fragment
        final View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

        setHasOptionsMenu(true);

        profile = (LinearLayout) fourFragmentView.findViewById(R.id.profile);
        tvUserName = (TextView) fourFragmentView.findViewById(R.id.tvUserName);
        tvUserID = (TextView) fourFragmentView.findViewById(R.id.userID);
        layout_round = (ImageView) fourFragmentView.findViewById(R.id.layout_round);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), UserProfile.class);
                intent.putExtra("user_name",tvUserName.getText().toString());
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

        /* add by thoeurn */
        txtContact = (TextView) fourFragmentView.findViewById(R.id.contactus_textview);
        txtHowToUseApp = (TextView) fourFragmentView.findViewById(R.id.howtouseapp_textview);
        txtPrivacy = (TextView) fourFragmentView.findViewById(R.id.privacy_textview);
        txtTermOfUse = (TextView) fourFragmentView.findViewById(R.id.terms_textview);

        txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactUs.class);
                getActivity().startActivity(intent);
            }
        });
        txtHowToUseApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyIntro.class);
                intent.putExtra("fragmentSetting", "fragmentFour");
                getActivity().startActivity(intent);
            }
        });
        //intro screen here
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyStatement.class);
                getActivity().startActivity(intent);
            }
        });
        txtTermOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent
                        intent = new Intent(getActivity(), TermOfUse.class);
                getActivity().startActivity(intent);
            }
        });
        /* end add by thoeurn */

        txtLogout= (TextView) fourFragmentView.findViewById(R.id.txt_logout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();

                mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.USERIDKEY, null);
                mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.ACCESSTOKEN,null);

                mSharedPrefer.putBooleanSharedPreference(SharedPreferencesFile.PERFER_VERIFY_KEY, false);
                Intent  intent = new Intent(getContext(), PhoneLogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        getUserProfile();

        return  fourFragmentView;
    }



    private void getUserProfile(){
        String url = API.VIEWUSERPROFILE + user_id;
        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("DATA")) {
                        JSONObject object = response.getJSONObject("DATA");
                        tvUserName.setText(object.getString("userName"));
                        tvUserID.setText(object.getString("userNo"));
                        String path = object.getString("userPhoto");
//                        Picasso.with(getContext())
//                                .load(path)
//                                .placeholder(R.drawable.icon_user)
//                                .error(R.drawable.icon_user)
//                                .into(layout_round);

                        String str=path;
                        boolean found = str.contains("facebook");
                        Log.d("found", "Return : " + found);
                        String imgPaht1 = API.UPLOADFILE +path;
                        String imgPaht2 = path;
                        if( found == false){
                            Picasso.with(getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(layout_round);
                        }else{
                            Picasso.with(getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(layout_round);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "No Friend Found !", Toast.LENGTH_SHORT).show();
                    }}
                catch (JSONException e) {
                    e.printStackTrace();

                } finally {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(objectRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.menu_more, menu);
         super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * On selecting action bar icons
     * */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Take appropriate action for each action item click
//        switch (item.getItemId()) {
//            case R.id.menu_setting:
//               /* alertDiolag(getActivity());*/
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
