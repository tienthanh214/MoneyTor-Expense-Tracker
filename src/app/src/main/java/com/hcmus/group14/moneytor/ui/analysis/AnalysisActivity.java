package com.hcmus.group14.moneytor.ui.analysis;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

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

import java.util.List;

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
        binding.totalAmountAnalyze.setText(String.format("%,2d", analyzeViewModel.getTotal(spendingList)));
        binding.averageByDateAnalyze.setText(String.format("%,2d", analyzeViewModel.getAverage(spendingList)));

    }

}