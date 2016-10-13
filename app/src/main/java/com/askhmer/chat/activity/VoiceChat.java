package com.askhmer.chat.activity;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.MessagesListAdapter;
import com.askhmer.chat.util.SaveUserAsyntaskAudio;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VoiceChat extends Fragment {

    private TextView txtTimer;
    private TextView txtRecorder;

    private MediaRecorder mediaRecorder = null;
    private static String getMediaName = null;
    private  Context context;

    private int seconds;
    private int minute;
    private MessagesListAdapter messagesListAdapter;

    // Rect rect;
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
        txtRecorder = (TextView)v.findViewById(R.id.recordAudio);

        txtRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
                                            minute = 0;
                                            seconds = 0;
                                            timer.cancel();
                                            stopRecordSendServer();
                                        }
                                        txtTimer.setText(""
                                                + (minute > 9 ? minute : (minute == 0 ? "" : "0") + minute)
                                                + ":"
                                                + (seconds > 9 ? seconds : "0" + seconds));
                                    }
                                });
                            }
                        }, 1000, 1000);
                        // rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());      // Get position of button
                        Log.e("onStart", "start recording");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("onSeconds", "" + seconds);
                        timer.cancel();
                        if (seconds < 1) {
                            txtTimer.setText("0:00");
                            seconds = 0;
                            minute = 0;

                            if (getMediaName != null) {
                                try {
                                    cancelRecordDelete();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }
                        if (seconds > 0) {
                            if (mediaRecorder != null) {
                                try {
                                    stopRecordSendServer();
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
                    /*case MotionEvent.ACTION_MOVE:
                        if(rect != null && !rect.contains(v.getLeft()+(int)event.getX(), v.getTop()+(int)event.getY())) {
                            Log.e("onMove", "Move");
                            // Move outside
                        }

                        break;
                        */
                }
                return true;
            }
        });
        return v;
    }

    public void stopRecordSendServer() {
        stopRecording();
        new SaveUserAsyntaskAudio(context).execute(getMediaName);

        android.os.Handler mhandler = new android.os.Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getMediaName != null) {
//                    deleteCurrentFile(getMediaName);
                }
            }
        }, 50);
        Log.e("onStop", "stop recording");
    }

    public void cancelRecordDelete() {
        stopRecording();
//        deleteCurrentFile(getMediaName);
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
/*

    private String getAudioFileName(){
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath();
        filename += "/" + System.currentTimeMillis()/1000 + ".mp3";
        return filename;
    }

*/


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
}
