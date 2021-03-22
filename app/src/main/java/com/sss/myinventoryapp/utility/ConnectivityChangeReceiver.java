package com.sss.myinventoryapp.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sss.myinventoryapp.interfaces.ConnectivityChangeListener;

import static com.sss.myinventoryapp.utility.Constant.LOCATION_DISABLE;
import static com.sss.myinventoryapp.utility.Constant.LOCATION_ENABLE;
import static com.sss.myinventoryapp.utility.Constant.NETWORK_AVAILABLE;
import static com.sss.myinventoryapp.utility.Constant.NETWORK_NOT_AVAILABLE;


public class ConnectivityChangeReceiver extends BroadcastReceiver {


    private static final String TAG = ConnectivityChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        Log.e(TAG, "onReceive: "+intentAction );
        if (intentAction != null) {
            switch (intentAction) {
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (isGpsEnabled) {
                        Log.e(TAG, "gps on");
                        ((ConnectivityChangeListener) context).onStatusChange(true, LOCATION_ENABLE);
                    } else {
                        Log.e(TAG, "gps off");
                        ((ConnectivityChangeListener) context).onStatusChange(false, LOCATION_DISABLE);
                    }
                    break;

                case ConnectivityManager.CONNECTIVITY_ACTION:
                    final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

                    //should check null because in airplane mode it will be null
                    boolean isNetworkAvailable = netInfo != null && netInfo.isAvailable() && netInfo.isConnected();

                    if (isNetworkAvailable) {
                        Log.e(TAG, "Network Available");
                        ((ConnectivityChangeListener) context).onStatusChange(true, NETWORK_AVAILABLE);
                    } else {
                        Log.e(TAG, "Network Not Available");
                        ((ConnectivityChangeListener) context).onStatusChange(false, NETWORK_NOT_AVAILABLE);
                    }
                    break;
            }
        }
    }
}
