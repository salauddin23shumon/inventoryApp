package com.sss.myinventoryapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.adapter.ProductAdapter;
import com.sss.myinventoryapp.interfaces.LocationServiceListener;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.Transaction;
import com.sss.myinventoryapp.responces.ProductResponse;

import com.sss.myinventoryapp.utility.SessionManager;
import com.sss.myinventoryapp.utility.Utils;
import com.sss.myinventoryapp.viewmodels.ProductViewModel;
import com.sss.myinventoryapp.viewmodels.SignInViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.sss.myinventoryapp.utility.Constant.ROOTREF;

public class HomeFragment extends Fragment implements ProductAdapter.TransactionCommit {

    private Context context;
    private RecyclerView recyclerView;
    private NavController navController;
    private ProductViewModel productViewModel;
    private ProductAdapter adapter;
    private List<Product> productList;
    private SessionManager sessionManager;
    private View mainView;

    private static final String TAG = "HomeFragment";
    private Dialog alertDialog;
    private BottomSheetDialog dialog;


    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        productList = new ArrayList<>();
        alertDialog = new Dialog(context);
        sessionManager = new SessionManager(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new ProductAdapter(productList, context, this);
        dialog = new BottomSheetDialog(context, R.style.DialogTheme);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.productRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.scrollToPositionWithOffset(0, 0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        navController = Navigation.findNavController(view);


        productViewModel.getProductLiveData(sessionManager.getUid()).observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getProducts() != null) {
                    adapter.updateList(response.getProducts());
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
                if (response.isProgressLoading()) {
                    showProgressDialog();
                } else {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_productAddFragment);
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


    @Override
    public void onCommit(Product product, String type) {
        showDialog(product, type);
    }

    private void showDialog(Product product, String type) {

        View dialogView = LayoutInflater.from(context).
                inflate(R.layout.transaction_dialog, (LinearLayout) mainView.findViewById(R.id.dialog_container));

        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        DatabaseReference transacRef = ROOTREF.child(sessionManager.getUid()).child("transaction");

        EditText rateET, quantityET, priceET, dateET;
        TextView nameTV;
        Button saveBtn;

        rateET = dialogView.findViewById(R.id.rateET);
        quantityET = dialogView.findViewById(R.id.quantityET);
        priceET = dialogView.findViewById(R.id.priceET);
        dateET = dialogView.findViewById(R.id.dateET);
        nameTV = dialogView.findViewById(R.id.typeTV);
        saveBtn = dialogView.findViewById(R.id.saveBtn);

        nameTV.setText(product.getProductName() + " " + type);


//        if (type.equals("Out"))
//            rateET.setText("Rate:" + String.valueOf(product.getRate()));


        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener startingDate =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        SimpleDateFormat sDF = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        myCalendar.set(Calendar.YEAR, y);
                        myCalendar.set(Calendar.MONTH, m);
                        myCalendar.set(Calendar.DAY_OF_MONTH, d);
                        dateET.setText(sDF.format(myCalendar.getTime()));
                    }
                };

        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    DatePickerDialog datePiker = new DatePickerDialog
                            (context, startingDate, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePiker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePiker.show();
                }
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q, s;

                Transaction transaction=new Transaction();

                String quantity = quantityET.getText().toString();
                String rate = rateET.getText().toString();
                String price = priceET.getText().toString();
                String date = dateET.getText().toString();

                String keyId = transacRef.push().getKey();

                transaction.setTid(keyId);
                transaction.setType(type);
                transaction.setProductName(product.getProductName());
                transaction.setQuantity(Integer.parseInt(quantity));
                transaction.setRate(Double.parseDouble(rate));
                transaction.setDate(date);

                if (type.equals("In")) {
                    q = product.getQuantity() + Integer.parseInt(quantity);
                    s = product.getQuantity() + Integer.parseInt(quantity);
                    product.setQuantity(q);
                    product.setStock(s);
                    product.setRate(Double.parseDouble(rate));
                    product.setPrice(Double.parseDouble(price));
                    product.setEntryDate(date);

                } else if (type.equals("Out")) {
//                    product.setQuantity( product.getQuantity());
                    s = product.getQuantity() - Integer.parseInt(quantity);
                    product.setStock(s);
                    product.setRate(Double.parseDouble(rate));
                    product.setPrice(Double.parseDouble(price));
                    product.setEntryDate(date);
                }


                ROOTREF.child(sessionManager.getUid()).child("product").child(product.getPid()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: ");
                        transacRef.child(keyId).setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: transaction success");
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Log.d(TAG, "onFailure: "+e.getMessage());
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
            }
        });

        dialog.setContentView(dialogView);
        setupFullHeight(dialog);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}