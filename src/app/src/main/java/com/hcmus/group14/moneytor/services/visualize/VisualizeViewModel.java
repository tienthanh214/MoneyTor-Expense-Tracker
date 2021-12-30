package com.hcmus.group14.moneytor.services.visualize;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;


import com.hcmus.group14.moneytor.data.local.dao.SpendingDao;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class VisualizeViewModel extends AndroidViewModel {
    public static final int FILTER_DAILY = -1,
            FILTER_WEEKLY = 0, FILTER_MONTHLY = 1, FILTER_ANNUALLY = 2;

    public class SpendingAmountInfo
    {
        public Category category;
        public long amount;
        public double percentage;

        public SpendingAmountInfo(Category category, long amount, double percentage)
        {
            this.category = category;
            this.amount = amount;
            this.percentage = percentage;
        }

        public String toString() {
            return "SpendingAmountInfo{" + "category=" + category +
                    "amount=" + amount +
                    ", percentage=" + percentage +
                    '}';
        }
    }
    public class SpendingPeriodInfo
    {
        public String period;
        public long periodAmount;

        public SpendingPeriodInfo(String period, long periodAmount)
        {
            this.period = period;
            this.periodAmount = periodAmount;
        }
    }

    private Comparator<SpendingAmountInfo> spendingAmountInfoComparator;
    private Comparator<SpendingPeriodInfo> spendingPeriodInfoComparator;

    public VisualizeViewModel(@NonNull Application application) {
        super(application);
        //Descending order
        spendingAmountInfoComparator = new Comparator<SpendingAmountInfo>() {
            @Override
            public int compare(SpendingAmountInfo spendingAmountInfo, SpendingAmountInfo t1) {
                if (spendingAmountInfo.amount < t1.amount) return 1;
                if (spendingAmountInfo.amount > t1.amount) return -1;
                return 0;
            }
        };


        spendingPeriodInfoComparator = new Comparator<SpendingPeriodInfo>() {
            @Override
            public int compare(SpendingPeriodInfo spendingPeriodInfo, SpendingPeriodInfo t1) {
                long timeR0 = DateTimeUtils.getDateInMillis(spendingPeriodInfo.period.substring(0,10)),
                        timeR1 = DateTimeUtils.getDateInMillis(t1.period.substring(0,10));
                if (timeR0 > timeR1) return 1;
                if (timeR1 > timeR0) return -1;
                return 0;
            }
        };
    }

    public HashMap<String, Long> getDailySpendingAmount(List<Spending> spendings)
    {
        HashMap<String, Long> returnResult = new HashMap<>();
        ArrayList<String> days = new ArrayList<>();

        //get all dates available
        for (Spending spending: spendings)
        {
            String date = DateTimeUtils.getDate(spending.getDate());
            if (!days.contains(date))
                days.add(date);
        }

        //retrieve value for each day, then put into the map
        for (String day: days)
        {
            long val = 0l;
            for (Spending spending: spendings)
                if (DateTimeUtils.getDate(spending.getDate()).equals(day))
                    val += spending.getCost();
            returnResult.put(day, val);
        }
        return returnResult;
    }


    @SuppressLint("NewApi")
    public ArrayList<SpendingAmountInfo>
    getSpendingProportionByCategory(List<Spending> spendings)
    {

        ArrayList<SpendingAmountInfo> returnResult = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        double sum = 0;
        //get all categories and the total amount spent
        for (Spending spending: spendings)
        {
            Category cat = CategoriesUtils.findCategoryById(spending.getCategory());
            if (!categories.contains(cat))
                categories.add(cat);
            sum += spending.getCost();
        }
        //retrieve the amount and put into the hash map
        for (Category category: categories)
        {
            double val = 0;
            for (Spending spending: spendings)
                if (category.toString().equals(spending.getCategory()))
                    val += spending.getCost();
            returnResult.add(new SpendingAmountInfo(category, (long)val, val / sum));
        }
        returnResult.sort(spendingAmountInfoComparator);
        Log.i("@@@ result", returnResult.toString());
        return returnResult;
    }

    public long getTotalSpending(@NonNull List<Spending> spendings)
    {
        long sum = 0L;
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum;
    }

   
    @SuppressLint("NewApi")
    public ArrayList<SpendingPeriodInfo>
    getGroupedSpendingAmount(List<Spending> spendings, int filterType)
    {
        ArrayList<SpendingPeriodInfo> returnResult = new ArrayList<>();

        String today = DateTimeUtils.getDate(DateTimeUtils.getCurrentTimeMillis());

        //limit = for intervals, cap = for the whole time period
        long upperLimit = DateTimeUtils.getDateInMillis(today),
                lowerLimit, upperCap = upperLimit, lowerCap;
        String beginning;

        int intervals = 0;
        long intervalDuration = 0l;
        switch (filterType)
        {
            case FILTER_WEEKLY:
                intervals = 7;
                intervalDuration = 24l * 60l * 60l * 1000l;
                lowerCap = upperCap - (long)intervals * intervalDuration;
                break;
            case FILTER_MONTHLY:
                intervals = 5;
                intervalDuration = 6l * (24l * 60l * 60l * 1000l);
                beginning = "01" + today.substring(2);
                lowerCap = DateTimeUtils.getDateInMillis(beginning);
                break;
            case FILTER_ANNUALLY:
                intervals = 12;
                intervalDuration = 30l * (24l * 60l * 60l * 1000l);
                beginning = "01/01" + today.substring(5);
                lowerCap = DateTimeUtils.getDateInMillis(beginning);
                break;
            default:
                return null;
        }
        for (int interval = 0; interval < intervals; interval++)
        {
            long cost = 0l;
            lowerLimit = upperLimit - intervalDuration;
            if (lowerLimit < lowerCap) lowerLimit = lowerCap;
            String lowerDate = DateTimeUtils.getDate(lowerLimit);
            String upperDate = DateTimeUtils.getDate(upperLimit);
            for (Spending spending: spendings)
            {
                long spendingDateMillis = spending.getDate();
                if (spendingDateMillis >= lowerLimit && spendingDateMillis < upperLimit)
                    cost += spending.getCost();
            }
            returnResult.add(
                    new SpendingPeriodInfo(
                            lowerDate + " - " + upperDate, cost));
            upperLimit = lowerLimit;
            if (upperLimit <= lowerCap) break;
        }
        returnResult.sort(spendingPeriodInfoComparator);
        if (filterType == FILTER_WEEKLY)
            for (SpendingPeriodInfo spendingPeriodInfo : returnResult)
                spendingPeriodInfo.period = spendingPeriodInfo.period.substring(0,5);
        else    //xx/yy/zzzz - xx/yy/zzzz
            for (SpendingPeriodInfo spendingPeriodInfo : returnResult)
                spendingPeriodInfo.period = spendingPeriodInfo.period.substring(0,5) +
                                            spendingPeriodInfo.period.substring(13, 18);
        return returnResult;
    }
}
