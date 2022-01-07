package com.hcmus.group14.moneytor.services.analyze;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: proper parameterization
public class AnalyzeViewModel extends AndroidViewModel {
//    private List<Spending> allSpending;
//
//    private static final long DAILY = 1000 * 24 * 60 * 60;
//    public static final long WEEKLY = 7 * DAILY;
//    public static final long MONTHLY = 30 * DAILY;
//    public static final long ANNUALLY = 365 * DAILY;

    public AnalyzeViewModel(@NonNull Application application) {
        super(application);
    }


    //Get total amount of spendings filtered
    public long getTotal(List<Spending> spendings)
    {
        if (spendings == null) return 0L;
        long sum = 0L;
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum;
    }

    //Get the average
    public long getAverage(List<Spending> spendings)
    {
        if (spendings == null) return 0;
        if (spendings.size() == 0) return 0;
        long sum = 0L;
        
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum / (long)spendings.size();
    }

    //Get the highest spending across the filtered.
    public long getMaxSpending(List<Spending> spendings)
    {
        if (spendings == null) return 0L;
        long max = 0;
        for (Spending spending: spendings)
            if (max < spending.getCost())
                max = spending.getCost();
        return max;
    }

    //Get the day where the amount of spending is highest
    public ArrayList<Category> getMaxSpendingCategory(List<Spending> spendings)
    {
        long max = 0;
        ArrayList<Category> categories = new ArrayList<>();
        if (spendings == null) return categories;
        for (Spending spending: spendings)
            if (max < spending.getCost()) {
                max = spending.getCost();
            }
        for (Spending spending: spendings)
        {
            if (max == spending.getCost())
                categories.add(Category.valueOf(spending.getCategory()));
        }
        
        return categories;
    }

    //Count how much have been spent in categories in appearance across all spendings filtered.
    public HashMap<Category, Long> getDetailsForCategories
            (List<Spending> spendings)
    {
        HashMap<Category, Long> returnResult = new HashMap<>();
        if (spendings == null) return returnResult;
        ArrayList<Category> categories = new ArrayList<>();
        //Digs all categories available in all filtered spendings
        for (Spending spending : spendings) {
            try {
                if (!categories.contains(Category.valueOf(spending.getCategory())))
                    categories.add(Category.valueOf(spending.getCategory()));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        //Binds categories and their corresponding amount spent to the map
        for (Category category : categories)
        {
            long val = 0l;
            for (Spending spending: spendings)
                if (spending.getCategory().equals(category.toString()))
                    val += spending.getCost();
            returnResult.put(category, val);
        }
        return returnResult;
    }
}
