package com.sss.myinventoryapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.User;


public class ProductAddFragment extends Fragment {

    private EditText nameET, rateET, quantityET, priceET, dateET, unitET;
    private Button saveBtn;
    private DatabaseReference eventRef;

    private static final String TAG = "ProductAddFragment";

    public ProductAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventRef = FirebaseDatabase.getInstance().getReference("users").child("K1XjaW2G0vWWajACWeaGv3rOPLT2").child("product");
        return inflater.inflate(R.layout.fragment_product_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameET = view.findViewById(R.id.nameET);
        quantityET = view.findViewById(R.id.quantityET);
        rateET = view.findViewById(R.id.rateET);
        unitET = view.findViewById(R.id.unitET);
        priceET = view.findViewById(R.id.priceET);
        dateET = view.findViewById(R.id.dateET);
        saveBtn = view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameET.getText().toString();
                String quantity = quantityET.getText().toString();
                String rate = rateET.getText().toString();
                String unit = unitET.getText().toString();
                String price = priceET.getText().toString();
                String date = dateET.getText().toString();

                String keyId = eventRef.push().getKey();
                Product product = new Product(keyId, name, Double.parseDouble(rate), Integer.parseInt(quantity),unit, Double.parseDouble(price), date);
                eventRef.child(keyId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+e.getMessage());
                    }
                });
            }
        });
    }
}