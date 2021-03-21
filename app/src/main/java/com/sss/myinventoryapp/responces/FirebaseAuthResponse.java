package com.sss.myinventoryapp.responces;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthResponse {

    private FirebaseUser firebaseUser;
    private boolean isLoggedOut;
    private boolean isProgressLoading;
    private String toastMessage;



    public FirebaseAuthResponse(boolean isLoggedOut) {
        this.isLoggedOut = isLoggedOut;
    }

    public FirebaseAuthResponse(FirebaseUser firebaseUser, String toastMessage, boolean isProgressLoading) {
        this.firebaseUser = firebaseUser;
        this.toastMessage = toastMessage;
        this.isProgressLoading=isProgressLoading;
    }

    public FirebaseAuthResponse(String toastMessage, boolean isProgressLoading) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public boolean isLoggedOut() {
        return isLoggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        isLoggedOut = loggedOut;
    }

    public boolean isProgressLoading() {
        return isProgressLoading;
    }

    public void setProgressLoading(boolean progressLoading) {
        isProgressLoading = progressLoading;
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
    }
}
