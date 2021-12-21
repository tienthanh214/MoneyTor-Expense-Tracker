package com.hcmus.group14.moneytor.services.visualize;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.hcmus.group14.moneytor.data.local.dao.SpendingDao;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

//TODO
public class VisualizeViewModel extends AndroidViewModel {
    Comparator<String> stringComparator;
    public VisualizeViewModel(@NonNull Application application) {
        super(application);
        stringComparator = new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                if (s.charAt(0) >= t1.charAt(0)) return 1;
                return 0;
            }
        };
    }

    @SuppressLint("NewApi")
    public HashMap<String, Long> getDailySpendingAmount(List<Spending> spendings)
    {
        HashMap<String, Long> returnResult = new HashMap<>();
        ArrayList<String> days = new ArrayList<>();

        for (Spending spending: spendings)
        {
            String date = DateTimeUtils.getDate(spending.getDate());
            if (!days.contains(date))
                days.add(date);
        }
        days.sort(stringComparator);
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
}
