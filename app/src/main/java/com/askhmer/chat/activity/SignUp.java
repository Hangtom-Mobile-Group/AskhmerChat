package com.askhmer.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.askhmer.chat.R;

public class SignUp extends AppCompatActivity {

    Button btnClearName, btnClearEmail, btnClearPwd, btnClearConPwd;
    TextView txtAdvance;
    LinearLayout layoutAdv;
    private Animation animShow, animHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);


        final EditText etName = (EditText) findViewById(R.id.et_name);
        final EditText etEmail = (EditText) findViewById(R.id.et_email);
        final EditText etPwd = (EditText) findViewById(R.id.et_pwd);
        final EditText etcofPwd = (EditText) findViewById(R.id.et_cof_pwd);
        RadioButton rbMale = (RadioButton) findViewById(R.id.rb_male);
        RadioButton rbFemale = (RadioButton) findViewById(R.id.rb_female);

        Button later = (Button)findViewById(R.id.btn_later);
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
                    Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade_out);
                    layoutAdv.startAnimation(slide_down);
                    layoutAdv.setVisibility(View.GONE);
                }
            }
        });

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        String cofPwd = etcofPwd.getText().toString();
        Boolean isSelectedMale = rbMale.isChecked();
        Boolean isSelectedFemale = rbFemale.isChecked();

        final Intent in = new Intent(SignUp.this, MainActivityTab.class);
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(in);
            }
        });

    }
}
