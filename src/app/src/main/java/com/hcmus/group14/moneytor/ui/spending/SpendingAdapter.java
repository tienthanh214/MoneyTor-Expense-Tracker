package com.hcmus.group14.moneytor.ui.spending;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Spending> spendings;
    private final Context context;

    public SpendingAdapter(Context context, List<Spending> spendingList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.spendings = spendingList;
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_spending, parent, false);
        return new SpendingViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Spending currentSpending = spendings.get(position);
        holder.setTitle(currentSpending.getTitle());
        holder.setDesc(currentSpending.getDescription());
        holder.setValue(currentSpending.getCost());
        holder.setDate(currentSpending.getDate());
        Category category = CategoriesUtils.findCategoryById(currentSpending.getCategory());
        holder.setImage(category.getColor(),category.getResourceId());
    }
    public void setSpending(List<Spending> spendingList){
        spendings=spendingList;
        notifyDataSetChanged();
    }

    public void filterList(List<Spending> filteredList){
        spendings=filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (spendings != null) return spendings.size();
        else return 0;
    }

    public class SpendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final private SpendingAdapter adapter;
        final private TextView titleView;
        final private TextView descView;
        final private TextView dateView;
        final private TextView valueView;
        final private ImageView imageView;

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setDesc(String desc) {
            descView.setText(desc);
        }

        public void setDate(long date) {
            dateView.setText(DateTimeUtils.getDate(date));
        }

        public void setValue(long value) {
            valueView.setText(InputUtils.getCurrency(value));
        }
        public void setImage(int color, int resource){
            imageView.setImageResource(resource);
            imageView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        public SpendingViewHolder(@NonNull View itemView, SpendingAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.titleView = itemView.findViewById(R.id.titleSpendingItem);
            this.descView = itemView.findViewById(R.id.descSpendingItem);
            this.dateView = itemView.findViewById(R.id.dateSpendingItem);
            this.valueView = itemView.findViewById(R.id.valueSpendingItem);
            this.imageView = itemView.findViewById(R.id.categorySpendingItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // open activity
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, AddSpendingActivity.class);
            intent.putExtra("spending_id", spendings.get(position).getSpendingId());
            context.startActivity(intent);
        }
    }
}

