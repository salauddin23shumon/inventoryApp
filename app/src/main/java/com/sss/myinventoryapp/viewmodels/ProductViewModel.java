package com.sss.myinventoryapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.repositories.ProductRepo;
import com.sss.myinventoryapp.responces.ProductResponse;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepo productRepo;
    private MutableLiveData<ProductResponse> productLiveData;
    private static final String TAG = "HomeFragment";

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepo = new ProductRepo(application);
    }

    public LiveData<ProductResponse> getProductLiveData(String uid) {
        Log.e(TAG, "getProductLiveData: "+uid );
        return productRepo.getProductLiveData(uid);
    }

    public void saveProduct(Product product){
        productRepo.setProductLiveData(product);
    }

    public void clear(){
        productLiveData.setValue(null);
    }
}
