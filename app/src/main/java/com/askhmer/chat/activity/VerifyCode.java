package com.askhmer.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;

public class VerifyCode extends AppCompatActivity {

    TextView tvResent;
    Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        btnnext = (Button) findViewById(R.id.btnnext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VerifyCode.this,SignUp.class);
                startActivity(intent);

            }
        });


        TextView tvResent=(TextView)findViewById(R.id.tvResent);


        tvResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyCode.this,"send...",Toast.LENGTH_LONG).show();
            }
        });





    }
}
