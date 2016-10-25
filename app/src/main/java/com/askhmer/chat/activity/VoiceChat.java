package com.askhmer.chat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.util.SaveUserAsyntaskAudio;
import com.askhmer.chat.util.ScreenUtils;
import com.askhmer.chat.util.VoiceView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VoiceChat extends Fragment implements VoiceView.OnRecordListener{

    private TextView txtTimer;
    private TextView txtRecorder;

    private MediaRecorder mediaRecorder = null;
    private static String getMediaName = null;
    private  Context context;

    private int seconds;
    private int minute;
    private MessagesListAdapter messagesListAdapter;

    private int isCancel=0;

    private TextView mTextView;
    private VoiceView mVoiceView;
    //private MediaRecorder mMediaRecorder;
    private Handler mHandler;

    private boolean mIsRecording = false;

    private Rect rect;

    private RelativeLayout rootLayout;

    public static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.RECORD_AUDIO",
    };

    Timer timer;

    public VoiceChat(MessagesListAdapter messagesListAdapter){
        this.messagesListAdapter = messagesListAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voicer, container, false);

        txtTimer = (TextView)v.findViewById(R.id.btnTimer);
        mVoiceView = (VoiceView) v.findViewById(R.id.voiceview);
        rootLayout = (RelativeLayout)v.findViewById(R.id.root_layout);

        mVoiceView.setOnRecordListener(this);

        mVoiceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        rootLayout.setBackgroundColor(Color.WHITE);
                        timer.cancel();
                        if (seconds < 1) {
                            seconds = 0;
                            minute = 0;

                            if (getMediaName != null) {
                                try {
                                    stopRecording();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }
                        if (seconds > 0) {
                            if (mediaRecorder != null) {
                                try {
                                    if (isCancel == 0) {
                                        stopRecordSendServer();
                                    } else {
                                        stopRecording();
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                seconds = 0;
                                minute = 0;
                            }
                        }
                        txtTimer.setText("0:00");
                        Log.e("onStop", "stop recording" + " audio file: " + getAudioFileName());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        messagesListAdapter.stopMedia();
                        timer = new Timer();
                        startRecording();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                txtTimer.post(new Runnable() {
                                    public void run() {
                                        seconds++;
                                        if (seconds == 60) {
                                            seconds = 0;
                                            minute++;
                                        }
                                        if (minute == 1) {
                                            try {
                                                timer.cancel();
                                                stopRecording();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            txtTimer.setText("Limited seconds. Up to send and Move outside to cancel.");
                                            Log.e("Limited seconds", "Agree");
                                        } else {
                                            txtTimer.setText(""
                                                    + (minute > 9 ? minute : (minute == 0 ? "" : "0") + minute)
                                                    + ":"
                                                    + (seconds > 9 ? seconds : "0" + seconds));
                                        }
                                    }
                                });
                            }
                        }, 1000, 1000);

                        isCancel = 0;
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());      // Get position of button
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(rect != null && !rect.contains(v.getLeft()+(int)event.getX(), v.getTop()+(int)event.getY())) {
                            isCancel = 1;
                            rootLayout.setBackgroundColor(Color.RED);
//                            Log.e("onMove","Move"+isCancel);
                        }else {
                            rootLayout.setBackgroundColor(Color.WHITE);
                            isCancel = 0;
//                            Log.e("onMove","Move"+isCancel);
                        }

                        break;
                }
                return false;
            }
        });

        mHandler = new Handler(Looper.getMainLooper());


        // Application permission 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            checkPermission(MANDATORY_PERMISSIONS);
        }

        return v;
    }

    public void stopRecordSendServer() {
        stopRecording();
        new SaveUserAsyntaskAudio(context).execute(getMediaName);

    }

    public void cancelRecordDelete() {
        stopRecording();
    }

    private void startRecording() {
        getMediaName = getAudioFileName();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getMediaName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e("onPrepared", "prepare() failed");
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private String getAudioFileName(){
        String filename = null;
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YsKMttBCM8McMedayiChat");
        folder.mkdir();
        if(!folder.isDirectory() || !folder.exists()) {;
            folder.mkdir();
        }
        filename = folder.toString();
        filename += "/" + System.currentTimeMillis()/1000 + ".mp3";
        return filename;
    }


    private boolean deleteCurrentFile(String path){
        File file = new File(path);
        boolean isDelete = false;
        if (file.exists()) {
            isDelete = file.delete();
        }
        return isDelete;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    // Application permission 23
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    @SuppressLint("NewApi")
    private void checkPermission(String[] permissions) {

        requestPermissions(permissions, MY_PERMISSION_REQUEST_STORAGE);
    }
    // Application permission 23
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                int cnt = permissions.length;
                for(int i = 0; i < cnt; i++ ) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED ) {

                        Log.i("Permission", "Permission[" + permissions[i] + "] = PERMISSION_GRANTED");

                    } else {

                        Log.i("Permission", "permission[" + permissions[i] + "] always deny");
                    }
                }
                break;
        }
    }

    @Override
    public void onRecordStart() {
            mIsRecording = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    float radius = 0f;
                    if (mediaRecorder != null) {
                        radius = (float) Math.log10(Math.max(1, mediaRecorder.getMaxAmplitude() - 200)) * ScreenUtils.dp2px(getContext(), 20);
                    }
                    mVoiceView.animateRadius(radius);
                    if (mIsRecording) {
                        mHandler.postDelayed(this, 50);
                    }
                }
            });
    }

    @Override
    public void onRecordFinish() {
        Log.d("record", "onRecordFinish");
        mIsRecording = false;

    }

    @Override
    public void onDestroy() {
        if(mIsRecording){
            mIsRecording = false;
        }
        super.onDestroy();
    }
}
