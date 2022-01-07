package com.hcmus.group14.moneytor.ui.goal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.databinding.ActivityGoalBinding;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.FilterSelectUtils;

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

    private FilterSelectUtils filterSelectUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        context = this.getApplicationContext();

        this.setTitle(getString(R.string.toolbar_title_goal));

        initializeViews();

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        filterViewModel.getAllSpendGoal().observe(this, goalList -> {
            this.spendGoals = goalList;
            this.goalAdapter.setSpendGoals(goalList);
        });
        filterViewModel.setFilterState(new FilterState());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddGoalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        filterSelectUtils = new FilterSelectUtils(this);
        goalAdapter = new GoalAdapter(this, spendGoals);
        binding.spendingGoalList.setAdapter(goalAdapter);
        binding.spendingGoalList.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void showDialog() {
        AlertDialog alertDialog = filterSelectUtils.createMainDialog();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionFilter) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
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