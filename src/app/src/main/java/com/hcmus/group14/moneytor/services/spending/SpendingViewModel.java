package com.hcmus.group14.moneytor.services.spending;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.group14.moneytor.data.local.AppRepository;
import com.hcmus.group14.moneytor.data.model.Spending;

public class SpendingViewModel extends AndroidViewModel {
    final private AppRepository repository;

    public SpendingViewModel(@NonNull Application app) {
        super(app);
        repository = new AppRepository(app);
    }

    public void insertSpending(Spending spending) {
        repository.insertSpending(spending);
    }
}
