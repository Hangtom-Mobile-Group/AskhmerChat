package com.askhmer.chat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.adapter.SearchUserNameOrNumApt;
import com.askhmer.chat.model.User;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.JsonConverter;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class SearchByID extends SwipeBackLib {

    private SearchUserNameOrNumApt searchUserIdApt = null;
    ArrayList<User> users = new ArrayList<User>();
    private SwipeBackLayout mSwipeBackLayout;


    private String countryCode;
    @NotEmpty(messageId = R.string.validation_empty)
    EditText et_search_id;
    EditText edPhoneno;
    Spinner spinnerCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_id);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Search Friends");

        SharedPreferencesFile mSharedPerfer = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        final String idUserUseApp = mSharedPerfer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

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

        //---todo radio

        //TextView tvNotFound = (TextView) findViewById(R.id.tvNotFound);

        RadioGroup toggle = (RadioGroup) findViewById(R.id.radio_search);
        int selectedId = toggle.getCheckedRadioButtonId();
        RadioButton radioBtnId = (RadioButton) findViewById(R.id.radio_id);
        RadioButton radioBtnPhone = (RadioButton) findViewById(R.id.radio_phone_no);

        final LinearLayout search_id_layout = (LinearLayout) findViewById(R.id.search_id_layout);
        final LinearLayout search_phone_layout = (LinearLayout) findViewById(R.id.search_phone_layout);

        Button btnSearchId = (Button) findViewById(R.id.btn_search_id);
        et_search_id = (EditText) findViewById(R.id.et_search_id);

        Button btnSearchPhone = (Button) findViewById(R.id.btn_search_phone);
        edPhoneno = (EditText) findViewById(R.id.ed_phone_no);



        spinnerCode = (Spinner) findViewById(R.id.spinner);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Cambodia  +855");
        categories.add("North Korea  +850");
        categories.add("United States  +1");
        categories.add("Thailand  +66");
        categories.add("Vietnam  +84");
        categories.add("Laos  +856");
        categories.add("Japan  +81");
        spinnerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                countryCode = item.replaceAll("[^\\.0123456789]", "");
                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCode.setAdapter(dataAdapter);



        search_phone_layout.setVisibility(View.GONE);

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_id) {
                    Log.d("id", "id");
                    search_phone_layout.setVisibility(View.GONE);
                    search_id_layout.setVisibility(View.VISIBLE);
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();

                } else {
                    Log.d("phone", "phone");
                    search_id_layout.setVisibility(View.GONE);
                    search_phone_layout.setVisibility(View.VISIBLE);
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();
                }
            }
        });


        btnSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--todo begin validate
                if(FormValidator.validate(SearchByID.this, new SimpleErrorPopupCallback(SearchByID.this))){
                String resultEditText = et_search_id.getText().toString();
                String url = "http://chat.askhmer.com/api/user/searchuser?userno="+resultEditText+"&userid="+idUserUseApp;
                if (!resultEditText.isEmpty()) {
                    GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("STATUS")==200) {
                                    List<User> luser = new JsonConverter().toList(response.getJSONArray("DATA").toString(), User.class);
                                    users.clear();
                                    users.addAll(luser);
                                    searchUserIdApt.notifyDataSetChanged();
                                }else{
                                    users.clear();
                                    searchUserIdApt.notifyDataSetChanged();
                                    Toast.makeText(SearchByID.this, "Dont't have this user!!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           /* Log.d("errorCustom", error.getMessage());*/
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
                } else {
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();
                }
            }
                //--todo end validate
                FormValidator.validate(SearchByID.this, new SimpleErrorPopupCallback(SearchByID.this));
                FormValidator.stopLiveValidation(this);
            }
        });




        btnSearchPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneno = edPhoneno.getText().toString();
                if (isValidMobile(phoneno)) {
                    String ind = String.valueOf(phoneno.charAt(0));
                    String fulPhoneNum = "";
                    if (ind.equals("0")) {
                        fulPhoneNum = countryCode + phoneno.substring(1);
                    } else {
                        fulPhoneNum = countryCode + phoneno;
                    }
                    String url = "http://chat.askhmer.com/api/user/searchuser?phonenumber=" + fulPhoneNum + "&userid=" + idUserUseApp;
                    GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("STATUS") == 200) {
                                    List<User> luser = new JsonConverter().toList(response.getJSONArray("DATA").toString(), User.class);
                                    users.clear();
                                    users.addAll(luser);
                                    searchUserIdApt.notifyDataSetChanged();
                                } else {
                                    users.clear();
                                    searchUserIdApt.notifyDataSetChanged();
                                    Toast.makeText(SearchByID.this, "Dont't have this user!!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           /* Log.d("errorCustom", error.getMessage());*/
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
                } else {
                    Toast.makeText(SearchByID.this, "Phone number format incorrect!!", Toast.LENGTH_SHORT).show();
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();
                }
            }
        });


        //--todo end

        final LinearLayout clearFocus;
        final EditText edtSearchID;
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Animation slide_up;

//        clearFocus = (LinearLayout)findViewById(R.id.clearFocus);
//        clearFocus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearFocus.clearFocus();
//            }
//        });

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        edtSearchID = (EditText)findViewById(R.id.edtSearchID);
        edtSearchID.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.magnifier), null, null, null);
        edtSearchID.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String resultEditText = edtSearchID.getText().toString();

                if (!resultEditText.isEmpty()) {
                    GsonObjectRequest gson = new GsonObjectRequest(Request.Method.POST, API.SEARCHUSER + resultEditText + "/" + idUserUseApp, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.has("USER_DETAIL")) {
                                    List<User> luser = new JsonConverter().toList(response.getJSONArray("USER_DETAIL").toString(),User.class);
                                    users.clear();
                                    users.addAll(luser);
                                    searchUserIdApt.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           /* Log.d("errorCustom", error.getMessage());*/
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gson);
                }else{
                    users.clear();
                    searchUserIdApt.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        searchUserIdApt = new SearchUserNameOrNumApt(users);
        recyclerView.setAdapter(searchUserIdApt);
    }




    private boolean isValidMobile(String phone)
    {
        String formatedPhNumber = edPhoneno.getText().toString();
        String newPhNumber = phone.replaceAll("[^\\.0123456789]", "");
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", formatedPhNumber))
        {
            if(newPhNumber.length() < 8 || newPhNumber.length() > 10)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }

}
