package com.hcmus.group14.moneytor.ui.goal;

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
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

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
        Category category = CategoriesUtils.findCategoryById(currentSpendingGoal.getCategory());
        holder.setImage(category.getColor(),category.getResourceId());
    }

    public void setSpendGoals(List<SpendGoal> goalList){
        goals = goalList;
        notifyDataSetChanged();
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
        private final TextView descView;
        private final TextView dateView;
        private final TextView valueView;
        private final GoalAdapter adapter;
        private final ImageView imageView;


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

        public GoalViewHolder(@NonNull View itemView, GoalAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.descView = itemView.findViewById(R.id.descSpendingGoalItem);
            this.dateView = itemView.findViewById(R.id.dateSpendingGoalItem);
            this.valueView = itemView.findViewById(R.id.valueSpendingGoalItem);
            this.imageView = itemView.findViewById(R.id.categorySpendingGoalItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // open activity
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, AddGoalActivity.class);
            intent.putExtra("goal_obj", goals.get(position));
            context.startActivity(intent);
        }
    }
}

