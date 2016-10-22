package com.askhmer.chat.listener;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Thoeurn on 10/12/2016.
 */
public interface SendAudioListener {
    public void sendAudio(String audio);
    public void setAudioTime(TextView audioTime,String timeStr);
    public void changeImageButton(ImageButton imageButton, int r1, int r2);
    public void setAudioStatusTextView(TextView textView,String text);
    public void setCoverLayoutWidth(RelativeLayout audioLayout, LinearLayout coverLayout,long duration,int width);
    public void longItemClick(int position);
}
