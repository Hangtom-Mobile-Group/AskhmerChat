package com.askhmer.chat.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.askhmer.chat.R;
`import com.askhmer.chat.util.DownloadImageTask;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ViewPhoto extends AppCompatActivity{

    SubsamplingScaleImageView photo;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_photo);

       photo = (SubsamplingScaleImageView) findViewById(R.id.photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("image");
        }

        if (path.contains("chat.askhmer.com")) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(ViewPhoto.this, photo);
            downloadImageTask.execute(path);
        }else {
            photo.setImage(ImageSource.uri(path));
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



/*
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
*/

    }
}
