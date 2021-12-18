package com.hcmus.group14.moneytor.ui.goal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private LayoutInflater layoutInflater;
    private List<SpendGoal> goals;
    private Context context;

    public GoalAdapter(Context context, List<SpendGoal> goalList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.goals = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_goal, parent, false);
        GoalViewHolder holder = new GoalViewHolder(itemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        SpendGoal currentSpendingGoal = goals.get(position);
        holder.setDesc(currentSpendingGoal.getDesc());
        holder.setValue(currentSpendingGoal.getSpendingCap());
        holder.setDate(currentSpendingGoal.getDate());
    }


    @Override
    public int getItemCount() {
        if (goals != null) return goals.size();
        else return 0;
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView descView;
        private TextView dateView;
        private TextView valueView;
        private GoalAdapter adapter;


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

        public GoalViewHolder(@NonNull View itemView, GoalAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.descView = itemView.findViewById(R.id.descSpendingGoalItem);
            this.dateView = itemView.findViewById(R.id.dateSpendingGoalItem);
            this.valueView = itemView.findViewById(R.id.valueSpendingGoalItem);
        }

        @Override
        public void onClick(View v) {
            // open activity
        }
    }
}

