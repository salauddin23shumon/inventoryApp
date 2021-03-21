package com.sss.myinventoryapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.repositories.ProductRepo;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepo productRepo;
    private MutableLiveData<List<Product>> productLiveData;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepo=new ProductRepo(application);
        productLiveData=productRepo.getProductLiveData();
    }

    public LiveData<List<Product>> getProductLiveData(){
        return productLiveData;
    }
}
