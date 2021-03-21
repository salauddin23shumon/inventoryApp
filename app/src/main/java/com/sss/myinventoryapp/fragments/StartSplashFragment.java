package com.sss.myinventoryapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.interfaces.ChangeHomeNavListener;
import com.sss.myinventoryapp.utility.SessionManager;

public class StartSplashFragment extends Fragment {

    private NavController navController;
    private SessionManager sessionManager;
    private Context context;
    private static final String TAG="SplashFragment";

    public StartSplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        sessionManager=new SessionManager(context);
        Log.e(TAG, "onAttach: "+context );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController= Navigation.findNavController(view);

        callHandler();
    }

    private void callHandler(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.isLogin()) {
                    navController.navigate(R.id.action_startSplashFragment_to_nav_home);
                    ((ChangeHomeNavListener)context).onHomeChange(R.id.nav_home);
                } else {
                    navController.navigate(R.id.action_startSplashFragment_to_signInFragment);
                    ((ChangeHomeNavListener)context).onHomeChange(R.id.nav_signin);
                }
            }
        }, 1000);
    }

}