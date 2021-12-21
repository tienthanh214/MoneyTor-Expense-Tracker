package com.hcmus.group14.moneytor.data.model;

import com.hcmus.group14.moneytor.services.options.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterState implements Serializable {
    public List<String> categories;
    public long startDate;
    public long endDate;

    public FilterState() {
        this(null, -1, -1);
    }

    public FilterState(List<String> cats, long startDate, long endDate) {
        this.categories = cats;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setCategories(List<Category> cats) {
        if (categories == null)
            categories = new ArrayList<>();
        for (Category cat : cats) {
            categories.add(cat.getId());
        }
    }
}
