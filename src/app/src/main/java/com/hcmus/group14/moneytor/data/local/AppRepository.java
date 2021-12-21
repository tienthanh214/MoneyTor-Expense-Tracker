package com.hcmus.group14.moneytor.data.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hcmus.group14.moneytor.data.local.dao.*;
import com.hcmus.group14.moneytor.data.model.*;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.List;

public class AppRepository {
    // all dao
    final private SpendingDao spendingDao;
    final private ReminderDao reminderDao;
    final private SpendGoalDao spendGoalDao;
    final private DebtLendDao debtLendDao;
    final private RelateDao relateDao;
    final private WalletDao walletDao;
    //Data repositories
    private LiveData<List<Spending>> allSpending;
    private LiveData<List<Reminder>> allReminders;
    private LiveData<List<SpendGoal>> allSpendGoals;
    private LiveData<List<DebtLend>> allDebtLends;
    private LiveData<List<Wallet>> allWallets;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        // get all DAO
        spendingDao = db.spendingDao();
        reminderDao = db.reminderDao();
        spendGoalDao = db.spendGoalDao();
        debtLendDao = db.debtLendDao();
        relateDao = db.relateDao();
        walletDao = db.walletDao();

        allSpending = spendingDao.getAllSpending();
        allReminders = reminderDao.getAllReminders();
        allSpendGoals = spendGoalDao.getAllSpendGoals();
        allDebtLends = debtLendDao.getAllDebtLends();
        allWallets = walletDao.getAllWallets();
    }

    // -------------- spending note --------------
    public LiveData<List<SpendingWithRelates>> getSpendingWithRelatesById(int id) {
        return spendingDao.getSpendingWithRelatesById(id);
    }

    public LiveData<List<Spending>> getAllSpending() {
        return allSpending;
    }

    public List<Spending> getSpendingByCategories(List<String> cats)
    {
        return spendingDao.getSpendingByCategories(cats);
    }

    public void insertSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertSpending(spending);
        });
    }

    public void insertAllSpendingRelateCrossRef(List<SpendingRelateCrossRef> crossRefList) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertAllSpendingRelateCrossRef(crossRefList);
        });
    }


    public void insertSpendingWithRelates(Spending spending, List<Relate> relates) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertSpendingWithRelates(spending, relates);
        });
    }
    // delete a spending with all relates
    public void deleteSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.deleteSpendingWithRelatesById(spending.getSpendingId());
        });
    }

    public void updateSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.updateSpending(spending);
        });
    }

    public LiveData<List<Spending>> filterByCategoryAndTime(List<String> cats, long startDate, long endDate) {
        if (startDate > endDate) {
            long temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        if ((cats == null || cats.isEmpty()) && endDate == -1) // wrong filter
            return spendingDao.getAllSpending();
        if ((cats != null && !cats.isEmpty()) && endDate != -1) // valid filter
            return spendingDao.filterByCategoryAndTime(cats, startDate, endDate);
        else if (endDate != -1)  // if only time valid
            return spendingDao.filterByTime(startDate, endDate);
        else  // if only cats valid
            return spendingDao.filterByCategories(cats);
    }
    // update spending with new relates, remove old relates
    public void updateSpendingWithRelates(Spending spending, List<Relate> olds, List<Relate> news) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.updateSpendingWithRelates(spending, olds, news);
        });
    }

    // -------------- wallet --------------
    public LiveData<List<Wallet>> getAllWallets() {
        return allWallets;
    }
    public LiveData<List<Wallet>> getWalletByProvider(String provider) {
        return walletDao.getWalletByProvider(provider);
    }

    public void insertWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.insertWallet(wallet);
        });
    }

    public void deleteWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.deleteWallet(wallet);
        });
    }

    public void updateWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.updateWallet((wallet));
        });
    }

    // -------------- Relate (share bill, debt target) --------------
    public Relate getRelateById(int id)
    {
        return relateDao.getRelateById(id)[0];
    }
    public void insertRelate(Relate relate) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            relateDao.insertRelate(relate);
        });
    }

    //---------------------Reminders-----------------------
    public LiveData<List<Reminder>> getAllReminders()
    {
        return allReminders;
    }
    public void insertReminder(Reminder reminder)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(()->reminderDao.insert(reminder));
    }
    public Reminder[] getReminderById(int id)
    {
        return reminderDao.getReminderByID(id);
    }
    //---------------------Spending goals------------------
    public LiveData<List<SpendGoal>> getAllSpendGoals()
    {
        return allSpendGoals;
    }
    public void insertSpendGoal(SpendGoal spendGoal)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(()->spendGoalDao.insert(spendGoal));
    }
    public void deleteSpendGoal(SpendGoal spendGoal) {
        AppRoomDatabase.databaseWriteExecutor.execute(()->spendGoalDao.deleteSpendGoal(spendGoal));
    }
    public void updateSpendGoal(SpendGoal spendGoal) {
        AppRoomDatabase.databaseWriteExecutor.execute(()->spendGoalDao.update(spendGoal));
    }
    //---------------------Spending goals------------------
    public LiveData<List<SpendGoal>> getSpendGoalById(int id)
    {
        return spendGoalDao.getSpendGoalByID(id);
    }
    //---------------------Debt/lends----------------------
    public LiveData<List<DebtLend>> getAllDebtLends()
    {
        return allDebtLends;
    }
    public void insertDebtLend(DebtLend debtLend)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(()->debtLendDao.insert(debtLend));
    }
    public DebtLend[] getDebtLendById(int id)
    {
        return debtLendDao.getDebtLendByID(id);
    }
    public List<DebtLendAndRelate> getAllDebtLendAndRelate()
    {
        return debtLendDao.getAllDebtLendAndRelate();
    }
    public DebtLendAndRelate getDebtLendAndRelateById(int id)
    {
        return debtLendDao.getDebtLendAndRelateById(id).get(0);
    }
}
