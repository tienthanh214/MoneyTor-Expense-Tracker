package com.hcmus.group14.moneytor.services.spending;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Spending;

import java.util.List;

public class SpendingViewModel extends AppViewModel {
    final private LiveData<List<Spending>> allSpending;

    public SpendingViewModel(@NonNull Application app) {
        super(app);
        this.allSpending = appRepository.getAllSpending();
    }

    public LiveData<List<Spending>> getAllSpending() {
        return this.allSpending;
    }
    // insert a simple spending
    public void insertSpending(Spending spending) {
        appRepository.insertSpending(spending);
    }
    // insert a spending with list of relate share bill
    public void insertSpendingWithRelates(Spending spending, List<Relate> relates) {
        appRepository.insertSpendingWithRelates(spending, relates);
    }
    // delete a spending with all relates to it
    public void deleteSpending(Spending spending) {
        appRepository.deleteSpending(spending);
    }

    public void updateSpending(Spending spending) {
        appRepository.updateSpending(spending);
    }

    public void updateSpendingWithRelates(Spending spending, List<Relate> ols, List<Relate> news) {
        appRepository.updateSpendingWithRelates(spending, ols, news);
    }

    public void getSpendingWithRelatesById(int id) {
        appRepository.getSpendingWithRelatesById(id);
    }
}
