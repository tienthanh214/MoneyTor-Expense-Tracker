package com.hcmus.group14.moneytor.services.debtlend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.model.DebtLend;

public class DebtLendDetailsViewModel extends AndroidViewModel {
    private MutableLiveData<DebtLend> debtLend;
    private DebtLend _debtLend;

    public DebtLendDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    //gets
    public int getRecordId() {
        return _debtLend.getRecordId();
    }

    public @Nullable
    String getCategory() {
        return _debtLend.getCategory();
    }

    public int getValue() {
        return _debtLend.getValue();
    }

    public int getDebt() {
        return _debtLend.getDebt();
    }

    public long getDate() {
        return _debtLend.getDate();
    }

    public int getTarget() {
        return _debtLend.getTarget();
    }

    public @Nullable
    String getDesc() {
        return _debtLend.getDesc();
    }

    //sets
    public void setCategory(@Nullable String category) {
        _debtLend.setCategory(category);
        debtLend.setValue(_debtLend);
    }

    public void setValue(int value) {
        _debtLend.setValue(value);
        debtLend.setValue(_debtLend);
    }

    public void setDebt(int debt) {
        _debtLend.setDebt(debt);
        debtLend.setValue(_debtLend);
    }

    public void setDate(long date) {
        _debtLend.setDate(date);
        debtLend.setValue(_debtLend);
    }

    public void setTarget(int target) {
        _debtLend.setTarget(target);
        debtLend.setValue(_debtLend);
    }

    public void setDesc(@Nullable String desc) {
        _debtLend.setDesc(desc);
        debtLend.setValue(_debtLend);
    }

    public void insertDebtLend(@NonNull DebtLendViewModel debtLendViewModel)
    {
        debtLendViewModel.insertDebtLend(debtLend.getValue());
    }
}