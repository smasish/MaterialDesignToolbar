package com.efilx.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ABC on 4/17/2015.
 */
public class Utils {


//    public static void Alert_Dialog( final Context context){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setTitle("Internet Required !");
//
//        // set dialog message
//        alertDialogBuilder.setMessage("You have no internet connection. Please connect your device to internet.")
//                .setCancelable(false)
//                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//
//                        dialog.cancel();
//
//                    }
//                });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    public static void AlertDialog( final Activity activity){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Internet Required !");

        // set dialog message
        alertDialogBuilder.setMessage("You have no internet connection. Please connect your device to internet.")
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //activity.finish();
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void LoadingControl(boolean spinning , LinearLayout loading) {
        if (spinning){
            loading.setVisibility(View.VISIBLE);
        }
        else{
            loading.setVisibility(View.GONE);
        }
    }
}
