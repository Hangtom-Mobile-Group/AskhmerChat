package com.askhmer.chat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.CustomDialogSweetAlert;
import com.askhmer.chat.util.SharedPreferencesFile;

import org.json.JSONException;
import org.json.JSONObject;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

@SuppressLint("ValidFragment")
public class SignUp extends AppCompatActivity {



    private String user_id;

    private String name;
    private String email;
    private String pwd;
    private String cofPwd;
    private Boolean isSelectedMale;
    private Boolean isSelectedFemale;
    private String phoneNumber;
    private String gender;

    @NotEmpty(messageId = R.string.validation_empty)
    private EditText etName;

    private EditText etEmail;
    private EditText etPwd ;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button save;
    private EditText etcofPwd;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private Button btnClearName, btnClearEmail, btnClearPwd, btnClearConPwd;
    private TextView txtAdvance;
    private LinearLayout layoutAdv;
    private Animation animShow, animHide;

    private SharedPreferencesFile mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        initUI();

        mSharedPref = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);
        phoneNumber = mSharedPref.getStringSharedPreference(SharedPreferencesFile.PHONENO);

        btnClearName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
            }
        });

        btnClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
            }
        });

        btnClearPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPwd.setText("");
            }
        });

        btnClearConPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etcofPwd.setText("");
            }
        });

        txtAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutAdv.getVisibility() == View.GONE) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in);
                    layoutAdv.startAnimation(slide_up);
                    layoutAdv.setVisibility(View.VISIBLE);
                }else {
                    layoutAdv.setVisibility(View.GONE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPref.putBooleanSharedPreference(SharedPreferencesFile.PERFER_VERIFY_KEY, true);
                validateSignUp();
            }
        });

    }

    private void initUI(){
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etcofPwd = (EditText) findViewById(R.id.et_cof_pwd);
        rbMale = (RadioButton) findViewById(R.id.rb_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_female);

        btnClearName = (Button) findViewById(R.id.btn_clear_name);
        btnClearEmail = (Button) findViewById(R.id.btn_clear_email);
        btnClearPwd = (Button) findViewById(R.id.btn_clear_pwd);
        btnClearConPwd = (Button) findViewById(R.id.btn_clear_cof_pwd);
        txtAdvance = (TextView) findViewById(R.id.tv_advance);
        layoutAdv = (LinearLayout) findViewById(R.id.layout_adv);

        save = (Button) findViewById(R.id.btn_save);
    }

    // validate while login

    private void validateSignUp(){
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))){
            signUp();
        }
        FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        FormValidator.stopLiveValidation(this);
    }

    //sign up new user
    public void signUp(){

        if(rbMale.isChecked()){
            gender = "M";
        }else if(rbFemale.isChecked()){
            gender = "F";
        }
        JSONObject params;
        try {
            params = new JSONObject();
            params.put("userName", etName.getText().toString());
            params.put("gender",gender);
            params.put("userPhoneNum", phoneNumber);
            params.put("userEmail", etEmail.getText().toString());
            params.put("userPassword", etPwd.getText().toString());
            params.put("friend", true);

            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, API.ADDUSER, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS")==200) {
                            Log.d("love", response.toString());
                            int   uId =  response.getInt("MESSAGE_USERID");
                            mSharedPref.putStringSharedPreference(SharedPreferencesFile.USERIDKEY, String.valueOf(uId));
                            Toast.makeText(SignUp.this, "Your id :"+user_id, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUp.this, response.getString("STATUS"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(SignUp.this, "Unsuccessfully Signup !!", Toast.LENGTH_LONG).show();
                    } finally {

                        user_id = mSharedPref.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                        Log.d("userId", user_id);

                        if (!user_id.equals("") || !user_id.equals(null)) {
                            CustomDialogSweetAlert.hideLoadingProcessDialog();
                            Intent intent = new Intent(SignUp.this, MainActivityTab.class);
                            startActivity(intent);
                            finish();
                        } else {
                            CustomDialogSweetAlert.showLoadingProcessDialog(SignUp.this);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
        } catch (JSONException e) {
            Toast.makeText(SignUp.this, "ERROR_MESSAGE_JSONOBJECT" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SignUp.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
