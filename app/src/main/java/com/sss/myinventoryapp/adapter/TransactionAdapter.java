package com.sss.myinventoryapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private static final String TAG = "ProductAdapter ";
    private List<Transaction> transactions;
    private Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transac_row, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);
        holder.nameTV.setText(transaction.getProductName());
        holder.rateTV.setText("Rate: " + transaction.getRate());
        holder.quantityTV.setText("Quantity: " + transaction.getQuantity());
        holder.dateTV.setText("Date: " + transaction.getDate());
        holder.typeBtn.setText(transaction.getType());

        if (transaction.getType().equals("In")){
            holder.productImg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.login));
            holder.typeBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.dark_orange));
        }else {
            holder.productImg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.logout));
            holder.typeBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.hard_green));
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+transactions.size());
        return transactions.size();
    }

    public void updateList(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
        notifyItemInserted(this.transactions.size());
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV,  quantityTV,  rateTV, dateTV;
        private ImageView productImg;
        private Button typeBtn;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.p_nameTV);
            rateTV = itemView.findViewById(R.id.rateTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            productImg = itemView.findViewById(R.id.p_img);
            typeBtn = itemView.findViewById(R.id.btnIn);
        }
    }
}
