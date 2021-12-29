package com.hcmus.group14.moneytor.ui.analysis;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizeActivity extends NoteBaseActivity<ActivityVisualizeBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityVisualizeBinding binding;
    private VisualizeViewModel viewModel;
    private FilterViewModel filterViewModel;

    private PieChart pieChart;
    private HashMap<Category, VisualizeViewModel.SpendingAmountInfo> pieHashMapEntries;
    private PieData pieData;

    private BarChart barChart;
    private HashMap<String, Long> barHashMapEntries;
    private BarData barData;

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
//        initPieChart();
//        initBarChart();
        // binding observe
        filterViewModel.getAllSending().observe(this, this::updateNewData);
        // TODO: receive intent and show filter by FilterState()
        filterViewModel.setFilterState(new FilterState());


    }

    private void updateNewData(List<Spending> spendingList) {
        Log.i("@@@ vis", spendingList.toString());
//        pieHashMapEntries = viewModel.getSpendingProportionByCategory(spendingList);
        barHashMapEntries = viewModel.getDailySpendingAmount(spendingList);
        initPieChart();
        initBarChart();
        // pieChart.getData().notifyDataChanged();
        // pieChart.notifyDataSetChanged();
        for (Category data : pieHashMapEntries.keySet()) {
            Log.i("@@@ data", data.toString() + " " + pieHashMapEntries.get(data).toString());
        }
    }

    private void initPieChart() {
        pieChart = binding.pieChart;

        setPieChartData();
        pieChart.setData(pieData);
        pieChart.setTouchEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.dark_sea_green));
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(270);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.invalidate();
    }

    private void setPieChartData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        PieDataSet pieDataSet;

        Log.d("@@@ category", String.valueOf(pieHashMapEntries.size()));

        for (Map.Entry<Category, VisualizeViewModel.SpendingAmountInfo> category : pieHashMapEntries.entrySet()) {
            double percentage = category.getValue().percentage;
            double minPercentageToShowLabelOnChart = 0.1d;

            if (percentage > minPercentageToShowLabelOnChart) {
                Drawable drawable = ContextCompat.getDrawable(VisualizeActivity.this,
                        category.getKey().getResourceId());
                drawable.setTint(Color.parseColor("#FFFFFF"));
                pieEntries.add(new PieEntry(category.getValue().amount, drawable));
                Log.d("@@@" + category.getKey(), String.valueOf(category.getValue().amount));
                //pieEntries.add(new PieEntry(20l, drawable));
            } else {
                pieEntries.add(new PieEntry(category.getValue().amount));
                Log.d("@@" + category.getKey(), String.valueOf(category.getValue().amount));
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

        /*for (Category category : Category.values()){
            Drawable drawable = ContextCompat.getDrawable(VisualizeActivity.this,
                    category.getResourceId());
            drawable.setTint(Color.parseColor("#FFFFFF"));
            pieEntries.add(new PieEntry(10l, drawable));
            pieColors.add(category.getColor());
        }*/
    }

    private void initBarChart() {
        barChart = binding.barChart;

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
        xAxis.setLabelCount(7);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false);

        setBarChartData();
        barChart.setData(barData);
        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.invalidate();

    }

    private void setBarChartData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        Log.d("@@@ date", String.valueOf(barHashMapEntries.size()));

        int i = 1;
        for (Map.Entry<String, Long> date : barHashMapEntries.entrySet()){
            barEntries.add(new BarEntry(i, date.getValue(), ContextCompat.getDrawable(
                    VisualizeActivity.this, R.color.candy_pink)));
            i+=1;
        }

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
            //barData.setValueTextSize(10f);
            //barData.setBarWidth(0.9f);

        }
    }

}