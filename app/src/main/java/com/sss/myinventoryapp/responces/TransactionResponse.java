package com.sss.myinventoryapp.responces;

import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.Transaction;

import java.util.List;

public class TransactionResponse {
    private boolean isProgressLoading;
    private String toastMessage;
    private List<Transaction> transactions;


    public TransactionResponse(boolean isProgressLoading, String toastMessage, List<Transaction> transactions) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
        this.transactions = transactions;
    }

    public TransactionResponse(boolean isProgressLoading, String toastMessage) {
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
