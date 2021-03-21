package com.sss.myinventoryapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.adapter.ProductAdapter;
import com.sss.myinventoryapp.interfaces.LocationServiceListener;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.utility.Utils;
import com.sss.myinventoryapp.viewmodels.ProductViewModel;
import com.sss.myinventoryapp.viewmodels.SignInViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private NavController navController;
    private ProductViewModel productViewModel;
    private ProductAdapter adapter;
    private List<Product> productList;

    private static final String TAG = "HomeFragment";

    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        productList = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new ProductAdapter(productList, context);

        return root;
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

        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        productViewModel.getProductLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.updateList(products);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_productAddFragment);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}