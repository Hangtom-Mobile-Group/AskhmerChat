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
import com.askhmer.chat.util.SharedPreferencesFile;

public class VerifyCode extends AppCompatActivity {

    TextView tvResent;
    Button btnNext, btnClear;
    EditText etVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        btnNext = (Button) findViewById(R.id.btnnext);
        tvResent = (TextView)findViewById(R.id.tvResent);
        btnClear = (Button) findViewById(R.id.btn_clear);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_num);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etVerifyCode.setText("");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesFile.putBooleanSharedPreference(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME, SharedPreferencesFile.PERFER_VERIFY_KEY, true);
                Intent intent = new Intent(VerifyCode.this, SignUp.class);
                startActivity(intent);

            }
        });

        tvResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyCode.this,"send...",Toast.LENGTH_LONG).show();
            }
        });


    }
}
