package com.hcmus.group14.moneytor.services.debtlend;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;

import java.util.List;

public class DebtLendViewModel extends AppViewModel {
    private LiveData<List<DebtLendAndRelate>> allDebtLends;

    public DebtLendViewModel(@NonNull Application application) {
        super(application);
        allDebtLends = appRepository.getAllDebtLendAndRelate();
    }

    public LiveData<List<DebtLendAndRelate>> getAllDebtLends()
    {
        return allDebtLends;
    }
    public void insertDebtLend(DebtLend debtLend)
    {
       appRepository.insertDebtLend(debtLend);
       allDebtLends = appRepository.getAllDebtLendAndRelate();
    }
}
