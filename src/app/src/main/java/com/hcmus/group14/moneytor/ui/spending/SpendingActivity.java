package com.hcmus.group14.moneytor.ui.spending;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpendingActivity extends NoteBaseActivity<ActivitySpendingBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySpendingBinding binding;
    private SpendingViewModel spendingViewModel;
    private List<Spending> spendings;
    private SpendingAdapter spendingAdapter;
    private Context context;
    private SearchView searchView;
    List<Category> selectedCategories;
    List<String> selectedCategoriesString;
    List<Category> allCategories = CategoriesUtils.getDefaultCategories();
    HashMap<String, Category> stringToCategory;
    String[] timePeriods = {"Week","Month","Year"};
    String selectedPeriod = "";
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

    private void initializeViews() {
        stringToCategory = new HashMap<String, Category>();
        for (Category category : allCategories) {
            stringToCategory.put(category.getName(), category);
        }
        spendingAdapter = new SpendingAdapter(this, spendings);
        binding.spendingList.setAdapter(spendingAdapter);
        binding.spendingList.setLayoutManager(new LinearLayoutManager(this));
    }

    AlertDialog createPeriodsDialog(View parentView){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.filter_select_theme);
        View v = getLayoutInflater().inflate(R.layout.filter_select_2, null);
        builder.setView(v);
        builder.setCancelable(false);
        Button confirmBtn = (Button) v.findViewById(R.id.confirmBtn);
        Button cancelBtn = (Button) v.findViewById(R.id.cancelBtn);
        LinearLayout wrapper = (LinearLayout) v.findViewById(R.id.groupOfSelection);
        RadioGroup radioGroup = new RadioGroup(this);
        for(int i=0;i<timePeriods.length;i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(timePeriods[i]);
            radioButton.setTextColor(Color.parseColor("#ffffff"));
            radioGroup.addView(radioButton);
            if(timePeriods[i].equals(selectedPeriod)) radioButton.setChecked(true);
        }
        wrapper.addView(radioGroup);
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView tv = (TextView) parentView.findViewById(R.id.periodSelect);
                for(int i=0;i<timePeriods.length;i++){
                    RadioButton radioButton =(RadioButton) radioGroup.getChildAt(i);
                    if(radioButton.isChecked()) selectedPeriod = radioButton.getText().toString();
                }
                if(selectedPeriod=="") tv.setText("Tap to select period");
                else tv.setText(selectedPeriod);
                alertDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    AlertDialog createCategoriesDialog(View parentView) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.filter_select_theme);
        View v = getLayoutInflater().inflate(R.layout.filter_select_2, null);
        builder.setView(v);
        builder.setCancelable(false);
        Button confirmBtn = (Button) v.findViewById(R.id.confirmBtn);
        Button cancelBtn = (Button) v.findViewById(R.id.cancelBtn);
        LinearLayout checkboxGroup = (LinearLayout) v.findViewById(R.id.groupOfSelection);
        for (Category category : allCategories) {
            CheckBox cb = new CheckBox(this);
            cb.setText(category.getName());
            cb.setTextColor(Color.parseColor("#ffffff"));
            if (selectedCategoriesString.contains(category.getName())) cb.setChecked(true);
            checkboxGroup.addView(cb);
        }
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) parentView.findViewById(R.id.categoriesSelect);
                List<String> newSelectedString = new ArrayList<>();
                List<Category> newSelectedCategory = new ArrayList<>();
                for (int i = 0; i < checkboxGroup.getChildCount(); i++) {
                    CheckBox check = (CheckBox) checkboxGroup.getChildAt(i);
                    if (check.isChecked()) {
                        String temp = check.getText().toString();
                        newSelectedString.add(temp);
                        newSelectedCategory.add(stringToCategory.get(temp));
                    }
                }
                selectedCategories = newSelectedCategory;
                selectedCategoriesString = newSelectedString;
                if (selectedCategoriesString.size() == 0) tv.setText("Tap to select categories");
                else tv.setText(String.join(", ", selectedCategoriesString));
                alertDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    AlertDialog createMainDialog() {
        AlertDialog.Builder builder;
        selectedCategories = new ArrayList<>();
        selectedCategoriesString = new ArrayList<>();
        selectedPeriod = "";
        builder = new AlertDialog.Builder(this, R.style.filter_select_theme);
        View v = getLayoutInflater().inflate(R.layout.filter_select, null);
        Button confirmBtn = (Button) v.findViewById(R.id.confirmBtn);
        Button cancelBtn = (Button) v.findViewById(R.id.cancelBtn);
        TextView categoriesSelect = (TextView) v.findViewById(R.id.categoriesSelect);
        TextView periodSelect = (TextView) v.findViewById(R.id.periodSelect);
        builder.setView(v);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // filter data lại từ selectedCategories
                alertDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        categoriesSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog categoriesDialog = createCategoriesDialog(v);
                categoriesDialog.show();
            }
        });
        periodSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog periodDialog = createPeriodsDialog(v);
                periodDialog.show();
            }
        });
        return alertDialog;
    }

    void showDialog() {
        AlertDialog alertDialog = createMainDialog();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionFilter:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
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