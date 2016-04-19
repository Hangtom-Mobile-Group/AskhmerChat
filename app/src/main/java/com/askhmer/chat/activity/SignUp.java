package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.askhmer.chat.R;

public class SignUp extends AppCompatActivity {

    Button btnClearName, btnClearEmail, btnClearPwd, btnClearConPwd;

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
                startActivity(in);
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
