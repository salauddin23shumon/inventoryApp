package com.sss.myinventoryapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sss.myinventoryapp.repositories.AuthRepo;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;


public class SignUpViewModel extends AndroidViewModel {
    private AuthRepo authRepo;
    private MutableLiveData<FirebaseAuthResponse> firebaseUserLiveData;


    public SignUpViewModel(@NonNull Application application) {
        super(application);
        if (firebaseUserLiveData!=null)
            return;
        authRepo = new AuthRepo(application);
        firebaseUserLiveData = authRepo.getUserLiveData();
    }


    public void doSignUp(String name, String email, String password) {
        authRepo.firebaseSignUp(name, email, password);
    }

    public MutableLiveData<FirebaseAuthResponse> getUserLiveData() {
        return firebaseUserLiveData;
    }
}
