package com.sss.myinventoryapp.utility;

public class EventBusData {
    private boolean isNetworkAvailable;

    public EventBusData(boolean isNetworkAvailable) {
        this.isNetworkAvailable = isNetworkAvailable;
    }

    public boolean isNetworkAvailable() {
        return isNetworkAvailable;
    }

    public void setNetworkAvailable(boolean networkAvailable) {
        isNetworkAvailable = networkAvailable;
    }
}
