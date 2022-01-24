package com.hcmus.group14.moneytor.services.visualize;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


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
        long upperLimit = DateTimeUtils.getDateInMillis(today) + 24l * 3600000l,
                lowerLimit, upperCap = upperLimit, lowerCap;
        String beginning;

        int intervals = 0;
        long intervalDuration = 0l;
        switch (filterType)
        {
            case FILTER_DAILY:
                intervals = 1;
                intervalDuration = 24l * 60l * 60l * 1000l;
                lowerCap = upperCap - intervalDuration;
                break;
            case FILTER_WEEKLY:
                intervals = 7;
                intervalDuration = 24l * 60l * 60l * 1000l;
                lowerCap = upperCap - (long)intervals * intervalDuration;
                break;
            case FILTER_MONTHLY:
                intervals = 5;
                intervalDuration = 7l * (24l * 60l * 60l * 1000l);
                beginning = "01" + today.substring(2);
                lowerCap = DateTimeUtils.getDateInMillis(beginning);
                break;
            case FILTER_ANNUALLY:
                beginning = "01/01" + today.substring(5);
                lowerCap = DateTimeUtils.getDateInMillis(beginning);
                break;
            default:
                return null;
        }
        if (filterType == FILTER_ANNUALLY)
        {
            //To get the leap year
            String theFirstOfMarch = "01/03" + today.substring(5);
            long beginningOfMarch = DateTimeUtils.getDateInMillis(theFirstOfMarch);

            //First, loop through months, for Feb, check if there is a day missing (leap year). If yes,
            //one more day is added to the interval duration.
            lowerLimit = lowerCap;
            for (int i = 1; i <= 12; i++)
            {
                switch (i)
                {
                    case 2:
                        intervalDuration = 28l * 3600000l * 24l;
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        intervalDuration = 30l * 3600000l * 24l;
                        break;
                    default:
                        intervalDuration = 31l * 3600000l * 24l;
                }
                upperLimit = lowerLimit + intervalDuration;
                if (i == 2 && upperLimit < beginningOfMarch) upperLimit += 3600000l * 24l;
                //Check if upper limit exceeds the current date, changes accordingly.
                if (upperLimit > upperCap) upperLimit = upperCap;
                //Loop through the spendings to check if the date created falls between the limits.
                long cost = 0l;
                for (Spending spending: spendings)
                {
                    long spendingDateMillis = spending.getDate();
                    if (spendingDateMillis >= lowerLimit && spendingDateMillis < upperLimit)
                        cost += spending.getCost();
                }
                String label = "";
                switch (i)
                {
                    case 1:
                        label = getApplication().getString(R.string.january);
                        break;
                    case 2:
                        label = getApplication().getString(R.string.february);
                        break;
                    case 3:
                        label = getApplication().getString(R.string.march);
                        break;
                    case 4:
                        label = getApplication().getString(R.string.april);
                        break;
                    case 5:
                        label = getApplication().getString(R.string.may);
                        break;
                    case 6:
                        label = getApplication().getString(R.string.june);
                        break;
                    case 7:
                        label = getApplication().getString(R.string.july);
                        break;
                    case 8:
                        label = getApplication().getString(R.string.august);
                        break;
                    case 9:
                        label = getApplication().getString(R.string.september);
                        break;
                    case 10:
                        label = getApplication().getString(R.string.october);
                        break;
                    case 11:
                        label = getApplication().getString(R.string.november);
                        break;
                    default:
                        label = getApplication().getString(R.string.december);
                        break;
                }
                returnResult.add(new SpendingPeriodInfo(label, cost));
                lowerLimit = upperLimit;
                if (lowerLimit >= upperCap) break;
            }
        }
        else {
            for (int interval = 0; interval < intervals; interval++) {
                long cost = 0L;
                lowerLimit = upperLimit - intervalDuration;
                if (lowerLimit < lowerCap) lowerLimit = lowerCap;
                String lowerDate = DateTimeUtils.getDate(lowerLimit);
                String upperDate = DateTimeUtils.getDate(upperLimit - 1l);
                for (Spending spending : spendings) {
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
        }

        String dateMonthFormat = getApplication().getString(R.string.date_time_format);
        if (filterType == FILTER_WEEKLY || filterType == FILTER_DAILY)
            for (SpendingPeriodInfo spendingPeriodInfo : returnResult)
            {
                String[] split = spendingPeriodInfo.period.split("\\/");
                spendingPeriodInfo.period = dateMonthFormat.replace("x", split[0]).
                        substring(0,3);

            }
        else if (filterType == FILTER_MONTHLY)   //2021年12月31日 - 2021年12月31日
            for (SpendingPeriodInfo spendingPeriodInfo : returnResult)
            {
                String[] dates = spendingPeriodInfo.period.split(" \\- ");
                String[] split0 = dates[0].split("\\/"),
                        split1 = dates[1].split("\\/");
                spendingPeriodInfo.period = dateMonthFormat.replace("x", split0[0]).
                        replace("y", split0[1]) + " - "
                        + dateMonthFormat.replace("x", split1[0]).
                        replace("y", split1[1]);
            }

        for (SpendingPeriodInfo spendingPeriodInfo : returnResult)
        {
           System.out.println(spendingPeriodInfo.period);
        }
        return returnResult;
    }

    public int getFilterTypeId(int filterType) {
        switch (filterType) {
            case FILTER_WEEKLY:
                return R.string.weekly_summary;
            case FILTER_MONTHLY:
                return R.string.monthly_summary;
            case FILTER_ANNUALLY:
                return R.string.annually_summary;
            default:
                return R.string.daily_summary;
        }
    }
}
