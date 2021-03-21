package com.sss.myinventoryapp.responces;

import com.sss.myinventoryapp.models.Product;

import java.util.List;

public class ProductResponse {

    private boolean isProgressLoading;
    private String toastMessage;
    private List<Product> products;


    public ProductResponse(boolean isProgressLoading, String toastMessage, List<Product> products) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
        this.products = products;
    }

    public ProductResponse(boolean isProgressLoading, String toastMessage) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
    }

    public boolean isProgressLoading() {
        return isProgressLoading;
    }

    public void setProgressLoading(boolean progressLoading) {
        isProgressLoading = progressLoading;
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
