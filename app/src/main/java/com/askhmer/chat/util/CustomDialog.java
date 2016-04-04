package com.askhmer.chat.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.askhmer.chat.R;

/**
 * Created by PC1 on 2/4/2016.
 */
public class CustomDialog {
    private static ProgressDialog proDialog;

    // Show ProgressDialog
    public static void showProgressDialog(Activity activity) {
        proDialog = new ProgressDialog(activity, R.style.MyProgressDialogTheme);
        proDialog.setCancelable(false);
        proDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        proDialog.show();
    }

    // Close ProgressDialog
    public static void hideProgressDialog() {
        if (proDialog != null) {
            proDialog.cancel();
            proDialog.dismiss();
        }
    }
}
