package com.hcmus.group14.moneytor.data.local;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.hcmus.group14.moneytor.data.model.*;
import com.hcmus.group14.moneytor.data.model.relation.*;
import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<SpendingWithRelates>> allSpending;
    private LiveData<List<Reminder>> allReminders;
    private LiveData<List<SpendGoal>> allSpendGoals;
    private LiveData<List<DebtLend>> allDebtLends;


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);

        allSpending = appRepository.getAllSpending();
        allReminders = appRepository.getAllReminders();
        allSpendGoals = appRepository.getAllSpendGoals();
        allDebtLends = appRepository.getAllDebtLends();
    }

    //---------------------Reminders-----------------------
    public LiveData<List<Reminder>> getAllReminders()
    {
        return allReminders;
    }
    public void insertReminder(Reminder reminder)
    {
        appRepository.insertReminder(reminder);
    }
    //---------------------Spending goals------------------
    public LiveData<List<SpendGoal>> getAllSpendGoals()
    {
        return allSpendGoals;
    }
    public void insertSpendGoal(SpendGoal spendGoal)
    {
        appRepository.insertSpendGoal(spendGoal);
    }
    public LiveData<List<DebtLend>> getAllDebtLends()
    {
        return allDebtLends;
    }
    public void insertDebtLend(DebtLend debtLend)
    {
       appRepository.insertDebtLend(debtLend);
    }
}
