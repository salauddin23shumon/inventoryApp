package com.sss.myinventoryapp.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;
import com.sss.myinventoryapp.responces.ProductResponse;
import com.sss.myinventoryapp.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.sss.myinventoryapp.utility.Constant.ROOTREF;

public class ProductRepo {


    private List<Product> products;
    private DatabaseReference productRef;
    private final MutableLiveData<ProductResponse> productResponseLiveData = new MutableLiveData<>();

    private static final String TAG="ProductRepo";

    public ProductRepo(Application application) {


        products=new ArrayList<>();

    }

    public MutableLiveData<ProductResponse> getProductLiveData(String uid) {

        productResponseLiveData.setValue(new ProductResponse(true,""));

        ROOTREF.child(uid).child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    products.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        products.add(product);
                        Log.e(TAG, "onDataChange: " + products.size());
                        productResponseLiveData.setValue(new ProductResponse(false, "", products));
                    }
                }else {
                    Log.e(TAG, "onDataChange: no value" );
                    productResponseLiveData.setValue(new ProductResponse(false, "no product found", products));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " "+databaseError.getMessage());
                productResponseLiveData.setValue(new ProductResponse(false,databaseError.getMessage()));
            }
        });
        return productResponseLiveData;
    }

    public void setProductLiveData(Product product){
        productResponseLiveData.setValue(new ProductResponse(true,""));

//        productRef=ROOTREF.child(sessionManager.getUid()).child("product");
        String keyId = productRef.push().getKey();
        product.setPid(keyId);
        productRef.child(keyId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: ");
                productResponseLiveData.setValue(new ProductResponse(false,"Product Saved"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
                productResponseLiveData.setValue(new ProductResponse(false,"Something went wrong"));
            }
        });
    }
}
