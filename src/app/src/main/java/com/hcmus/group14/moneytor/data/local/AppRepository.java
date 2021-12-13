package com.hcmus.group14.moneytor.data.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hcmus.group14.moneytor.data.local.dao.SpendingDao;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.List;
import java.util.concurrent.Future;

public class AppRepository {
    final private SpendingDao spendingDao;
    private LiveData<List<SpendingWithRelates>> allSpending;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        // get all DAO
        spendingDao = db.spendingDao();
        allSpending = spendingDao.getSpendingWithRelates();

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
}
