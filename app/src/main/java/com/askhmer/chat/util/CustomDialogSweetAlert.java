package com.askhmer.chat.util;

import android.app.Activity;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by PC1 on 2/4/2016.
 */
public class CustomDialogSweetAlert {
    private static SweetAlertDialog pDialog;

    // Show ProgressDialog
    public static void showLoadingProcessDialog(Activity activity) {
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    // Close ProgressDialog
    public static void hideLoadingProcessDialog() {
        if (pDialog != null) {
            pDialog.cancel();
            pDialog.dismissWithAnimation();
        }
    }


}
