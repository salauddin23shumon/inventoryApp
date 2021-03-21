package com.sss.myinventoryapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.Transaction;
import com.sss.myinventoryapp.responces.ProductResponse;
import com.sss.myinventoryapp.responces.TransactionResponse;

import java.util.ArrayList;
import java.util.List;

import static com.sss.myinventoryapp.utility.Constant.ROOTREF;

public class TransactionRepo {

    private List<Transaction> transactions;
    private DatabaseReference transacRef;
    private final MutableLiveData<TransactionResponse> transactionResponseLiveData = new MutableLiveData<>();

    private static final String TAG="ProductRepo";

    public TransactionRepo() {
        transactions=new ArrayList<>();
    }

    public MutableLiveData<TransactionResponse> getTransactionLiveData(String uid) {

        transactionResponseLiveData.setValue(new TransactionResponse(true,""));

        ROOTREF.child(uid).child("transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    transactions.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = snapshot.getValue(Transaction.class);
                        transactions.add(transaction);
                        Log.e(TAG, "onDataChange: " + transactions.size());
                        transactionResponseLiveData.setValue(new TransactionResponse(false, "", transactions));
                    }
                }else {
                    Log.e(TAG, "onDataChange: no value" );
                    transactionResponseLiveData.setValue(new TransactionResponse(false, "no item found"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " "+databaseError.getMessage());
                transactionResponseLiveData.setValue(new TransactionResponse(false,databaseError.getMessage()));
            }
        });
        return transactionResponseLiveData;
    }
}
