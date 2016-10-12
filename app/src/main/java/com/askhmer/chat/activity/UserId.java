package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONException;
import org.json.JSONObject;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class UserId extends AppCompatActivity {

    private String user_id;
    private SharedPreferencesFile mSharedPrefer;
    private TextView tvFirstShow;
    private TextView tvError;
    private TextView tvSuccess;
    private Button calc_clear_txt_PriseButton;
    @NotEmpty(messageId = R.string.validation_empty)
    private TextView edUserId;
    private int char_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);
        mSharedPrefer = SharedPreferencesFile.newInstance(UserId.this, SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("User ID");

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



         tvFirstShow = (TextView) findViewById(R.id.tvFirstShow);
         tvError = (TextView) findViewById(R.id.tvError);
         tvSuccess = (TextView) findViewById(R.id.tvSuccess);



        edUserId = (TextView)findViewById(R.id.edUserId);
        calc_clear_txt_PriseButton= (Button) findViewById(R.id.calc_clear_txt_Prise);
        final TextView tvNum = (TextView) findViewById(R.id.tvNum);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        if (edUserId != null) {
            edUserId.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {

                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        AlertDialog.Builder msg = new AlertDialog.Builder(UserId.this);
                        msg.setMessage("ID not allow space");
                        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        msg.show();
                        edUserId.setText(result);
                    }

                    char_num = edUserId.getText().toString().length();
                    tvNum.setText(char_num + "/20");
                    if (char_num >= 1) {
                        calc_clear_txt_PriseButton.setVisibility(View.VISIBLE);
                    } else {
                        calc_clear_txt_PriseButton.setVisibility(View.GONE);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }

        calc_clear_txt_PriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUserId.setText("");
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_no = edUserId.getText().toString();
                //--todo begin validate
                if (FormValidator.validate(UserId.this, new SimpleErrorPopupCallback(UserId.this))) {

                    if (char_num >= 4) {
                        updateUserno(user_no);
                    } else {
                        AlertDialog.Builder msg = new AlertDialog.Builder(UserId.this);
                        msg.setMessage("Please input from 4-20 charactor.");
                        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        msg.show();
                    }
                    //--todo end validate
                    FormValidator.validate(UserId.this, new SimpleErrorPopupCallback(UserId.this));
                    FormValidator.stopLiveValidation(this);
                }
            }
        });
    }



    //---todo add user and roomid to table seen
    public void updateUserno(String user_no) {
        try {
            String url ="http://chat.askhmer.com/api/user/updateuserno?userid="+user_id+"&userno="+user_no;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("STATUS")==true) {
                                    Log.d("updateUserNo", response.toString());
                                    tvFirstShow.setVisibility(View.GONE);
                                    tvError.setVisibility(View.GONE);
                                    tvSuccess.setVisibility(View.VISIBLE);
                                    success();
                                }else{
                                    tvFirstShow.setVisibility(View.GONE);
                                    tvSuccess.setVisibility(View.GONE);
                                    tvError.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(UserId.this, "Unsuccessfully Updated !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UserId.this, "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(UserId.this).addToRequestQueue(jsonRequest);
        } catch (Exception e) {
            Toast.makeText(UserId.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //--------------------------------------------------------------------


    // sweet alert for success
    public void success(){
        AlertDialog.Builder msg = new AlertDialog.Builder(UserId.this);
        msg.setMessage("Successed");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UserId.this,UserProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish(); // Call once you redirect to another activity
            }
        });
        msg.show();
    }

}
