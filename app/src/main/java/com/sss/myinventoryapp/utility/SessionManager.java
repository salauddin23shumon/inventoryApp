package com.sss.myinventoryapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.sss.myinventoryapp.models.User;


public class SessionManager {

    private static final String TAG = "SessionManager";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private User appUser;
    private static final int MODE = Context.MODE_PRIVATE;
    public static final String PREF_NAME = "Session";
    public static final String LOGIN = "IS_LOGIN";
    public static final String APP_USER = "app_user";
    public static final String UID = "UID";




    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void createSession(User user) {
        Log.e(TAG, "createSession: " );
        editor.putBoolean(LOGIN, true);


        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(APP_USER, json);

        editor.putString(UID, user.getUid());

//        editor.putString(TOKEN, user.getToken());
//        editor.putBoolean(STATUS, user.isProfileComplete());


        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public User getAppUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(APP_USER, "err in preference 1");
        return gson.fromJson(json, User.class);
    }

    public String getUid(){
        return sharedPreferences.getString(UID, "id not found");
    }

//    public FirebaseUser getFirebaseUser() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(FIREBASE_USER, "err in preference 2");
//        return gson.fromJson(json, FirebaseUser.class);
//    }

    public void clearSession() {
        editor.clear();
        editor.putBoolean(LOGIN, false);
        editor.commit();

    }

}
