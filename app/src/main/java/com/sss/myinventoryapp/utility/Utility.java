package com.sss.myinventoryapp.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class Utility {

    public static void hideKeyboard(@NonNull View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static byte[] getImageBAOS(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, outputStream);
        return outputStream.toByteArray();
    }
}
