package com.hcmus.group14.moneytor.ui.visualize;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.compose.ui.graphics.drawscope.Fill;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivityAnalysisBinding;
import com.hcmus.group14.moneytor.databinding.ActivityVisualizeBinding;
import com.hcmus.group14.moneytor.services.analyze.AnalyzeViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;
import com.hcmus.group14.moneytor.services.visualize.VisualizeViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizeActivity extends NoteBaseActivity<ActivityVisualizeBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityVisualizeBinding binding;
    private VisualizeViewModel viewModel;
    private FilterViewModel filterViewModel;
    private int mod = 0;

    private PieChart pieChart;
    private HashMap<Category, VisualizeViewModel.SpendingAmountInfo> pieHashMapEntries;
    private PieData pieData;

    private BarChart barChart;
    private HashMap<String, Long> barHashMapEntries;
    private HashMap<String, Long> barGroupedEntries;
    private BarData barData;
    private ArrayList<String> label;

    final private int DAILY_MOD = 1;
    final private int WEEKLY_MOD = 2;
    final private int MONTHLY_MOD = 3;
    final private int ANNUALLY_MOD = 4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_visualize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Visualize");
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(VisualizeViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        binding.setViewModel(viewModel);
        pieHashMapEntries = new HashMap<>();
        barHashMapEntries = new HashMap<>();
        barGroupedEntries = new HashMap<>();
        // binding observe
        filterViewModel.getAllSpending().observe(this, this::updateNewData);
        // TODO: receive intent and show filter by FilterState()
        filterViewModel.setFilterState(new FilterState());

        pieChart = binding.pieChart;
        barChart = binding.barChart;
        initPieChart();
        initBarChart();
    }

    private void updateNewData(List<Spending> spendingList) {
        // TODO: get daily/weekly/monthly/annually spending from view model
        //barGroupedEntries = viewModel.getGroupedSpendingAmount(spendingList, VisualizeViewModel.FILTER_WEEKLY);
        pieHashMapEntries = viewModel.getSpendingProportionByCategory(spendingList);
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

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.dark_sea_green));
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
    }

    private void setPieChartData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        PieDataSet pieDataSet;

        Log.d("@@@ category", String.valueOf(pieHashMapEntries.size()));

        for (Map.Entry<Category, VisualizeViewModel.SpendingAmountInfo> category : pieHashMapEntries.entrySet()) {
            double percentage = category.getValue().percentage;
            double minPercentageToShowLabelOnChart = 0.08d;

            if (percentage > minPercentageToShowLabelOnChart) {
                Drawable drawable = ContextCompat.getDrawable(VisualizeActivity.this,
                        category.getKey().getResourceId());
                drawable.setTint(Color.parseColor("#FFFFFF"));
                pieEntries.add(new PieEntry(category.getValue().amount, drawable));
                Log.d("@@@" + category.getKey(), String.valueOf(category.getValue().percentage));
                //pieEntries.add(new PieEntry(20l, drawable));
            } else {
                pieEntries.add(new PieEntry(category.getValue().amount));
                Log.d("@@@" + category.getKey(), String.valueOf(category.getValue().percentage));
                //pieEntries.add(new PieEntry(5l));
            }
            pieColors.add(category.getKey().getColor());
        }

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
            pieDataSet.setSliceSpace(2f);

            pieData = new PieData(pieDataSet);
        }
        pieChart.setData(pieData);
    }

    private void initBarChart() {

        //barChart.setOnChartValueSelectedListener(this);

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
        xAxis.setTextSize(6f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(6f);

        barChart.getAxisRight().setEnabled(false);

        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
    }

    private void setBarChartData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        label = new ArrayList<>();

        Log.d("@@@ date", String.valueOf(barHashMapEntries.size()));

        int i = 0;
        for (Map.Entry<String, Long> date : barHashMapEntries.entrySet()){
            barEntries.add(new BarEntry(i, date.getValue(), ContextCompat.getDrawable(
                    VisualizeActivity.this, R.color.candy_pink)));
            label.add(date.getKey());
            i+=1;
        }
        Log.d("@@@ in set data", String.valueOf(label));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));

        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(barEntries, "");

            int color = ContextCompat.getColor(this, R.color.candy_pink);
            set1.setDrawIcons(false);
            set1.setColor(color);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            barData = new BarData(dataSets);
            barData.setDrawValues(false);
            //barData.setBarWidth(0.9f);
        }
        barChart.setData(barData);
    }

}