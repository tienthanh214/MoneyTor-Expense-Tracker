package com.hcmus.group14.moneytor.data.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hcmus.group14.moneytor.data.local.dao.*;
import com.hcmus.group14.moneytor.data.model.*;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.List;

public class AppRepository {
    // all dao
    final private SpendingDao spendingDao;
    final private ReminderDao reminderDao;
    final private SpendGoalDao spendGoalDao;
    final private DebtLendDao debtLendDao;

    //Data repositories
    private LiveData<List<SpendingWithRelates>> allSpending;
    private LiveData<List<Reminder>> allReminders;
    private LiveData<List<SpendGoal>> allSpendGoals;
    private LiveData<List<DebtLend>> allDebtLends;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        // get all DAO
        spendingDao = db.spendingDao();
        reminderDao = db.reminderDao();
        spendGoalDao = db.spendGoalDao();
        debtLendDao = db.debtLendDao();

        allSpending = spendingDao.getAllSpendingWithRelates();
        allReminders = reminderDao.getAllReminders();
        allSpendGoals = spendGoalDao.getAllSpendGoals();
        allDebtLends = debtLendDao.getAllDebtLends();
    }

    // -------------- spending note --------------
    public LiveData<List<SpendingWithRelates>> getAllSpending() {
        return allSpending;
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

    // TODO: other entities
    //---------------------Reminders-----------------------
    public LiveData<List<Reminder>> getAllReminders()
    {
        return allReminders;
    }
    public void insertReminder(Reminder reminder)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(()->reminderDao.insert(reminder));
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
    public LiveData<List<DebtLend>> getAllDebtLends()
    {
        return allDebtLends;
    }
    public void insertDebtLend(DebtLend debtLend)
    {
        AppRoomDatabase.databaseWriteExecutor.execute(()->debtLendDao.insert(debtLend));
    }
}
