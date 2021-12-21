package com.hcmus.group14.moneytor.services.analyze;

import android.app.Application;
import androidx.annotation.NonNull;


import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: proper parameterization
public class AnalyzeViewModel extends AppViewModel {
    private List<Spending> allSpending;

    private static final long DAILY = 1000 * 24 * 60 * 60;
    public static final long WEEKLY = 7 * DAILY;
    public static final long MONTHLY = 30 * DAILY;
    public static final long ANNUALLY = 365 * DAILY;

    public AnalyzeViewModel(@NonNull Application application) {
        super(application);
        allSpending = appRepository.getAllSpending().getValue();
    }

    public ArrayList<Spending> getSpendingDetails(long duration)
    {
        long now = DateTimeUtils.getCurrentTimeMillis();
        assert(duration == WEEKLY ||
                duration == MONTHLY || duration == ANNUALLY);
        ArrayList<Spending> spendingInDuration = new ArrayList<>();
        for (Spending spending: allSpending)
        {
            if (now - spending.getDate() <= duration)
                spendingInDuration.add(spending);
        }
        return spendingInDuration;
    }

    public long getTotal(long duration)
    {
        ArrayList<Spending> spendings = getSpendingDetails(duration);
        long sum = 0l;
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum;
    }

    public long getAverage(long duration)
    {
        ArrayList<Spending> spendings = getSpendingDetails(duration);
        long sum = 0l;
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum / (long)spendings.size();
    }

    public long getMaxSpending(long duration)
    {
        ArrayList<Spending> spendings = getSpendingDetails(duration);
        long max = 0;
        for (Spending spending: spendings)
            if (max < spending.getCost())
                max = spending.getCost();
        return max;
    }

    public HashMap<Category, Long> getDetailsForCategories
            (List<Category> categories, long duration)
    {
        assert(duration == WEEKLY ||
                duration == MONTHLY || duration == ANNUALLY);
        long now = DateTimeUtils.getCurrentTimeMillis();
        ArrayList<String> categoriesInString = new ArrayList<>();

        for (Category category: categories)
            categoriesInString.add(category.toString());
        ArrayList<Spending> filteredSpending =
                (ArrayList<Spending>) appRepository.getSpendingByCategories(categoriesInString);

        HashMap<Category, Long> returnResult = new HashMap<>();


        for (Category category: categories)
        {
            long val = 0l;
            for (Spending spending: filteredSpending)
                if (spending.getCategory().equals(category.toString())
                        && now - spending.getDate() <= duration)
                    val += spending.getCost();
            returnResult.put(category, val);
        }
        return returnResult;
    }
}
