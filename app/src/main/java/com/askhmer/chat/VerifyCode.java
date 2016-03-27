package com.askhmer.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                Intent intent = new Intent(VerifyCode.this,Login.class);
                startActivity(intent);
            }
        });




        String text="Re-enter Phone Number";
        TextView tvResent=(TextView)findViewById(R.id.tvResent);
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        tvResent.setText(spanString);

        tvResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyCode.this,"send...",Toast.LENGTH_LONG).show();
            }
        });



    }
}
