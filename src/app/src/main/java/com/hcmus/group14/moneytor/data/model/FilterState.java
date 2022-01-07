package com.hcmus.group14.moneytor.data.model;

import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.visualize.VisualizeViewModel;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterState implements Serializable {
    final static public String WEEK = "Week";
    final static public String MONTH = "Month";
    final static public String YEAR = "Year";
    static public final String[] timePeriods = {WEEK, MONTH, YEAR};

    public List<String> categories;
    public long startDate;
    public long endDate;
    public int filterType;

    public FilterState() {
        this(null, -1, -1);
    }

    public FilterState(List<String> cats, long startDate, long endDate) {
        this.categories = cats;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public FilterState(List<String> cats, String period) {
        this.categories = cats;
        this.endDate = DateTimeUtils.getCurrentTimeMillis();
        switch (period) {
            case WEEK:
                this.startDate = this.endDate - DateTimeUtils.WEEKLY_INTERVAL;
                filterType = VisualizeViewModel.FILTER_WEEKLY;
                break;
            case MONTH:
                this.startDate = this.endDate - DateTimeUtils.MONTHLY_INTERVAL;
                filterType = VisualizeViewModel.FILTER_MONTHLY;
                break;
            case YEAR:
                this.startDate = this.endDate - DateTimeUtils.ANNUALLY_INTERVAL;
                filterType = VisualizeViewModel.FILTER_ANNUALLY;
                break;
            default:
                this.startDate = -1;
                this.endDate = -1;
                filterType = VisualizeViewModel.FILTER_DAILY;
                break;
        }

    }

    public void setCategories(List<Category> cats) {
        if (categories == null)
            categories = new ArrayList<>();
        for (Category cat : cats) {
            categories.add(cat.getId());
        }
    }


}
