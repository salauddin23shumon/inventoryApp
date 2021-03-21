package com.sss.myinventoryapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sss.myinventoryapp.repositories.AuthRepo;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;


public class SignInViewModel extends AndroidViewModel {

    private AuthRepo authRepo;
    private MutableLiveData<FirebaseAuthResponse> firebaseUserLiveData;


    public SignInViewModel(@NonNull Application application) {
        super(application);
        authRepo = new AuthRepo(application);
        firebaseUserLiveData = authRepo.getUserLiveData();
    }


    public void doSignIn(String email, String password) {
        authRepo.firebaseSignIn(email, password);
    }

    public MutableLiveData<FirebaseAuthResponse> getUserLiveData() {
        return firebaseUserLiveData;
    }
}
