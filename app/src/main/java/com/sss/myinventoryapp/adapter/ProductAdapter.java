package com.sss.myinventoryapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.models.Product;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String TAG = "ProductAdapter ";
    private List<Product> productList;
    private Context context;
    private Bundle bundle;
    private TransactionCommit transactionCommit;
    private Fragment fragment;


    public ProductAdapter(List<Product> productList, Context context, Fragment fragment) {
        this.productList = productList;
        this.context = context;
        this.fragment = fragment;
        transactionCommit = (TransactionCommit) fragment;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.nameTV.setText(product.getProductName());
        holder.priceTV.setText("Price: " + product.getPrice());
        holder.rateTV.setText("Rate: " + product.getRate());
        holder.quantityTV.setText("Quantity: " + product.getQuantity());
        holder.stockTV.setText("Stock: " + product.getStock());
        holder.unitTV.setText("Unit: " + product.getUnit());
        Picasso.get()
                .load(product.getImgUrl()).into(holder.productImg);

        holder.inBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
//                bundle = new Bundle();
//                bundle.putString("type", "in");
//                bundle.putSerializable("product",product);
//                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_transactionAddFragment,bundle);
                transactionCommit.onCommit(product, "In");
            }
        });

        holder.outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionCommit.onCommit(product, "Out");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("type", "in");
                bundle.putSerializable("product", product);
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_transactionAddFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + productList.size());
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, priceTV, quantityTV, unitTV, rateTV, stockTV;
        private Button inBtn, outBtn;
        private ImageView productImg;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.p_nameTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            unitTV = itemView.findViewById(R.id.unitTV);
            rateTV = itemView.findViewById(R.id.rateTV);
            stockTV = itemView.findViewById(R.id.stockTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            inBtn = itemView.findViewById(R.id.btnIn);
            outBtn = itemView.findViewById(R.id.btnOut);
            productImg = itemView.findViewById(R.id.p_img);

        }
    }

    public void updateList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
        notifyItemInserted(this.productList.size());
    }

    public interface TransactionCommit {
        void onCommit(Product product, String type);
    }

}
