package com.hcmus.group14.moneytor.ui.spending;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.goal.AddGoalActivity;

import java.util.ArrayList;
import java.util.List;

public class SpendingActivity extends NoteBaseActivity<ActivitySpendingBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySpendingBinding binding;
    private SpendingViewModel spendingViewModel;
    private List<Spending> spendings;
    private SpendingAdapter spendingAdapter;
    private Context context;

    public SpendingActivity() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_spending;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        this.context = this.getApplicationContext();
        // do what you want
        this.setTitle("List spending");

        /*spendingViewModel = new ViewModelProvider(this).get(SpendingViewModel.class);
        spendingViewModel.getAllRentals().observe(this, new Observer<List<Rental>>() {
            @Override
            public void onChanged(@Nullable final List<Spending> spendingList) {
                spendings = spendingList;
                SpendingAdapter.setRental(rentalsList);
            }
        });*/
        spendings = getData();
        initializeViews();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddSpendingActivity.class);
                startActivity(intent);
            }
        });
    }

    List<Spending> getData() {
        List<Spending> data = new ArrayList<>();
        data.add(new Spending(10000000, "Title 1", "A", "This is a description", 1639586109000L));
        data.add(new Spending(10000, "Title 2", "A", "This is a description", 1639586109000L));
        data.add(new Spending(10000, "Title 3", "A", "This is a description", 1639586109000L));
        data.add(new Spending(100000, "Title 4", "A", "This is a description", 1639586109000L));
        data.add(new Spending(100000000, "Title 5", "A", "This is a description", 1639586109000L));
        data.add(new Spending(1000000, "Title 6", "A", "This is a description", 1639586109000L));
        data.add(new Spending(1000000, "Title 7", "A", "This is a description", 1639586109000L));
        data.add(new Spending(100000, "Title 8", "A", "This is a description", 1639586109000L));
        data.add(new Spending(100000, "Title 9", "A", "This is a description", 1639586109000L));
        data.add(new Spending(10000, "Title 10", "A", "This is a description", 1639586109000L));
        return data;
    }

    private void initializeViews() {
        spendingAdapter = new SpendingAdapter(this, spendings);
        binding.spendingList.setAdapter(spendingAdapter);
        binding.spendingList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.spending_menu, menu);
        //MenuItem searchItem = menu.findItem(R.id.actionSearch);
        return super.onCreateOptionsMenu(menu);
    }
}