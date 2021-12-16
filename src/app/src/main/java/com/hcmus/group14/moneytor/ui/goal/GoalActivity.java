package com.hcmus.group14.moneytor.ui.goal;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.databinding.ActivityGoalBinding;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends NoteBaseActivity<ActivityGoalBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGoalBinding binding;
    private SpendGoalViewModel goalViewModel;
    private List<SpendGoal> spendGoals;
    private GoalAdapter goalAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();

        this.setTitle("Spending goal");

        /*spendingViewModel = new ViewModelProvider(this).get(SpendingViewModel.class);
        spendingViewModel.getAllRentals().observe(this, new Observer<List<Rental>>() {
            @Override
            public void onChanged(@Nullable final List<Spending> spendingList) {
                spendings = spendingList;
                SpendingAdapter.setRental(rentalsList);
            }
        });*/
        spendGoals = getData();
        initializeViews();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    List<SpendGoal> getData() {
        List<SpendGoal> data = new ArrayList<>();
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        data.add(new SpendGoal("A",100000,1639586109000L,"This is a description"));
        return data;
    }

    private void initializeViews() {
        goalAdapter = new GoalAdapter(this, spendGoals);
        binding.spendingGoalList.setAdapter(goalAdapter);
        binding.spendingGoalList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.spending_menu, menu);
        //MenuItem searchItem = menu.findItem(R.id.actionSearch);
        return super.onCreateOptionsMenu(menu);
    }
}