package com.hcmus.group14.moneytor.ui.spending;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Note;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class SpendingActivity extends NoteBaseActivity<ActivitySpendingBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySpendingBinding binding;
    private SpendingViewModel spendingViewModel;
    private List<Spending> spendings;
    private SpendingAdapter spendingAdapter;
    private Context context;
    private SearchView searchView;

    private FilterViewModel filterViewModel;

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
        this.setTitle(getString(R.string.toolbar_goal_spending));
        initializeViews();

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        filterViewModel.getAllSpending().observe(this, spendingList -> {
            spendings = spendingList;
            spendingAdapter.setSpending(spendings);
        });
        
        filterViewModel.setFilterState(new FilterState());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddSpendingActivity.class);
                startActivity(intent);
            }
        });
    }

    // for test filter, should be delete
    void testFilter() {
        List<String> cats = new ArrayList<>();
        cats.add(Category.FOOD_AND_DRINK.getId());
//        filterViewModel.setFilterState(new FilterState(cats, DateTimeUtils.getDateInMillis(9, 12, 2021), DateTimeUtils.getDateInMillis(22, 12, 2021)));
        filterViewModel.setFilterState(new FilterState(cats, DateTimeUtils.getMillisByDate(10, 12, 2021), DateTimeUtils.getMillisByDate(22, 12, 2021)));
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

    private List<Spending> filter(String text) {
        List<Spending> filteredList = new ArrayList<>();
        for (Spending item : spendings) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
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
                spendingAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                spendingAdapter.filterList(filter(s));
                return false;
            }
        });
        return true;
    }
}