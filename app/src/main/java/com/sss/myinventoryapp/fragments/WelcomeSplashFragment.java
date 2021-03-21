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


public class WelcomeSplashFragment extends Fragment {

    private static final String TAG="WelcomeSplashFragment";
    private NavController navController;
    private Context context;

    public WelcomeSplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                navController.navigate(R.id.action_welcomeSplashFragment_to_nav_home);
                ((ChangeHomeNavListener)context).onHomeChange(R.id.nav_home);
            }
        }, 2000);
    }

}