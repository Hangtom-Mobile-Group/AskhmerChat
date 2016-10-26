package com.askhmer.chat.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.util.DownloadImageTask;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPhoto extends AppCompatActivity{

    SubsamplingScaleImageView photo;
    private String path;
    private String profilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_photo);

        photo = (SubsamplingScaleImageView) findViewById(R.id.photo);
        final ImageButton downloadImg = (ImageButton) findViewById(R.id.download_imag);
        final ImageView successImg = (ImageView) findViewById(R.id.download_imag_success);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("image");
        }

        if (path.contains("thumnails")){
            profilePath = path.replace("thumnails", "user");
        }else{
            profilePath = path;
        }

        if (profilePath.contains("chat.askhmer.com") || profilePath.contains("graph.facebook.com")) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(ViewPhoto.this, photo);
            downloadImageTask.execute(profilePath);
        }else {
            downloadImg.setVisibility(View.GONE);
            photo.setImage(ImageSource.uri(profilePath));
        }

      //  photo.setImageURI(Uri.parse(new File("/storage/emulated/0/Pictures/limravy/image_2016_52_19_07_52.png").toString()));
        /*Picasso.with(getApplicationContext())
                .load(path)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.image_error)
                .fit()
                .centerInside()
                .into(imageView);*/
           /* Thread thread = new Thread() {
                public void run() {
                    Looper.prepare();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            photo.setImage(ImageSource.uri(getImageUri(ViewPhoto.this ,getBitmapFromURL(path))));
                            handler.removeCallbacks(this);
                            Looper.myLooper().quit();
                        }
                    }, 2000);

                    Looper.loop();
                }
            };
            thread.start();*/

       /* photo.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                System.out.println("matrix=" + savedMatrix.toString());
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("testing123", "ACTION_DOWN");
                        savedMatrix.set(matrix);
                        startPoint.set(event.getX(), event.getY());
                        mode = DRAG; break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.e("testing123", "ACTION_POINTER_DOWN");
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(midPoint, event);
                            mode = ZOOM;
                        } break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE; break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            }
                        } break;
                } view.setImageMatrix(matrix);
                return true;
            }
            @SuppressLint("FloatMath")
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) Math.sqrt(x * x + y * y);
            }
            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });*/




        downloadImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filename = null;

                File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Medayi_Chat");
                folder.mkdir();
                if (!folder.isDirectory() || !folder.exists()) {
                    folder.mkdir();
                }

                /*
                boolean success = (new File("/sdcard/Medayi")).mkdir();
                if (!success) {
                    Log.w("directory not created", "directory not created");
                    Toast.makeText(ViewPhoto.this, "Cannot save image to your phone directory.", Toast.LENGTH_SHORT).show();
                }*/

                try {
                    URL url = new URL(path);


                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    filename = folder.toString();
                    filename += "/" + System.currentTimeMillis()/1000 + ".jpg";

                    FileOutputStream stream = new FileOutputStream(filename);

                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
                    byte[] byteArray = outstream.toByteArray();

                    stream.write(byteArray);
                    stream.close();

                    downloadImg.setVisibility(View.GONE);
                    successImg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Downloading Completed!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
