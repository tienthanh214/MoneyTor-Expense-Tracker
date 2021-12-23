package com.hcmus.group14.moneytor.ui.analysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.databinding.ActivityAnalysisBinding;
import com.hcmus.group14.moneytor.services.analyze.AnalyzeViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.spending.SpendingDetailsViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;

import java.util.List;

public class AnalysisActivity extends NoteBaseActivity<ActivityAnalysisBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAnalysisBinding binding;
    private AnalyzeViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_analysis;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Analysis");
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(AnalyzeViewModel.class);
        binding.setViewModel(viewModel);

        setCategoriesStatistics();
    }

    private void setCategoriesStatistics() {
        GridView gridView = binding.categoriesStatistics;
        final List<Category> categories = CategoriesUtils.getDefaultCategories();
        CategoryItemStatisticsAdapter adapter = new CategoryItemStatisticsAdapter(this,
                R.layout.category_item_statistics, categories, viewModel);

        gridView.setAdapter(adapter);
    }

}