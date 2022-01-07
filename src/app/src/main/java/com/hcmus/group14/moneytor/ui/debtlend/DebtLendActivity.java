package com.hcmus.group14.moneytor.ui.debtlend;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.databinding.ActivityDebtLendBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.FilterSelectUtils;

import java.util.ArrayList;
import java.util.List;

public class DebtLendActivity extends NoteBaseActivity<ActivityDebtLendBinding> {

    private ActivityDebtLendBinding binding;
    private List<DebtLendAndRelate> debtLends;
    private DebtLendAdapter debtLendAdapter;
    private Context context;
    SearchView searchView;

    private FilterSelectUtils filterSelectUtils;


    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_lend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        context = this.getApplicationContext();

        this.setTitle(getString(R.string.toolbar_title_debtlend));
        initializeViews();

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        filterViewModel.getAllDebtLend().observe(this, debtLends -> {
            this.debtLends = debtLends;
            debtLendAdapter.setDebtLends(debtLends);
        });
        filterViewModel.setFilterState(new FilterState());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDebtLendActivity.class);
                startActivity(intent);
            }
        });
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

    List<DebtLend> getData() {
        List<DebtLend> data = new ArrayList<>();
        data.add(new DebtLend(Category.PETS.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.MAKEUP.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.ENTERTAINMENT.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.OTHERS.getId(), 100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.HEALTH.getId(), 100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.HEALTH.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.FITNESS.getId(), 100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.TRAFFIC.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.MAINTENANCE.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.FOOD_AND_DRINK.getId(), 100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.BILLS.getId(), 100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend(Category.UTILITIES.getId(), 100000,1,1,1639586109000L,"this is a description"));
        return data;
    }

    private void initializeViews() {
        filterSelectUtils = new FilterSelectUtils(this);
        debtLendAdapter = new DebtLendAdapter(this, debtLends);
        binding.debtLendList.setAdapter(debtLendAdapter);
        binding.debtLendList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<DebtLendAndRelate> filter(String text) {
        List<DebtLendAndRelate> filteredList = new ArrayList<>();
        for (DebtLendAndRelate item : debtLends) {
            if (item.debtLend.getDesc().toLowerCase().contains(text.toLowerCase())) {
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
                debtLendAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                debtLendAdapter.filterList(filter(s));
                return false;
            }
        });
        return true;
    }
}