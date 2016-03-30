package com.askhmer.chat.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.VerifyCode;

import java.util.ArrayList;
import java.util.List;

public class PhoneLogIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    Button btnnext;
    EditText etPhnoeno;
    String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log_in);

        spinner1 = (Spinner) findViewById(R.id.spinner);
        btnnext = (Button) findViewById(R.id.btnnext);
        etPhnoeno = (EditText) findViewById(R.id.ephoneno);



        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneno = etPhnoeno.getText().toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PhoneLogIn.this);
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Are you sure to use this number?" + "\n\n" + phoneno);

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(PhoneLogIn.this, VerifyCode.class);
                        startActivity(intent);
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        spinner1.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Afghanistan\t93");
        categories.add("Albania\t\t355");
        categories.add("Algeria\t\t213");
        categories.add("American Samoa\t1-684");
        categories.add("Andorra\t\t376");
        categories.add("Angola\t\t244");
        categories.add("Anguilla\t\t1-264");
        categories.add("Antarctica\t\t672 ");
        categories.add("Antigua and Barbuda\t1-268");
        categories.add("Argentina\t\t54");
        categories.add("Armenia\t\t374");
        categories.add("Aruba\t\t297");
        categories.add("Australia\t\t61");
        categories.add("Austria\t\t43 ");
        categories.add("Azerbaijan\t994");
        categories.add("Bahamas\t\t1-242");
        categories.add("Bahrain\t\t973");
        categories.add("Bangladesh\t880");
        categories.add("Barbados\t\t1-246");
        categories.add("Belarus\t\t375");
        categories.add("Belgium\t\t32");
        categories.add("Belize\t\t501");
        categories.add("Benin\t\t229");
        categories.add("Bermuda\t\t1-441");
        categories.add("Bhutan\t\t975");
        categories.add("Bolivia\t\t591 ");
        categories.add("Bosnia and Herzegovina\t387");
        categories.add("Botswana\t\t267");
        categories.add("Brazil\t\t55");
        categories.add("British Indian Ocean Territory\t+246");
        categories.add("British Virgin Islands\t\t1-284");
        categories.add("Brunei\t\t+673");
        categories.add("Bulgaria\t\t+359");
        categories.add("Burkina Faso\t+226");
        categories.add("Burundi\t\t+257");
        categories.add("Cambodia\t\t+855");
        categories.add("Cameroon\t\t237");
        categories.add("Canada\t\t1");
        categories.add("Cape Verde\t238");
        categories.add("Cayman Islands\t1-345");
        categories.add("Central African Republic\t236");
        categories.add("Chad\t\t235");
        categories.add("Chile\t\t56");
        categories.add("China\t\t86");
        categories.add("Christmas Island\t61");
        categories.add("Cocos Islands\t61");
        categories.add("Comoros\t\t269");
        categories.add("Cook Islands\t682");
        categories.add("Costa Rica\t506");
        categories.add("Croatia\t\t385");
        categories.add("Cuba\t\t53");
        categories.add("Curacao\t\t599");
        categories.add("Cyprus\t\t357");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");
        categories.add("AAAAA");




        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
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
}
