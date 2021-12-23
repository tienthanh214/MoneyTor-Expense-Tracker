package com.hcmus.group14.moneytor.ui.debtlend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivityDebtLendBinding;
import com.hcmus.group14.moneytor.databinding.ActivityGoalBinding;
import com.hcmus.group14.moneytor.services.debtlend.DebtLendViewModel;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.goal.AddGoalActivity;
import com.hcmus.group14.moneytor.ui.goal.GoalAdapter;

import java.util.ArrayList;
import java.util.List;

public class DebtLendActivity extends NoteBaseActivity<ActivityDebtLendBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDebtLendBinding binding;
    private DebtLendViewModel debtLendViewModel;
    private List<DebtLend> debtLends;
    private DebtLendAdapter debtLendAdapter;
    private Context context;
    SearchView searchView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_lend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        context = this.getApplicationContext();

        this.setTitle("Manage debt");
        debtLends = getData();
        initializeViews();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDebtLendActivity.class);
                startActivity(intent);
            }
        });
    }

    List<DebtLend> getData() {
        List<DebtLend> data = new ArrayList<>();
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        return data;
    }

    private void initializeViews() {
        debtLendAdapter = new DebtLendAdapter(this, debtLends);
        binding.debtLendList.setAdapter(debtLendAdapter);
        binding.debtLendList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<DebtLend> filter(String text) {
        List<DebtLend> filteredList = new ArrayList<>();
        for (DebtLend item : debtLends) {
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