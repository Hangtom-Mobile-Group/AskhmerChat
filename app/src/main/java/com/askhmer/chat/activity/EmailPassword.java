package com.askhmer.chat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.askhmer.chat.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmailPassword extends SwipeBackActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);


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




        ScrollView sView = (ScrollView)findViewById(R.id.scrollView);
        sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);


        edEmail = (EditText)findViewById(R.id.edEmail);
        edoldpwd = (EditText)findViewById(R.id.edoldpwd);
        ednewpwd = (EditText)findViewById(R.id.ednewpwd);
        edconfirmpwd = (EditText) findViewById(R.id.edconfirmpwd);

        btneditemail = (ImageButton)findViewById(R.id.btneditemail);
        btneditoldpwd = (ImageButton)findViewById(R.id.btneditoldpwd);
        btneditnewpwd = (ImageButton)findViewById(R.id.btneditnewpwd);
        btneditconfirmpwd = (ImageButton)findViewById(R.id.btneditconfirmpwd);



        btndeleteemail = (ImageButton) findViewById(R.id.btndeleteemail);
        btndeleteemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btneditemail.setVisibility(View.VISIBLE);
                edEmail.setText("");
                edEmail.setHint("limravytop10@gmail.com");
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
                ednewpwd.setHint("Old password");
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
                edconfirmpwd.setHint("Old password");
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
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Saved!")
                        .setContentText("You data was saved to server!")
                        .show();
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }


}



