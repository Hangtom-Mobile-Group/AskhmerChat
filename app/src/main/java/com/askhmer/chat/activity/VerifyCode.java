package com.askhmer.chat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.askhmer.chat.R;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.SmsBroadcastReceiver;

public class VerifyCode extends AppCompatActivity {

    TextView tvResent;
    Button btnNext, btnClear;
    EditText etVerifyCode;
    String verifynum;
    String reciever;
    String val;




    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferencesFile mSharedPref;
    private  Bundle extra;
    private String verifyBroadcast;

   // private BroadcastReceiver mybroadcast = new SmsBroadcastReceiver();
    String content;

    public class Receiver extends BroadcastReceiver{
        public static final String SMS_CONTENT = "sms_content";
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs;
                String sender;
                String msgBody;
                if (bundle != null) {
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            // Here you have the sender(phone number)
                            sender = msgs[i].getOriginatingAddress();
                             msgBody = msgs[i].getMessageBody();
                            // you have the sms content in the msgBody
                            if(sender.equals("Hangtom")) {
                                Intent fireActivityIntent = new Intent(context, VerifyCode.class);
                                fireActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                fireActivityIntent.putExtra(SMS_CONTENT, msgBody);
                                context.startActivity(fireActivityIntent);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        }

    }


    private  BroadcastReceiver mybroadcast = new Receiver();
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mybroadcast, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mybroadcast);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        mSharedPref = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);



        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("verifyno")!=null){
            verifynum = bundle.getString("verifyno");
            Toast.makeText(VerifyCode.this, verifynum, Toast.LENGTH_LONG).show();
        }



        btnNext = (Button) findViewById(R.id.btnnext);
        tvResent = (TextView)findViewById(R.id.tvResent);
        btnClear = (Button) findViewById(R.id.btn_clear);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_num);



        extra = getIntent().getExtras();
        if(extra.getString("sms_content")!=null){
            content = extra.getString("sms_content");
            String number =  content.substring(content.length() - 4);
            etVerifyCode.setText(number);
        }

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etVerifyCode.setText("");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifynum = mSharedPref.getStringSharedPreference(SharedPreferencesFile.VERIFYCODE);
              String inputVerifyCode =  etVerifyCode.getText().toString();
                if(verifynum.equals(inputVerifyCode)){
                Intent intent = new Intent(VerifyCode.this, SignUp.class);
                startActivity(intent);
                }else {
                    Toast.makeText(VerifyCode.this, "Your input not match verify code!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(VerifyCode.this, "sent request....", Toast.LENGTH_LONG).show();
                reciever = mSharedPref.getStringSharedPreference(SharedPreferencesFile.PHONENO);
                int randomPIN = (int) (Math.random() * 9000) + 1000;
                val = "" + randomPIN;
                Toast.makeText(VerifyCode.this, reciever +" "+val, Toast.LENGTH_LONG).show();
                sendSMS(reciever, val);
                mSharedPref.putStringSharedPreference(SharedPreferencesFile.PHONENO, reciever);
                mSharedPref.putStringSharedPreference(SharedPreferencesFile.VERIFYCODE, val);
            }
        });
    }


    //send SMS to client
    public void sendSMS(final String receiver,String verifycode){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://chat.askhmer.com/api/verify/phone_number/"+ receiver+"/"+verifycode;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("200")) {
                            Intent intent = new Intent(VerifyCode.this, VerifyCode.class);
                            intent.putExtra("verifyno", val);
                            startActivity(intent);
                            mSharedPref.putStringSharedPreference(SharedPreferencesFile.PHONENO, receiver);
                            mSharedPref.putStringSharedPreference(SharedPreferencesFile.VERIFYCODE, val);

                            Toast.makeText(VerifyCode.this, "request sucessed  :" + response, Toast.LENGTH_SHORT).show();
                            Log.d("respone", response);
                        } else {
                            Toast.makeText(VerifyCode.this, "request failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
