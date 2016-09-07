package com.askhmer.chat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
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
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

@SuppressLint("ValidFragment")
public class SignUp extends AppCompatActivity {

    Button btnClearName, btnClearEmail, btnClearPwd, btnClearConPwd;
    TextView txtAdvance;
    LinearLayout layoutAdv;
    private Animation animShow, animHide;


     @NotEmpty(messageId = R.string.validation_empty)
     EditText etName;

    @NotEmpty(messageId = R.string.validation_empty)
    @RegExp(value = EMAIL, messageId = R.string.validation_valid_email)
     EditText etEmail;

    @NotEmpty(messageId = R.string.validation_empty)
     EditText etPwd ;

    @NotEmpty(messageId = R.string.validation_empty)
    EditText etcofPwd;

    RadioButton radioButton;
    RadioGroup radioGroup;

    String user_id;



    String name;
    String email;
    String pwd;
    String cofPwd;
    Boolean isSelectedMale;
    Boolean isSelectedFemale;

    RadioButton rbMale;
    RadioButton rbFemale;
    String gender;

    private SharedPreferencesFile mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mSharedPref = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);

         etName = (EditText) findViewById(R.id.et_name);
         etEmail = (EditText) findViewById(R.id.et_email);
         etPwd = (EditText) findViewById(R.id.et_pwd);
              etcofPwd = (EditText) findViewById(R.id.et_cof_pwd);
        final RadioButton rbMale = (RadioButton) findViewById(R.id.rb_male);
        final RadioButton rbFemale = (RadioButton) findViewById(R.id.rb_female);
//        Button later = (Button)findViewById(R.id.btn_later);
        Button save = (Button) findViewById(R.id.btn_save);

        btnClearName = (Button) findViewById(R.id.btn_clear_name);
        btnClearEmail = (Button) findViewById(R.id.btn_clear_email);
        btnClearPwd = (Button) findViewById(R.id.btn_clear_pwd);
        btnClearConPwd = (Button) findViewById(R.id.btn_clear_cof_pwd);
        txtAdvance = (TextView) findViewById(R.id.tv_advance);
        layoutAdv = (LinearLayout) findViewById(R.id.layout_adv);

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

         name = etName.getText().toString();
        email = etEmail.getText().toString();
        pwd = etPwd.getText().toString();
        cofPwd = etcofPwd.getText().toString();
        isSelectedMale = rbMale.isChecked();
        isSelectedFemale = rbFemale.isChecked();

        final Intent in = new Intent(SignUp.this, MainActivityTab.class);
/*
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage(R.string.information_massage_later);
                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(in);
                    }
                });

                alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPref.putBooleanSharedPreference(SharedPreferencesFile.PERFER_VERIFY_KEY, true);

//                signUp();
                //startActivity(in);
                validateSignUp();
            }
        });

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

        if(!etPwd.getText().toString().equals(etcofPwd.getText().toString())){
            Toast.makeText(SignUp.this, "Passwrod not match!!!", Toast.LENGTH_SHORT).show();
        }else{

            JSONObject params;
            try {
                params = new JSONObject();
                params.put("userName", etName.getText().toString());
                params.put("gender","M");
                params.put("userPhoto", "");
                params.put("userEmail", etEmail.getText().toString());
                params.put("userPassword", etPwd.getText().toString());
                params.put("friend", true);

                String url = "http://chat.askhmer.com/api/user/add";
                GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

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



}
