package com.sss.myinventoryapp.utility;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constant {
    public static final int LOCATION_SERVICE_ID = 4545;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
    public static final String NOTIFICATION_CHANNEL_ID = "location_notification_channel";
    public static final String FILE_NAME = "com.app.config";
    public static final String IS_SERVICE_ACTIVE = "isServiceActive";
    public static final int NETWORK_AVAILABLE = 22;
    public static final int NETWORK_NOT_AVAILABLE = 44;
    public static final int LOCATION_ENABLE = 11;
    public static final int LOCATION_DISABLE = 33;

    public static final DatabaseReference ROOTREF = FirebaseDatabase.getInstance().getReference("users");
}
