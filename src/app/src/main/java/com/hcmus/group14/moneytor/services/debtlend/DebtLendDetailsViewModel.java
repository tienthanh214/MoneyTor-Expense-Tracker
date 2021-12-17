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
        if (_debtLend.getCategory().equals(category)) return;
        _debtLend.setCategory(category);
        debtLend.setValue(_debtLend);
    }

    public void setValue(int value) {
        if (_debtLend.getValue() == value) return;
        _debtLend.setValue(value);
        debtLend.setValue(_debtLend);
    }

    public void setDebt(int debt) {
        if (_debtLend.getDebt() == debt) return;
        _debtLend.setDebt(debt);
        debtLend.setValue(_debtLend);
    }

    public void setDate(long date) {
        if (_debtLend.getDate() == date) return;
        _debtLend.setDate(date);
        debtLend.setValue(_debtLend);
    }

    public void setTarget(int target) {
        if (_debtLend.getTarget() == target) return;
        _debtLend.setTarget(target);
        debtLend.setValue(_debtLend);
    }

    public void setDesc(@Nullable String desc) {
        if (_debtLend.getDesc().equals(desc)) return;
        _debtLend.setDesc(desc);
        debtLend.setValue(_debtLend);
    }

    public void insertDebtLend(@NonNull DebtLendViewModel debtLendViewModel)
    {
        debtLendViewModel.insertDebtLend(debtLend.getValue());
    }
}
