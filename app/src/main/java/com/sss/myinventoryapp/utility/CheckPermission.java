package com.sss.myinventoryapp.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;


public class CheckPermission {


    private static final String TAG = "AppPermission";
    public static final int ENABLE_GPS_ACCESS = 44;
    private static boolean gps_enabled = false;
    private static boolean network_enabled = false;

    public static Boolean chkStoragePermission(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            return false;
        }
        return true;
    }


    public static boolean chkLocationPermission(Context context, int requestCode) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            return false;
        }
        return true;
    }


    public static boolean checkGPS(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            Log.d(TAG, "chkGPS: gps not enabled");
            return false;
        }
        return true;
    }


    public static void showAlert(final Context context) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ENABLE_GPS_ACCESS);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(context, "u will not allow to use this app properly", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }
}
