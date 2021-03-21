package com.sss.myinventoryapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.adapter.ProductAdapter;
import com.sss.myinventoryapp.adapter.TransactionAdapter;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.Transaction;
import com.sss.myinventoryapp.responces.TransactionResponse;
import com.sss.myinventoryapp.utility.SessionManager;
import com.sss.myinventoryapp.viewmodels.ProductViewModel;
import com.sss.myinventoryapp.viewmodels.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;


public class TransactionListFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private NavController navController;
    private TransactionViewModel transactionViewModel;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private SessionManager sessionManager;

    private static final String TAG = "HomeFragment";
    private Dialog alertDialog;

    public TransactionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        sessionManager=new SessionManager(context);
        transactionList= new ArrayList<>();
        alertDialog = new Dialog(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter=new TransactionAdapter(transactionList, context);
        transactionViewModel=new ViewModelProvider(getActivity()).get(TransactionViewModel.class);
        return inflater.inflate(R.layout.fragment_transaction_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.productRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.scrollToPositionWithOffset(0, 0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        transactionViewModel.getAllTransaction(sessionManager.getUid()).observe(getViewLifecycleOwner(), new Observer<TransactionResponse>() {
            @Override
            public void onChanged(TransactionResponse transactionResponse) {
                if (transactionResponse.getTransactions() != null) {
                    adapter.updateList(transactionResponse.getTransactions());
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
                if (transactionResponse.isProgressLoading()) {
                    showProgressDialog();
                } else {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                }
            }
        });
    }

    private void showProgressDialog() {

//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.progress_dialog);
        alertDialog.setCancelable(false);
        final CircularProgressBar circularProgressBar = alertDialog.findViewById(R.id.circular_progressBar);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
        final TextView progressUpdate = alertDialog.findViewById(R.id.progressTV);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && alertDialog.isShowing()) {
//                    getFragmentManager().popBackStack();
                    alertDialog.dismiss();
                }
                return true;
            }
        });
    }
}