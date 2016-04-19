package com.askhmer.chat.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class SearchByID extends SwipeBackActivity {

    RelativeLayout clearFocus;
    EditText edtSearchID;
    Button btnSearchID;
    TextView txtNotFound;
    String friendID = "12345";
    Animation slide_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_id);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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



        clearFocus = (RelativeLayout)findViewById(R.id.clearFocus);
        clearFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus.clearFocus();
            }
        });

        btnSearchID = (Button)findViewById(R.id.accessFriend);
        btnSearchID.setVisibility(View.GONE);
        txtNotFound = (TextView)findViewById(R.id.txtNotFound);
        txtNotFound.setVisibility(View.GONE);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        edtSearchID = (EditText)findViewById(R.id.edtSearchID);
        edtSearchID.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_btn), null);
        edtSearchID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getRawX() >= edtSearchID.getRight() - edtSearchID.getTotalPaddingRight()) {
                    if(edtSearchID.getText().toString().equals(friendID)) {
                        btnSearchID.startAnimation(slide_up);
                        txtNotFound.setVisibility(View.GONE);
                        btnSearchID.setVisibility(View.VISIBLE);
                        btnSearchID.setText("Bunthoeurn");
                        btnSearchID.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Hits", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        txtNotFound.startAnimation(slide_up);
                        btnSearchID.setVisibility(View.GONE);
                        txtNotFound.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
