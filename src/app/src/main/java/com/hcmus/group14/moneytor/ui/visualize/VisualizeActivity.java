package com.hcmus.group14.moneytor.ui.visualize;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.compose.ui.graphics.drawscope.Fill;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
    private ArrayList<VisualizeViewModel.SpendingAmountInfo> pieAllEntries;
    private ArrayList<Category> pieLabels;
    private PieData pieData;

    private BarChart barChart;
    private HashMap<String, Long> barHashMapEntries;
    private ArrayList<VisualizeViewModel.SpendingPeriodInfo> barGroupedEntries;
    private ArrayList<String> barLabels;
    private BarData barData;

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

    private void updateNewData(List<Spending> spendingList) {
        // TODO: get daily/weekly/monthly/annually spending from view model
        barGroupedEntries = viewModel.getGroupedSpendingAmount(spendingList, VisualizeViewModel.FILTER_DAILY);
        pieAllEntries = viewModel.getSpendingProportionByCategory(spendingList);
        barHashMapEntries = viewModel.getDailySpendingAmount(spendingList);
        setPieChartData();
        pieChart.invalidate();
        setBarChartData();
        barChart.invalidate();
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    private void initPieChart() {
        pieChart.setTouchEnabled(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraRightOffset(40f);

        pieChart.setDrawEntryLabels(true);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        pieChart.setEntryLabelColor(Color.WHITE);
    }

    private void customLegend(Legend legend) {
        float height = pieChart.getHeight();
        float size = height / (5 * 13);
        Log.i("@@@ height", String.valueOf(height));
        Log.i("@@@ size", String.valueOf(size));
        LegendEntry[] legendEntries = new LegendEntry[pieLabels.size()];
        for (int i = 0; i < pieLabels.size(); i++) {
            legendEntries[i] = new LegendEntry(pieLabels.get(i).getName(), Legend.LegendForm.CIRCLE,
                    size, 2f, null, pieLabels.get(i).getColor());
        }
        legend.setCustom(legendEntries);
        legend.setTextSize(size);
    }

    private void setPieChartData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        pieLabels = new ArrayList<>();
        PieDataSet pieDataSet;

        Log.d("@@@ category", String.valueOf(pieAllEntries.size()));

        for (VisualizeViewModel.SpendingAmountInfo entry : pieAllEntries) {
            double percentage = entry.percentage;
            double minPercentageToShowLabelOnChart = 0.05d;
            if (percentage > minPercentageToShowLabelOnChart) {
                Drawable drawable = ContextCompat.getDrawable(VisualizeActivity.this,
                        entry.category.getResourceId());
                drawable.setTint(Color.parseColor("#FFFFFF"));
                pieEntries.add(new PieEntry(entry.amount, String.valueOf((int)(entry.percentage * 100)) + "%"));
                Log.d("@@@" + entry.category, String.valueOf(entry.percentage));
            } else {
                pieEntries.add(new PieEntry(entry.amount));
                Log.d("@@@" + entry.category, String.valueOf(entry.percentage));
            }
            pieColors.add(entry.category.getColor());
            pieLabels.add(entry.category);
        }

        customLegend(pieChart.getLegend());

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
        barLabels = new ArrayList<>();

        Log.d("@@@ date", String.valueOf(barGroupedEntries));

        int i = 0;
        for (VisualizeViewModel.SpendingPeriodInfo entry : barGroupedEntries){
            barEntries.add(new BarEntry(i, entry.periodAmount, ContextCompat.getDrawable(
                    VisualizeActivity.this, R.color.candy_pink)));
            barLabels.add(entry.period);
            i+=1;
        }
        Log.d("@@@ in set data", String.valueOf(barLabels));

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