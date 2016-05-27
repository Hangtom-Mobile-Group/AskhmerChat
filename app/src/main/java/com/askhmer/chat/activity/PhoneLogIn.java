package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneLogIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    Button btnnext,btnLogin, btnClear;
    EditText etPhnoeno;
    TextView temp;
    String phoneno;

    private LoginButton btnfb;
    private AccessToken accessToken;
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferencesFile mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_FILE_NAME);

        /*initialize facebook*/
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log_in);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        btnnext = (Button) findViewById(R.id.btnnext);
        etPhnoeno = (EditText) findViewById(R.id.et_phone_no);
        btnLogin = (Button) findViewById(R.id.btn_log_in_with_email);
        btnClear = (Button) findViewById(R.id.btn_clear_num);
        temp = (TextView) findViewById(R.id.tempId);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhnoeno.setText("");
            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneno = etPhnoeno.getText().toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PhoneLogIn.this);
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage(getApplicationContext().getString(R.string.use_this_number) + "\n\n" + phoneno);

                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(PhoneLogIn.this, VerifyCode.class);
                        startActivity(intent);
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PhoneLogIn.this, Login.class);
                startActivity(in);
            }
        });

        spinner1.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Cambodia          +855");
        categories.add("North Korea       +850");
        categories.add("United States     +1");
        categories.add("Thailand          +66");
        categories.add("Vietnam           +84");
        categories.add("Laos              +856");
        categories.add("Japan             +81");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);


        sharedPreferences = this.getSharedPreferences("accessTokenFB", 0);
        editor = sharedPreferences.edit();

        btnfb = (LoginButton)findViewById(R.id.btnfb);
        //btnfb.setReadPermissions("user_friends");
        btnfb.setReadPermissions(Arrays.asList("user_friends", "user_hometown", "user_location", "public_profile", "email", "user_birthday"));
        btnfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Gson gson = new Gson();
                String json = gson.toJson(accessToken);
                editor.putString("dataAccessToken", json);
                editor.commit();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    JSONObject hometown = object.getJSONObject("hometown");
                                    String town;
                                    if (hometown != null) {
                                         town = hometown.getString("name");
                                    }else {
                                        town = "";
                                    }

                                    JSONObject locations = object.getJSONObject("location");
                                    String location;
                                    if (locations != null) {
                                        location = locations.getString("name");
                                    }else {
                                        location = "";
                                    }

                                    String name = object.getString("name");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format

                                    String id = object.getString("id");
                                    String gender = object.getString("gender");
                                    String email = object.getString("email");

                                    addUser(name, gender, email, town, location, id, loginResult.getAccessToken().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,email,birthday,hometown,location");
                request.setParameters(parameters);
                request.executeAsync();

                mSharedPref.putBooleanSharedPreference(SharedPreferencesFile.PERFER_VERIFY_KEY, true);
                Intent intent = new Intent(PhoneLogIn.this, MainActivityTab.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.i("status", "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("status", "Error");
            }
        });
    }

    /*facebook override function*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
       String str ;
        str = item.replaceAll("[^\\.0123456789]","");
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void printHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.askhmer.chat",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void addUser(String name, String gender, String email, String town, String location, String id, String accessToken){
        JSONObject params = new JSONObject();
        try {
            params.put("userName", name);

            if (gender.equals("male")){
                gender = "M";
            }else{
                gender = "F";
            }

            params.put("gender", gender);
            params.put("userPhoto", "https://graph.facebook.com/"+id+"/picture?width=500&height=500");
            params.put("userEmail", email);
            params.put("userHometown", town);
            params.put("userCurrentCity", location);
            params.put("facebookId", id);
            params.put("userAccessToken", accessToken);

            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, API.FBSIGNUP, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getString("STATUS").equals("200")) {
                            String uId = response.getString("MESSAGE_USERID");
                            mSharedPref.putStringSharedPreference(SharedPreferencesFile.USERIDKEY, uId);

                        }else{
                            Toast.makeText(PhoneLogIn.this, response.getString("STATUS"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
