package com.askhmer.chat.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.askhmer.chat.R;
import com.askhmer.chat.network.API;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

public class ViewPhoto extends SwipeBackActivity {


    ImageView photo;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_photo);
        setDragEdge(SwipeBackLayout.DragEdge.TOP);
        photo = (ImageView)findViewById(R.id.photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("image");
        }
        Picasso.with(getApplicationContext()).load(path).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(photo);
    }
}
