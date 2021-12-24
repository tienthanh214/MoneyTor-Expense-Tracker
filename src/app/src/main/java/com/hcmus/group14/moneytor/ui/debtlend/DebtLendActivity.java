package com.hcmus.group14.moneytor.ui.debtlend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.databinding.ActivityDebtLendBinding;
import com.hcmus.group14.moneytor.services.debtlend.DebtLendViewModel;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DebtLendActivity extends NoteBaseActivity<ActivityDebtLendBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDebtLendBinding binding;
    private DebtLendViewModel debtLendViewModel;
    private List<DebtLendAndRelate> debtLends;
    private DebtLendAdapter debtLendAdapter;
    private Context context;
    SearchView searchView;

    private FilterViewModel viewModel;

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
        initializeViews();

//        debtLendViewModel = new ViewModelProvider(this).get(DebtLendViewModel.class);
//        debtLendViewModel.getAllDebtLends().observe(this, debtLends -> {
//            this.debtLends = debtLends;
//            debtLendAdapter.setDebtLends(debtLends);
//            // TODO: bug query not correct target id
//            for (int i = 0; i < debtLends.size(); ++i) {
//                Log.i("@@@ debt ", debtLends.get(i).debtLend.toString());
//                Log.i("@@@ target", debtLends.get(i).relate.getRelateId() + " ");
//            }
//        });
        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.getAllDebtLend().observe(this, debtLends -> {
            this.debtLends = debtLends;
            debtLendAdapter.setDebtLends(debtLends);
        });
        viewModel.setFilterState(new FilterState());

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
        data.add(new DebtLend("A",100000,2,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,3,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,2,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,1,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,1,0,1639586109000L,"this is a description"));
        data.add(new DebtLend("A",100000,3,0,1639586109000L,"this is a description"));
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