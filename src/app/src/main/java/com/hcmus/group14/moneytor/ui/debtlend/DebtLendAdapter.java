package com.hcmus.group14.moneytor.ui.debtlend;

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
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.List;
import java.util.Locale;

public class DebtLendAdapter extends RecyclerView.Adapter<DebtLendAdapter.DebtLendViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<DebtLendAndRelate> debtLends;
    private final Context context;

    public DebtLendAdapter(Context context, List<DebtLendAndRelate> debtLendList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.debtLends = debtLendList;
    }

    @NonNull
    @Override
    public DebtLendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_debt_lend, parent, false);
        return new DebtLendViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtLendViewHolder holder, int position) {
        DebtLend currentDebtLend = debtLends.get(position).debtLend;
        Relate currentRelate = debtLends.get(position).relate;
        holder.setDate(currentDebtLend.getDate());
        holder.setValue(currentDebtLend.getValue());
        holder.setDesc(currentDebtLend.getDesc());
        int isDebt =currentDebtLend.getDebt();
        String title = context.getString(isDebt == 1 ? R.string.debt : R.string.lend) + " " + currentRelate.getName();
        holder.setTitle(title);
        Category category = CategoriesUtils.findCategoryById(currentDebtLend.getCategory());
        holder.setImage(category.getColor(),category.getResourceId());
    }


    public void setDebtLends(List<DebtLendAndRelate> debtLendList){
        debtLends=debtLendList;
        notifyDataSetChanged();
    }

    public void filterList(List<DebtLendAndRelate> filteredList){
        debtLends=filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (debtLends != null) return debtLends.size();
        else return 0;
    }

    public class DebtLendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final DebtLendAdapter adapter;
        private final TextView titleView;
        private final TextView descView;
        private final TextView dateView;
        private final TextView valueView;
        private final ImageView imageView;


        public void setImage(int color, int resource){
            imageView.setImageResource(resource);
            imageView.setBackgroundTintList(ColorStateList.valueOf(color));
        }

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

        public DebtLendViewHolder(@NonNull View itemView, DebtLendAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.titleView = itemView.findViewById(R.id.titleDebtLendItem);
            this.descView = itemView.findViewById(R.id.descDebtLendItem);
            this.dateView = itemView.findViewById(R.id.dateDebtLendItem);
            this.valueView = itemView.findViewById(R.id.valueDebtLendItem);
            this.imageView = itemView.findViewById(R.id.categoryDebtLendItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // open activity
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, AddDebtLendActivity.class);
            intent.putExtra("debt_id", debtLends.get(position).debtLend.getRecordId());
            context.startActivity(intent);
        }
    }
}

