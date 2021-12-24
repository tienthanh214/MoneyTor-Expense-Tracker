package com.hcmus.group14.moneytor.ui.goal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<SpendGoal> goals;
    private final Context context;

    public GoalAdapter(Context context, List<SpendGoal> goalList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.goals = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_goal, parent, false);
        return new GoalViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        SpendGoal currentSpendingGoal = goals.get(position);
        holder.setDesc(currentSpendingGoal.getDesc());
        holder.setValue(currentSpendingGoal.getSpendingCap());
        holder.setDate(currentSpendingGoal.getDate());
    }

    public void filterList(List<SpendGoal> filteredList){
        goals=filteredList;
        notifyDataSetChanged();
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
            dateView.setText(DateTimeUtils.getDate(date));
        }

        public void setValue(long value) {
            valueView.setText(String.format(Locale.US, "%,d", value) + " VNĐ");
        }

        public GoalViewHolder(@NonNull View itemView, GoalAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.descView = itemView.findViewById(R.id.descSpendingGoalItem);
            this.dateView = itemView.findViewById(R.id.dateSpendingGoalItem);
            this.valueView = itemView.findViewById(R.id.valueSpendingGoalItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // open activity
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, AddGoalActivity.class);
            intent.putExtra("spending_id", goals.get(position).getGoalID());
            context.startActivity(intent);
        }
    }
}

