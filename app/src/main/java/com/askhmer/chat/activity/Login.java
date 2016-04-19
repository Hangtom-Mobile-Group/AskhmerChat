package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;

public class Login extends AppCompatActivity {


    TextView tvFind;
    Button btnLogin, btnClearEmail, btnClearPWD;
    EditText etEmail, etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent(Login.this, MainActivityTab.class);
                startActivity(intent);
            }
        });

        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Find...", Toast.LENGTH_LONG).show();
            }
        });

    }
}
