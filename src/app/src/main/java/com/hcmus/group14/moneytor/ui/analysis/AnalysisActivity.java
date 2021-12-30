package com.hcmus.group14.moneytor.ui.analysis;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivityAnalysisBinding;
import com.hcmus.group14.moneytor.services.analyze.AnalyzeViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnalysisActivity extends NoteBaseActivity<ActivityAnalysisBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAnalysisBinding binding;
    private AnalyzeViewModel analyzeViewModel;
    private FilterViewModel filterViewModel;
    // data
    private CategoryItemStatisticsAdapter categoryAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_analysis;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Analysis");
        binding = getViewDataBinding();
        analyzeViewModel = new ViewModelProvider(this).get(AnalyzeViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        binding.setViewModel(analyzeViewModel);
        // binding observe
        filterViewModel.getAllSpending().observe(this, this::updateNewData);
        // TODO: receive intent and show filter by FilterState()
        filterViewModel.setFilterState(new FilterState());
        setCategoriesStatistics();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.analyze_visualize_menu, menu);
        MenuItem filterItem = menu.findItem(R.id.actionFilter);
        return true;
    }

    private void setCategoriesStatistics() {
        GridView gridView = binding.categoriesStatistics;
        final List<Category> categories = CategoriesUtils.getDefaultCategories();
        CategoryItemStatisticsAdapter adapter = new CategoryItemStatisticsAdapter(this,
                R.layout.category_item_statistics, categories);
        this.categoryAdapter = adapter;
        gridView.setAdapter(adapter);
    }

    // TODO: do everything when data change
    private void updateNewData(List<Spending> spendingList) {
        // update category statistics
        categoryAdapter.setItems(analyzeViewModel.getDetailsForCategories(spendingList));
        // update total amount
        binding.totalAmountAnalyze.setText(String.format(Locale.US ,"%,d", analyzeViewModel.getTotal(spendingList)) + " VNĐ");
        binding.averageByDateAnalyze.setText(String.format(Locale.US, "%,d", analyzeViewModel.getAverage(spendingList)) + " VNĐ");
        binding.highestSpendingAnalyze.setText(String.format("%,2d", analyzeViewModel.getMaxSpending(spendingList)) + " VNĐ");

        ArrayList<Category> highestCategory = analyzeViewModel.getMaxSpendingCategory(spendingList);
        binding.highestCategoryIcon.setImageResource(highestCategory.get(0).getResourceId());
        binding.highestCategoryIcon.setBackgroundTintList(ColorStateList.valueOf(highestCategory.get(0).getColor()));
    }

}