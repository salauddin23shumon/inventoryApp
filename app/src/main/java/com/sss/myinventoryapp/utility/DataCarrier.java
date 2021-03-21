package com.sss.myinventoryapp.utility;

import android.location.Location;

public class DataCarrier {

    private Location location;
    private String msg;
    private boolean isLocationPermitted;
    private boolean isStoragePermitted;
    private boolean isGpsEnable;


    public DataCarrier(Location location) {
        this.location = location;
    }


    public DataCarrier(Location location, boolean isLocationPermitted, boolean isGpsEnable) {
        this.location = location;
        this.isLocationPermitted = isLocationPermitted;
        this.isGpsEnable = isGpsEnable;
    }

    public DataCarrier() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isLocationPermitted() {
        return isLocationPermitted;
    }

    public void setLocationPermitted(boolean locationPermitted) {
        isLocationPermitted = locationPermitted;
    }

    public boolean isStoragePermitted() {
        return isStoragePermitted;
    }

    public void setStoragePermitted(boolean storagePermitted) {
        isStoragePermitted = storagePermitted;
    }

    public boolean isGpsEnable() {
        return isGpsEnable;
    }

    public void setGpsEnable(boolean gpsEnable) {
        isGpsEnable = gpsEnable;
    }
}
