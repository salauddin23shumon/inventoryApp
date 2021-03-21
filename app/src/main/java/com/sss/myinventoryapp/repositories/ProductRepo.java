package com.sss.myinventoryapp.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;
import com.sss.myinventoryapp.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.sss.myinventoryapp.utility.Constant.ROOTREF;

public class ProductRepo {

    private SessionManager sessionManager;
    private List<Product> products;
    private final MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();

    private static final String TAG="ProductRepo";

    public ProductRepo(Application application) {

        sessionManager=new SessionManager(application);
        products=new ArrayList<>();
        Log.d(TAG, "ProductRepo: "+sessionManager.getUid());
    }

    public MutableLiveData<List<Product>> getProductLiveData() {

        ROOTREF.child(sessionManager.getUid()).child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                    Log.e(TAG, "onDataChange: "+products.size() );
                    productListLiveData.setValue(products);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " "+databaseError.getMessage());
                productListLiveData.setValue(null);
            }
        });
        return productListLiveData;
    }
}
