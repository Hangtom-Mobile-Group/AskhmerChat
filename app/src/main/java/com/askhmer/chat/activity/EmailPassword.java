package com.askhmer.chat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.adapter.FriendAdapter;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.model.User;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmailPassword extends SwipeBackActivity {


    String user_id;
    private SharedPreferencesFile mSharedPrefer;

    private Button btn_save;

    private EditText edEmail;
    private EditText edoldpwd;
    private EditText ednewpwd;
    private EditText edconfirmpwd;

    private ImageButton btneditemail;
    private ImageButton btneditoldpwd;
    private ImageButton btneditnewpwd;
    private ImageButton btneditconfirmpwd;


    private ImageButton btndeleteemail;
    private ImageButton btndeleteoldpwd;
    private ImageButton btndeletenewpwd;
    private ImageButton btndeleteconfirmpwd;

    private TextView check_match;
    private TextView pwd_match;
    private TextView invalid,valid;


    private LinearLayout pwd;
    private LinearLayout newpwd;
    private LinearLayout confirmpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);


        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        /**
         *
         */

        checkPassword();
        pwd = (LinearLayout) findViewById(R.id.pwd);
        pwd.setVisibility(View.GONE);





        ScrollView sView = (ScrollView)findViewById(R.id.scrollView);
        sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);


        newpwd = (LinearLayout) findViewById(R.id.newpwd);
        confirmpwd = (LinearLayout) findViewById(R.id.confirmpwd);


        newpwd.setVisibility(View.GONE);
        confirmpwd.setVisibility(View.GONE);


        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setEnabled(false);

        edEmail = (EditText)findViewById(R.id.edEmail);
        edoldpwd = (EditText)findViewById(R.id.edoldpwd);
        ednewpwd = (EditText)findViewById(R.id.ednewpwd);
        edconfirmpwd = (EditText) findViewById(R.id.edconfirmpwd);

        btneditemail = (ImageButton)findViewById(R.id.btneditemail);
        btneditoldpwd = (ImageButton)findViewById(R.id.btneditoldpwd);
        btneditnewpwd = (ImageButton)findViewById(R.id.btneditnewpwd);
        btneditconfirmpwd = (ImageButton)findViewById(R.id.btneditconfirmpwd);

        check_match = (TextView) findViewById(R.id.warning_not_match);
        pwd_match = (TextView) findViewById(R.id.warning_match);
        pwd_match.setVisibility(View.GONE);

        valid = (TextView) findViewById(R.id.valid);
        invalid = (TextView) findViewById(R.id.invalid);
        valid.setVisibility(View.GONE);
        invalid.setVisibility(View.GONE);


        edconfirmpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = edconfirmpwd.getText().toString();
                String newpwd = ednewpwd.getText().toString();
                if (!pwd.equals(newpwd)) {
                    pwd_match.setVisibility(View.GONE);
                    check_match.setVisibility(View.VISIBLE);
                } else {
                    check_match.setVisibility(View.GONE);
                    pwd_match.setVisibility(View.VISIBLE);
                    btn_save.setEnabled(true);

                }
            }
        });




        edoldpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isvalidAuth();
            }
        });

        /**
         *  button save  password
         */

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd();
            }
        });



        btndeleteemail = (ImageButton) findViewById(R.id.btndeleteemail);
        btndeleteemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btneditemail.setVisibility(View.VISIBLE);
                edEmail.setText("");
                edEmail.setHint("Enter Your Email");
                btndeleteemail.setVisibility(View.GONE);
                edEmail.setEnabled(false);
                edEmail.requestFocus();

            }
        });
        btndeleteoldpwd = (ImageButton)findViewById(R.id.btndeleteoldpwd);
        btndeleteoldpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btneditoldpwd.setVisibility(View.VISIBLE);
                edoldpwd.setText("");
                edoldpwd.setHint("Old password");
                btndeleteoldpwd.setVisibility(View.GONE);
                edoldpwd.setEnabled(false);
                edoldpwd.requestFocus();
            }
        });

        btndeletenewpwd = (ImageButton)findViewById(R.id.btndeletenewpwd);
        btndeletenewpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btneditnewpwd.setVisibility(View.VISIBLE);
                ednewpwd.setText("");
                ednewpwd.setHint("new password");
                btndeletenewpwd.setVisibility(View.GONE);
                ednewpwd.setEnabled(false);
                ednewpwd.requestFocus();
            }
        });
        btndeleteconfirmpwd = (ImageButton)findViewById(R.id.btndeleteconfirmpwd);
        btndeleteconfirmpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btneditconfirmpwd.setVisibility(View.VISIBLE);
                edconfirmpwd.setText("");
                edconfirmpwd.setHint("Confirm password");
                btndeleteconfirmpwd.setVisibility(View.GONE);
                edconfirmpwd.setEnabled(false);
                edconfirmpwd.requestFocus();
            }
        });

        btneditemail.setOnClickListener(EditEmailClick);
        btneditoldpwd.setOnClickListener(EditNewpwdClick);
        btneditnewpwd.setOnClickListener(EditOldpwdClick);
        btneditconfirmpwd.setOnClickListener(EditconfirmpwdClick);

    }

    private  View.OnClickListener EditEmailClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(edEmail);
            edEmail.setHint("");
            btneditemail.setVisibility(View.GONE);
            btndeleteemail.setVisibility(View.VISIBLE);


        }
    };

    private  View.OnClickListener EditNewpwdClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(edoldpwd);
            edoldpwd.setHint("");
            btneditoldpwd.setVisibility(View.GONE);
            btndeleteoldpwd.setVisibility(View.VISIBLE);

        }
    };
    private  View.OnClickListener EditOldpwdClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(ednewpwd);
            ednewpwd.setHint("");
            btneditnewpwd.setVisibility(View.GONE);
            btndeletenewpwd.setVisibility(View.VISIBLE);
        }
    };
    private  View.OnClickListener EditconfirmpwdClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(edconfirmpwd);
            edconfirmpwd.setHint("");
            btneditconfirmpwd.setVisibility(View.GONE);
            btndeleteconfirmpwd.setVisibility(View.VISIBLE);

        }
    };


    private void editTextAction (EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //--check pwd match pwd



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_email_password, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.save:
//                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Saved!")
//                        .setContentText("You data was saved to server!")
//                        .show();
//                finish();
//                startActivity(getIntent());
                changePwd();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    /**
     * check password to show pwd box
     */

    public void checkPassword() {
        String url = "http://chat.askhmer.com/api/profile/getcurrentemailpassword/" + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        if(response.has("data")) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String Email = jsonArray.getJSONObject(i).getString("user_email");
                                String password = jsonArray.getJSONObject(i).getString("user_password");
                                Toast.makeText(EmailPassword.this, "PWD : "+ password + "Email :" + Email , Toast.LENGTH_SHORT).show();
                                if(!password.equals("")){
                                    pwd.setVisibility(View.VISIBLE);
                                    newpwd.setVisibility(View.GONE);
                                    confirmpwd.setVisibility(View.GONE);
                                }else{
                                    newpwd.setVisibility(View.VISIBLE);
                                    confirmpwd.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            Toast.makeText(EmailPassword.this, "No thing!!", Toast.LENGTH_SHORT).show();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "There is Something Wrong !!", Toast.LENGTH_LONG).show();
                Log.d("ravyerror", error.toString());
            }
        });
        // Add request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }


    /**
     * verify pwd and email
     */



    public void isvalidAuth() {

        String email = edEmail.getText().toString();
        String pwd =   edoldpwd.getText().toString();

            String url = "http://chat.askhmer.com/api/profile/isvalidauth/"+user_id+"?user_id=19&user_password="+pwd+"&user_email="+email;
            GsonObjectRequest jsonRequests = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("status")==200) {
                            Toast.makeText(EmailPassword.this, "email password valid", Toast.LENGTH_LONG).show();
                            invalid.setVisibility(View.GONE);
                            valid.setVisibility(View.VISIBLE);

                            newpwd.setVisibility(View.VISIBLE);
                            confirmpwd.setVisibility(View.VISIBLE);

                        }else{
                            valid.setVisibility(View.GONE);
                            invalid.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {


                    } finally {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
               public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getBaseContext(), "Hasaha  !!!!!" + error.toString(), Toast.LENGTH_LONG).show();
//                    valid.setVisibility(View.GONE);
//                    invalid.setVisibility(View.VISIBLE);
                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequests);
        }
    /**
     * chage password
     */

    public void changePwd() {

        String email = edEmail.getText().toString();
        String newpwd = edconfirmpwd.getText().toString();

        String url = "http://chat.askhmer.com/api/profile/modifyauthinfo/"+user_id+"?user_id="+user_id+"&user_password="+newpwd+"&user_email="+email;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("status")==200) {
                        Toast.makeText(EmailPassword.this, "Password changed", Toast.LENGTH_LONG).show();

                        btneditemail.setVisibility(View.VISIBLE);
                        edEmail.setText("");
                        edEmail.setHint("Enter Your Email");
                        btndeleteemail.setVisibility(View.GONE);
                        edEmail.setEnabled(false);
                        edEmail.requestFocus();

                        btneditoldpwd.setVisibility(View.VISIBLE);
                        edoldpwd.setText("");
                        edoldpwd.setHint("Old password");
                        btndeleteoldpwd.setVisibility(View.GONE);
                        edoldpwd.setEnabled(false);
                        edoldpwd.requestFocus();

                        btneditnewpwd.setVisibility(View.VISIBLE);
                        ednewpwd.setText("");
                        ednewpwd.setHint("new password");
                        btndeletenewpwd.setVisibility(View.GONE);
                        ednewpwd.setEnabled(false);
                        ednewpwd.requestFocus();

                        btneditconfirmpwd.setVisibility(View.VISIBLE);
                        edconfirmpwd.setText("");
                        edconfirmpwd.setHint("Confirm password");
                        btndeleteconfirmpwd.setVisibility(View.GONE);
                        edconfirmpwd.setEnabled(false);
                        edconfirmpwd.requestFocus();


                         check_match.setVisibility(View.GONE);
                         pwd_match.setVisibility(View.GONE);
                         invalid.setVisibility(View.GONE);
                         valid.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(EmailPassword.this, "Change Password not Success!!", Toast.LENGTH_LONG).show();
                } finally {

                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }


}



