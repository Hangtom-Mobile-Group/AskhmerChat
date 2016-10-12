package com.askhmer.chat.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class ViewPhoto extends AppCompatActivity {

    ImageView photo;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_photo);


        photo = (ImageView)findViewById(R.id.photo);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("image");
        }
        Picasso.with(getApplicationContext())
                .load(path)
                .into(photo);

        photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean success = (new File("/sdcard/dirname")).mkdir();
                if (!success) {
                    Log.w("directory not created", "directory not created");
                }

                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    String data1 = String.valueOf(String.format("/sdcard/dirname/%d.jpg", System.currentTimeMillis()));

                    FileOutputStream stream = new FileOutputStream(data1);

                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
                    byte[] byteArray = outstream.toByteArray();

                    stream.write(byteArray);
                    stream.close();

                    Toast.makeText(getApplicationContext(), "Downloading Completed", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
