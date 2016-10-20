package com.askhmer.chat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.BitmapEfficient;
import com.askhmer.chat.util.MultipartUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {

    private static final int TAKE_PICTURE_REQUEST_A = 100;
    private Button mSaveImageButton;

    //--todo send image----------------------------------------------------------------------
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private String imgUrl;
    private String imagePath;
    private String[] uploadImgPath;
    private String picturePath = null;
    private static int RESULT_LOAD_IMAGE_PROFILE = 1;
    private boolean isChangeProfileImage;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    String img_path_send;

    //----------------------------------------------------------------------------------------

    public static final String EXTRA_CAMERA_DATA = "camera_data";
    private static final String KEY_IS_CAPTURING = "is_capturing";

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private byte[] mCameraData;
    private boolean mIsCapturing;

    //---------------------------------------------------------------------------------------
    LinearLayout top_layout;
    RelativeLayout bottom_layout;
    LinearLayout cancel_layout;
    LinearLayout send_layout;
    Button btnCancel;
    Button btnUpload;

    int camNum = Camera.getNumberOfCameras();
    int camBackId = Camera.CameraInfo.CAMERA_FACING_BACK;
    int camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    //flag to detect flash is on or off
    private boolean isLighOn = false;


    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        captureImage();
                    }
                }
            });
        }
    };

    private OnClickListener mRecaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
        }
    };

    private OnClickListener mDoneButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCameraData != null) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

          /*btn_buttom*/
        ImageView marketBtn = (ImageView)findViewById(R.id.market_btn);
        marketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this,WebViewMaket.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ImageView newsBtn = (ImageView)findViewById(R.id.timeline_btn);
        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this,WebViewTimeLine.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        top_layout = (LinearLayout) findViewById(R.id.top_layout);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        cancel_layout = (LinearLayout) findViewById(R.id.cancel_layout);
        send_layout = (LinearLayout) findViewById(R.id.send_layout);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnUpload = (Button) findViewById(R.id.btnUpload);

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent in = new Intent(CameraActivity.this, CameraActivity.class);
                startActivity(in);
            }
        });


        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        mCameraImage.setVisibility(View.INVISIBLE);

        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureImageButton = (Button) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        final Button doneButton = (Button) findViewById(R.id.btnDone);
        doneButton.setOnClickListener(mDoneButtonClickListener);

        //--------save-----------
        mSaveImageButton = (Button) findViewById(R.id.btnUpload);
        mSaveImageButton.setOnClickListener(mSaveImageButtonClickListener);
        // mSaveImageButton.setEnabled(false);
        //-------------------------

        mIsCapturing = true;
        

        mCameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                        }
                    }
                });
                return false;
            }
        });


        //---------------------------------------------------
        Button otherCamera = (Button) findViewById(R.id.switch_camera);
        otherCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCapturing) {
                    mCamera.stopPreview();
                }
                //NB: if you don't release the current camera before switching, you app will crash
                mCamera.release();

                //swap the id of the camera to be used
                if (camNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camNum = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    camNum = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                mCamera = Camera.open(camNum);

                setCameraDisplayOrientation(CameraActivity.this, camNum, mCamera);
                try {

                    mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();


            }
        });

        //-----------------------------------------------------

        Context context = this;
        PackageManager pm = context.getPackageManager();

        // if device support camera?
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            return;
        }

        mCamera = Camera.open();
        final Camera.Parameters p = mCamera.getParameters();

        Button buttonFlashlight = (Button) findViewById(R.id.buttonFlashlight);
        buttonFlashlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isLighOn) {
                    Camera cam = Camera.open();
                    Camera.Parameters p = cam.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    isLighOn = false;

                } else {
                    Log.i("info", "torch is turn on!");
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(p);
                    mCamera.startPreview();
                    isLighOn = true;
                }


            }
        });

        //---------------Setting autofocus
        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setRotation(90);
        mCamera.setParameters(params);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCameraData = data;
        setupImageDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {

            if (camNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camNum = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else {
                camNum = Camera.CameraInfo.CAMERA_FACING_BACK;
            }

            setCameraDisplayOrientation(CameraActivity.this, camNum, mCamera);

            try {
                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void captureImage() {
        mCamera.takePicture(null, null, this);
    }

    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        //mCaptureImageButton.setText(R.string.capture_image);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        //mCameraImage.setImageBitmap(bitmap);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bitmap1 = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        mCameraImage.setImageBitmap(bitmap1);

        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
        //  mCaptureImageButton.setText(R.string.recapture_image);
        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);

        top_layout.setVisibility(View.GONE);
        bottom_layout.setVisibility(View.GONE);
        cancel_layout.setVisibility(View.VISIBLE);
        send_layout.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mCamera != null) {
            mCamera.release();
        }
    }

    /**
     * todo upload image
     */

    // upload image process background------------------------------------------------------
    private class UploadTask extends AsyncTask<String, Void, Void> {
        String url = API.UPLOADIMAGE;
        String charset = "UTF-8";
        String responseContent = null;
        File file = null;

        @Override
        protected Void doInBackground(String... params) {
            sendFileToServer(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            file = BitmapEfficient.persistImage(bitmap, getApplicationContext());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (responseContent != null) {
                try {
                    JSONObject object = new JSONObject(responseContent);
                    if (object.getBoolean("STATUS") == true) {
                        imgUrl = object.getString("IMG");
                        uploadImgPath = imgUrl.split("file/");
                        imagePath = uploadImgPath[1];
                        Log.i("upload_image", "http://chat.askhmer.com/resources/upload/file/" + imagePath);
                        img_path_send = "http://chat.askhmer.com/resources/upload/file/" + imagePath;
                        Toast.makeText(CameraActivity.this, "Upload Successfully !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(CameraActivity.this, "Uploaded Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // upload large file size
        public void sendFileToServer(String filePath) {
            try {
                MultipartUtility multipart = new MultipartUtility(url, charset);
                multipart.addFilePart("image", file);
                List<String> response = multipart.finish();
                Log.e("UploadProcess", response.toString());
                for (String line : response) {
                    if (line != null) {
                        responseContent = line;
                        break;
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

    }

    //--end upload image process background--------------------------------------------------


    //====================================================================================
    //================todo save image to gallery==========================================
    //===================================================================================

    private OnClickListener mSaveImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
            onActivityResult(TAKE_PICTURE_REQUEST_A,RESULT_OK,intent);


            //---upload to server
            new UploadTask().execute(picturePath);

            //---save to gallery
            File saveFile = openFileForImage();
            if (saveFile != null) {
                saveImageToFile(saveFile);
            } else {
                Toast.makeText(CameraActivity.this, "Unable to open file for saving image.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST_A) {
            if (resultCode == RESULT_OK) {
                // Recycle the previous bitmap.
                if (bitmap1 != null) {
                    bitmap1.recycle();
                    bitmap1 = null;
                }
                Bundle extras = data.getExtras();
                // mCameraBitmap = (Bitmap) extras.get("data");
                byte[] cameraData = extras.getByteArray(CameraActivity.EXTRA_CAMERA_DATA);
                if (cameraData != null) {
                    bitmap1 = BitmapFactory.decodeByteArray(cameraData, 0, cameraData.length);
                    //mCameraImageView.setImageBitmap(bitmap);
                    mSaveImageButton.setEnabled(true);
                }
            } else {
                bitmap1 = null;
                mSaveImageButton.setEnabled(false);
            }
        }
    }

    private File openFileForImage() {
        File imageDirectory = null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            imageDirectory = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "limravy");
            if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
                imageDirectory = null;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm",
                        Locale.getDefault());

                return new File(imageDirectory.getPath() +
                        File.separator + "image_" +
                        dateFormat.format(new Date()) + ".png");
            }
        }
        return null;
    }

    private void saveImageToFile(File file) {
        if (bitmap1 != null) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(file);
                if (!bitmap1.compress(Bitmap.CompressFormat.PNG, 100, outStream)) {
                    Toast.makeText(CameraActivity.this, "Unable to save image to file.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CameraActivity.this, "Saved image to: " + file.getPath(),
                            Toast.LENGTH_LONG).show();
                    Log.i("uri",file.getPath().toString());
                }
                outStream.close();
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to save image to file.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    //====================================================================================
    //================ todo end  save image to gallery==========================================
    //===================================================================================
}
