package com.sss.myinventoryapp.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sss.myinventoryapp.models.User;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;
import com.sss.myinventoryapp.utility.SessionManager;


public class AuthRepo {


    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    private SessionManager sessionManager;
    private final MutableLiveData<FirebaseAuthResponse> firebaseAuthUserMutableLiveData = new MutableLiveData<>();


    private static final String TAG = "AuthRepo";

    public AuthRepo(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        sessionManager=new SessionManager(application);

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuthUserMutableLiveData.postValue(new FirebaseAuthResponse(firebaseAuth.getCurrentUser(),null,false));
        }
    }


    public void firebaseSignIn(String email, String password) {
        firebaseAuthUserMutableLiveData.setValue(new FirebaseAuthResponse(null, true));

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onFirebaseAuthComplete(firebaseAuth.getCurrentUser().getUid());
                        } else {
                            Log.e(TAG, "onComplete: "+task.getException().getMessage() );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage() );
                firebaseAuthUserMutableLiveData.setValue(new FirebaseAuthResponse(e.getMessage(), false));
            }
        });
    }

    public void firebaseSignUp(String name, String email, String password) {
        firebaseAuthUserMutableLiveData.setValue(new FirebaseAuthResponse(null, true));

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(firebaseAuth.getCurrentUser().getUid(), name, email);
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference("users").child(firebaseAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e(TAG, "onComplete: ");
                                                onFirebaseAuthComplete(firebaseAuth.getCurrentUser().getUid());
                                            } else {
                                                Log.e(TAG, "sign up failed: "+task.getException().getMessage());
                                            }

                                        }
                                    });

                        } else {
                            Log.e(TAG, "onComplete: "+task.getException().getMessage() );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseAuthUserMutableLiveData.setValue(new FirebaseAuthResponse(e.getMessage(), false));
            }
        });
    }

    public void onFirebaseAuthComplete(String uid) {

        rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(uid);
        Log.e(TAG, "onFirebaseAuthComplete: "+uid );
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                sessionManager.createSession(user);
                Log.e(TAG, "onDataChange: " + user.getEmail());
                firebaseAuthUserMutableLiveData.postValue(new FirebaseAuthResponse(firebaseAuth.getCurrentUser(),null, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });
    }

    public void logOut() {
        firebaseAuth.signOut();
        firebaseAuthUserMutableLiveData.postValue(new FirebaseAuthResponse(true));
        Log.d(TAG, "logOut: called");
    }

    public MutableLiveData<FirebaseAuthResponse> getUserLiveData() {
        return firebaseAuthUserMutableLiveData;
    }


}
