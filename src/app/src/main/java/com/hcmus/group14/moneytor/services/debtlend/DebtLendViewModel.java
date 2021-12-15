package com.hcmus.group14.moneytor.services.debtlend;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import java.util.List;

public class DebtLendViewModel extends AppViewModel {
    private LiveData<List<DebtLend>> allDebtLends;

    public DebtLendViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<DebtLend>> getAllDebtLends()
    {
        return allDebtLends;
    }
    public void insertDebtLend(DebtLend debtLend)
    {
       appRepository.insertDebtLend(debtLend);
       allDebtLends = appRepository.getAllDebtLends();
    }
}
