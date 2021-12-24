package com.hcmus.group14.moneytor.ui.goal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.databinding.ActivityGoalBinding;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.spending.SpendingActivity;

import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends NoteBaseActivity<ActivityGoalBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGoalBinding binding;
    private SpendGoalViewModel goalViewModel;
    private List<SpendGoal> spendGoals;
    private GoalAdapter goalAdapter;
    private Context context;
    private SearchView searchView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        context = this.getApplicationContext();

        this.setTitle("Spending goal");

        spendGoals = getData();
        initializeViews();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddGoalActivity.class);
                startActivity(intent);
            }
        });
    }

    List<SpendGoal> getData() {
        List<SpendGoal> data = new ArrayList<>();
        data.add(new SpendGoal(Category.MAKEUP.getId(), 100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.EDUCATION.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.TRAFFIC.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.FOOD_AND_DRINK.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.FITNESS.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.BILLS.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.APPLIANCES.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.ENTERTAINMENT.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.MAKEUP.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.HEALTH.getId(),100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal(Category.OTHERS.getId(),100000,1639586109000L,"This is a description"));
        return data;
    }

    private void initializeViews() {
        goalAdapter = new GoalAdapter(this, spendGoals);
        binding.spendingGoalList.setAdapter(goalAdapter);
        binding.spendingGoalList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<SpendGoal> filter(String text) {
        List<SpendGoal> filteredList = new ArrayList<>();
        for (SpendGoal item : spendGoals) {
            if (item.getDesc().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.spending_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                goalAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                goalAdapter.filterList(filter(s));
                return false;
            }
        });
        return true;
    }
}