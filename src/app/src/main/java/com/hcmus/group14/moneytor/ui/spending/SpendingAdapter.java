package com.hcmus.group14.moneytor.ui.spending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Spending;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Spending> spendings;
    private Context context;

    public SpendingAdapter(Context context, List<Spending> spendingList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.spendings = spendingList;
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_spending, parent, false);
        SpendingViewHolder holder = new SpendingViewHolder(itemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Spending currentSpending = spendings.get(position);
        holder.setTitle(currentSpending.getTitle());
        holder.setDesc(currentSpending.getDescription());
        holder.setValue(currentSpending.getCost());
        holder.setDate(currentSpending.getDate());
    }


    @Override
    public int getItemCount() {
        if (spendings != null) return spendings.size();
        else return 0;
    }

    public class SpendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SpendingAdapter adapter;
        private TextView titleView;
        private TextView descView;
        private TextView dateView;
        private TextView valueView;

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setDesc(String desc) {
            descView.setText(desc);
        }

        public void setDate(long date) {
            Date temp = new Date(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateView.setText(dateFormat.format(temp));
        }

        public void setValue(long value) {
            valueView.setText(String.format("%,d", value) + " VNĐ");
        }

        public SpendingViewHolder(@NonNull View itemView, SpendingAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.titleView = itemView.findViewById(R.id.titleSpendingItem);
            this.descView = itemView.findViewById(R.id.descSpendingItem);
            this.dateView = itemView.findViewById(R.id.dateSpendingItem);
            this.valueView = itemView.findViewById(R.id.valueSpendingItem);
        }

        @Override
        public void onClick(View v) {
            // open activity
        }
    }
}

