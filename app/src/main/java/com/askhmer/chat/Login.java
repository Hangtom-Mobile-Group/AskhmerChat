package com.askhmer.chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {


    TextView tvFind;
    Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnsignup = (Button) findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(Login.this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
                final View textEntryView = factory.inflate(R.layout.signup_dialog, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.etemail);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.etpwd);
                final EditText input3 = (EditText) textEntryView.findViewById(R.id.cetpwd);


                input1.setText("", TextView.BufferType.EDITABLE);
                input2.setText("", TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                alert.setIcon(R.drawable.signup);
                alert.setTitle("SIGN UP").setView(textEntryView).setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                                Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());

                            }
                        }).setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        });
                alert.show();

            }
        });


        String text="Find my email or password.";
        TextView tvFind=(TextView)findViewById(R.id.tvFind);
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        tvFind.setText(spanString);
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Find...", Toast.LENGTH_LONG).show();
            }
        });

    }
}
