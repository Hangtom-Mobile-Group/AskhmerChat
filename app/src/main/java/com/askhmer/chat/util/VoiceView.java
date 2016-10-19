package com.askhmer.chat.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.askhmer.chat.R;

/**
 * Created by yugy on 2014/3/23.
 */
public class VoiceView extends View {

    private static final String TAG = VoiceView.class.getName();

    private static final int STATE_NORMAL = 0;
    private static final int STATE_PRESSED = 1;
    private static final int STATE_RECORDING = 2;

    private Bitmap mNormalBitmap;
    private Bitmap mPressedBitmap;
    private Bitmap mRecordingBitmap;
    private Paint mPaint;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private OnRecordListener mOnRecordListener;

    private int mState = STATE_NORMAL;
    private boolean mIsRecording = false;
    private float mMinRadius;
    private float mMaxRadius;
    private float mCurrentRadius;

    public VoiceView(Context context) {
        super(context);
        init();
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mNormalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vs_micbtn_off);
        mPressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vs_micbtn_pressed);
        mRecordingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vs_micbtn_on);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        mMinRadius = ScreenUtils.dp2px(getContext(), 116) / 2;
        mCurrentRadius = mMinRadius;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxRadius = Math.min(w, h) / 2;
        Log.d(TAG, "MaxRadius: " + mMaxRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if(mCurrentRadius > mMinRadius){
            canvas.drawCircle(width / 2, height / 2, mCurrentRadius, mPaint);
        }

        switch (mState){
            case STATE_NORMAL:
                canvas.drawBitmap(mNormalBitmap, width / 2 - mMinRadius,  height / 2 - mMinRadius, mPaint);
                break;
            case STATE_PRESSED:
                canvas.drawBitmap(mPressedBitmap, width / 2 - mMinRadius,  height / 2 - mMinRadius, mPaint);
                break;
            case STATE_RECORDING:
                canvas.drawBitmap(mRecordingBitmap, width / 2 - mMinRadius,  height / 2 - mMinRadius, mPaint);
                break;
        }
    }

    public void animateRadius(float radius){
        if(radius <= mCurrentRadius){
            return;
        }
        if(radius > mMaxRadius){
            radius = mMaxRadius;
        }else if(radius < mMinRadius){
            radius = mMinRadius;
        }
        if(radius == mCurrentRadius){
            return;
        }
        if(mAnimatorSet.isRunning()){
            mAnimatorSet.cancel();
        }
        mAnimatorSet.playSequentially(
                ObjectAnimator.ofFloat(this, "CurrentRadius", getCurrentRadius(), radius).setDuration(30),
                ObjectAnimator.ofFloat(this, "CurrentRadius", radius, mMinRadius).setDuration(500)
        );
        mAnimatorSet.start();
    }

    public float getCurrentRadius() {
        return mCurrentRadius;
    }

    public void setCurrentRadius(float currentRadius) {
        mCurrentRadius = currentRadius;
        invalidate();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        Toast.makeText(getContext(), "sss", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");

                if(mIsRecording){
                    mState = STATE_NORMAL;
                    if(mOnRecordListener != null){
                        mOnRecordListener.onRecordFinish();
                    }
                }else{
                    mState = STATE_RECORDING;
                    if(mOnRecordListener != null){
                        mOnRecordListener.onRecordStart();
                    }
                }
                mIsRecording = !mIsRecording;
                invalidate();

                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");

                if(mIsRecording){
                    mState = STATE_NORMAL;
                    if(mOnRecordListener != null){
                        mOnRecordListener.onRecordFinish();
                    }
                }
                mIsRecording = !mIsRecording;

//                mState = STATE_PRESSED;
                invalidate();

                return true;
            case MotionEvent.ACTION_MOVE:
                /*if(rect != null && !rect.contains(v.getLeft()+(int)event.getX(), v.getTop()+(int)event.getY())) {
                    Log.e("onMove", "Move");
                    // Move outside
                }*/

                return  true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        mOnRecordListener = onRecordListener;
    }

    public static interface OnRecordListener{
        public void onRecordStart();
        public void onRecordFinish();
    }
}
