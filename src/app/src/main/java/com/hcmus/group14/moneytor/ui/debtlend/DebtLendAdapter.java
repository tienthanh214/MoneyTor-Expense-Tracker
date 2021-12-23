package com.hcmus.group14.moneytor.ui.debtlend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        String title = (isDebt == 1 ? "Debt" : "Lend") + " " + currentRelate.getName();
        holder.setTitle(title);
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
        final private DebtLendAdapter adapter;
        final private TextView titleView;
        final private TextView descView;
        final private TextView dateView;
        final private TextView valueView;

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

        public DebtLendViewHolder(@NonNull View itemView, DebtLendAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.titleView = itemView.findViewById(R.id.titleDebtLendItem);
            this.descView = itemView.findViewById(R.id.descDebtLendItem);
            this.dateView = itemView.findViewById(R.id.dateDebtLendItem);
            this.valueView = itemView.findViewById(R.id.valueDebtLendItem);
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

