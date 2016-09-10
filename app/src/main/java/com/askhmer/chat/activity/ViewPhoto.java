package com.askhmer.chat.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.squareup.picasso.Picasso;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class ViewPhoto extends SwipeBackLib {

    ImageView photo;
    private String path;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        photo = (ImageView)findViewById(R.id.photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("image");
        }
        Picasso.with(getApplicationContext()).load(path).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(photo);
    }
}
