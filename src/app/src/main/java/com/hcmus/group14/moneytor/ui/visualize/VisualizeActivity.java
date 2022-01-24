package com.hcmus.group14.moneytor.ui.visualize;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivityVisualizeBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.services.visualize.VisualizeViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.custom.CategoryLabelAdapter;
import com.hcmus.group14.moneytor.utils.FilterSelectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VisualizeActivity extends NoteBaseActivity<ActivityVisualizeBinding> {

    private ActivityVisualizeBinding binding;
    private VisualizeViewModel viewModel;

    private PieChart pieChart;
    private ArrayList<VisualizeViewModel.SpendingAmountInfo> pieAllEntries;
    private ArrayList<Category> pieLabels;
    private PieData pieData;

    private BarChart barChart;
    private HashMap<String, Long> barHashMapEntries;
    private ArrayList<VisualizeViewModel.SpendingPeriodInfo> barGroupedEntries;
    private BarData barData;

    private final FilterSelectUtils filterSelectUtils = new FilterSelectUtils(this);


    @Override
    public int getLayoutId() {
        return R.layout.activity_visualize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.toolbar_title_visualize));
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(VisualizeViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        binding.setViewModel(viewModel);
        pieAllEntries = new ArrayList<>();
        barHashMapEntries = new HashMap<>();
        barGroupedEntries = new ArrayList<>();
        // binding observe
        filterViewModel.getAllSpending().observe(this, this::updateNewData);
        // TODO: receive intent and show filter by FilterState()
        filterViewModel.setFilterState(new FilterState());

        pieChart = binding.pieChart;
        barChart = binding.barChart;
        initPieChart();
        initBarChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.analyze_visualize_menu, menu);
        MenuItem filterItem = menu.findItem(R.id.actionFilter);
        return true;
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

    public void setFilter(List<String> cats, String period) {
        // setup range filter there
        if (filterViewModel != null) {
            FilterState filterState = new FilterState(cats, period);
            // filterState.endDate = -1;
            // filterState.startDate = -1;
            filterViewModel.setFilterState(filterState);
        }
    }

    private void updateNewData(List<Spending> spendingList) {
        // TODO: get daily/weekly/monthly/annually spending from view model
        int filterType = filterViewModel.getFilterState().filterType;
        barGroupedEntries = viewModel.getGroupedSpendingAmount(spendingList,
                filterType
        //        ,VisualizeViewModel.LANG_JAPANESE
        );
        binding.barChartTitle.setText(getString(viewModel.getFilterTypeId(filterType)));
        pieAllEntries = viewModel.getSpendingProportionByCategory(spendingList);
        barHashMapEntries = viewModel.getDailySpendingAmount(spendingList);
        setPieChartData();
        pieChart.invalidate();
        setBarChartData();
        barChart.invalidate();
    }

    private void initPieChart() {
        pieChart.setTouchEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawEntryLabels(true);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setEntryLabelColor(Color.WHITE);
    }

    private void setPieChartLabels() {
        GridView labels = binding.pieChartLabels;
        CategoryLabelAdapter adapter = new CategoryLabelAdapter(this, R.layout.category_item_pie_chart_label, pieLabels);
        labels.setAdapter(adapter);
    }

    private void setPieChartData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        pieLabels = new ArrayList<>();
        PieDataSet pieDataSet;

        for (VisualizeViewModel.SpendingAmountInfo entry : pieAllEntries) {
            double percentage = entry.percentage;
            double minPercentageToShowLabelOnChart = 0.05d;
            if (percentage > minPercentageToShowLabelOnChart) {
                Drawable drawable = ContextCompat.getDrawable(VisualizeActivity.this,
                        entry.category.getResourceId());
                assert drawable != null;
                drawable.setTint(Color.parseColor("#FFFFFF"));
                pieEntries.add(new PieEntry(entry.amount, (int) (entry.percentage * 100) + "%"));
            } else {
                pieEntries.add(new PieEntry(entry.amount));
            }
            pieColors.add(entry.category.getColor());
            pieLabels.add(entry.category);
        }

        setPieChartLabels();

        if (pieChart.getData() != null && pieChart.getData().getDataSetCount() > 0) {
            pieDataSet = (PieDataSet) pieChart.getData().getDataSetByIndex(0);
            pieDataSet.setValues(pieEntries);
            pieChart.getData().notifyDataChanged();
            pieChart.notifyDataSetChanged();
        }
        else{
            pieColors.add(ColorTemplate.getHoloBlue());

            pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setDrawValues(false);
            pieDataSet.setColors(pieColors);
            pieDataSet.setSliceSpace(0.5f);

            pieData = new PieData(pieDataSet);
            pieData.setValueTextColor(Color.BLACK);
        }
        pieChart.setData(pieData);
    }

    private void initBarChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setTextSize(8f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(8f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(8f);

        barChart.getAxisRight().setEnabled(false);

        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
    }

    private void setBarChartData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> barLabels = new ArrayList<>();

        int i = 0;
        for (VisualizeViewModel.SpendingPeriodInfo entry : barGroupedEntries){
            barEntries.add(new BarEntry(i, entry.periodAmount, ContextCompat.getDrawable(
                    VisualizeActivity.this, R.color.candy_pink)));
            barLabels.add(entry.period);
            i+=1;
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barLabels));

        BarDataSet barDataSet;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(barEntries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            barDataSet = new BarDataSet(barEntries, "");

            int color = ContextCompat.getColor(this, R.color.candy_pink);
            barDataSet.setDrawIcons(false);
            barDataSet.setColor(color);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            barData = new BarData(dataSets);
            barData.setDrawValues(false);
            barData.setBarWidth(0.9f);
        }
        barChart.setData(barData);
    }

}