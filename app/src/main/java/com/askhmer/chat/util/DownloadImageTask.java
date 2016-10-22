package com.askhmer.chat.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.Window;

import com.askhmer.chat.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by soklundy on 10/22/2016.
 */

public class DownloadImageTask extends AsyncTask <String, Void, Bitmap> {

    private Dialog dialog;
    private Context context;
    private SubsamplingScaleImageView subsamplingScaleImageView;

    private void createDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    public DownloadImageTask(Context context, SubsamplingScaleImageView subsamplingScaleImageView) {
        this.context = context;
        this.subsamplingScaleImageView = subsamplingScaleImageView;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onPreExecute() {
        createDialog();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        subsamplingScaleImageView.setImage(ImageSource.uri(getImageUri(context ,bitmap)));
        dialog.dismiss();
    }
}
