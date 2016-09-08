package com.askhmer.chat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class Login extends AppCompatActivity {


    Context context;


    TextView tvFind;
    Button btnLogin, btnClearEmail, btnClearPWD;


    @NotEmpty(messageId = R.string.validation_empty)
    @RegExp(value = EMAIL, messageId = R.string.validation_valid_email)
    EditText etEmail;



    @NotEmpty(messageId = R.string.validation_empty)
    EditText etPwd;



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferencesFile mSharedPref;
    String user_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPref = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);

        btnLogin = (Button) findViewById(R.id.btn_log_in);
        tvFind = (TextView) findViewById(R.id.tv_Find);
        etEmail = (EditText) findViewById(R.id.et_email_log_in);
        etPwd = (EditText) findViewById(R.id.et_pwd_log_in);
        btnClearEmail = (Button) findViewById(R.id.btn_clear_email);
        btnClearPWD = (Button) findViewById(R.id.btn_clear_pwd);

        btnClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
            }
        });
        btnClearPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPwd.setText("");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });

        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Find...", Toast.LENGTH_LONG).show();
            }
        });
    }


    // validate while login

    private void validateLogin(){
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))){
            logIn();
        }
        FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        FormValidator.stopLiveValidation(this);
    }


    //  login
    private void logIn(){
        JSONObject param = new JSONObject();
        try {
            param.put("email",etEmail.getText().toString());
            param.put("password", etPwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        final SpotsDialog pDialog = new SpotsDialog(getActivity(), "កំពុងដំណើរការ");
//        pDialog.show();
        CustomDialogSweetAlert.showLoadingProcessDialog(Login.this);



        String url = API.LOGINEMAILPWD;
        //API.logInUrl
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST,url , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // If logIn Successfully
                    if (response.getBoolean("STATUS")){
                        String uId = response.getString("USERID");
                        mSharedPref.putStringSharedPreference(SharedPreferencesFile.USERIDKEY, uId);
                        user_id = mSharedPref.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                        Log.d("userId", user_id);
                        if (!user_id.equals("") || !user_id.equals(null)) {
                            CustomDialogSweetAlert.hideLoadingProcessDialog();
                            Intent intent = new Intent(Login.this, MainActivityTab.class);
                            startActivity(intent);
                            finish();
                        } else {
                            CustomDialogSweetAlert.showLoadingProcessDialog(Login.this);
                       }
                        Toast.makeText(getApplicationContext(), "successed", Toast.LENGTH_LONG).show();
                    }else{
                        CustomDialogSweetAlert.hideLoadingProcessDialog();
                        Toast.makeText(getApplicationContext(), "Invalid email or password!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  pDialog.dismiss();
                CustomDialogSweetAlert.hideLoadingProcessDialog();
                Toast.makeText(getApplicationContext(), R.string.check_internet, Toast.LENGTH_LONG).show();
            }
        });
        // Add request queue
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

}
