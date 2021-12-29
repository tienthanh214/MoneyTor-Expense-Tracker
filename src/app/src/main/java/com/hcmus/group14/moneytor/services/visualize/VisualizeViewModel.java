package com.hcmus.group14.moneytor.services.visualize;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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


public class VisualizeViewModel extends AndroidViewModel {
    Comparator<String> stringComparator;
    Comparator<Category> categoryComparator;
    public class SpendingAmountInfo
    {
        public long amount;
        public double percentage;

        public SpendingAmountInfo(long amount, double percentage)
        {
            this.amount = amount;
            this.percentage = percentage;
        }

        public String toString() {
            return "SpendingAmountInfo{" +
                    "amount=" + amount +
                    ", percentage=" + percentage +
                    '}';
        }
    }
    public VisualizeViewModel(@NonNull Application application) {
        super(application);
        stringComparator = new Comparator<String>() {
            // t0 > t1 <=> t0[0] > t1[0]
            @Override
            public int compare(String s, String t1) {
                if (s.charAt(0) > t1.charAt(0)) return 1;   //positive if bigger
                if (s.charAt(0) < t1.charAt(0)) return -1;  //negative if smaller
                return 0;           //0 if equal
            }
        };
        categoryComparator = new Comparator<Category>() {
            // t0 > t1 <=> t0.ordinal() > t1.ordinal()
            @Override
            public int compare(Category category, Category t1) {
                if (category.ordinal() > t1.ordinal()) return 1;    //positive if bigger
                if (category.ordinal() < t1.ordinal()) return -1;   //negative if smaller
                return 0;       //0 if equal
            }
        };
    }

    @SuppressLint("NewApi")
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
        //sort days, ascending order
        days.sort(stringComparator);
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
    public HashMap<Category, SpendingAmountInfo>
    getSpendingProportionByCategory(List<Spending> spendings)
    {

        HashMap<Category, SpendingAmountInfo> returnResult = new HashMap<>();
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
        //sort categories
        categories.sort(categoryComparator);
        //retrieve the amount and put into the hash map
        for (Category category: categories)
        {
            double val = 0;
            for (Spending spending: spendings)
                if (category.toString().equals(spending.getCategory()))
                    val += spending.getCost();
            returnResult.put(category, new SpendingAmountInfo((long)val, val / sum));
        }
        Log.i("@@@ result", returnResult.toString());
        return returnResult;
    }

    public long getTotalSpending(List<Spending> spendings)
    {
        long sum = 0L;
        for (Spending spending: spendings)
            sum += spending.getCost();
        return sum;
    }
}
