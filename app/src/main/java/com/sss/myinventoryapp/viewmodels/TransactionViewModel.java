package com.sss.myinventoryapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sss.myinventoryapp.repositories.TransactionRepo;
import com.sss.myinventoryapp.responces.TransactionResponse;

public class TransactionViewModel extends ViewModel {
    private TransactionRepo transactionRepo;
    private MutableLiveData<TransactionResponse> responseMutableLiveData;

    public TransactionViewModel() {
        transactionRepo=new TransactionRepo();
    }

    public LiveData<TransactionResponse> getAllTransaction(String uid){
        return transactionRepo.getTransactionLiveData(uid);
    }
}
