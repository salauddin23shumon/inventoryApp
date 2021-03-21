package com.sss.myinventoryapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sss.myinventoryapp.R;


public class TransactionAddFragment extends Fragment {


    private Context context;
    private String type;

    private static final String TAG="TransactionAddFragment";


    public TransactionAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        type=getArguments().getString("type");
        Log.d(TAG, "onAttach: "+type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_add, container, false);
    }
}