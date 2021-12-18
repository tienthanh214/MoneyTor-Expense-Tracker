package com.hcmus.group14.moneytor.services.debtlend;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;
import com.hcmus.group14.moneytor.data.model.DebtLend;

public class DebtLendDetailsViewModel extends AppViewModel {
    private MutableLiveData<DebtLend> debtLend;
    private DebtLend _debtLend;

    public DebtLendDetailsViewModel(@NonNull Application application) {
        super(application);
        _debtLend = new DebtLend();
        debtLend = new MutableLiveData<>(_debtLend);
    }
    public DebtLendDetailsViewModel(@NonNull Application application, int recordId)
    {
        super(application);
        DebtLend[] list = appRepository.getDebtLendById(recordId);
        _debtLend = list.length > 0? list[0] : new DebtLend();
        debtLend = new MutableLiveData<>(_debtLend);
    }

    //gets
    public @Nullable
    String getCategory() {
        return _debtLend.getCategory();
    }

    public String getValue() {
        return String.valueOf(_debtLend.getValue());
    }

    public String getDebt() {
        return _debtLend.getDebt() == 1 ? "Debt" : "Lend";
    }

    public String getDate() {
        if (_debtLend.getDate() == -1)
            return DateTimeUtils.getDate(DateTimeUtils.getCurrentTimeMillis());
        return DateTimeUtils.getDate(_debtLend.getDate());
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

    public void setValue(String value) {
        if (value.isEmpty()) return;
        if (_debtLend.getValue() == Integer.parseInt(value)) return;
        _debtLend.setValue(Integer.parseInt(value));
        debtLend.setValue(_debtLend);
    }

    public void setDebt(String debt) {
        if (_debtLend.getDebt() == Integer.parseInt(debt)) return;
        _debtLend.setDebt(Integer.parseInt(debt));
        debtLend.setValue(_debtLend);
    }

    public void setDate(String date) {
        long millis = DateTimeUtils.getDateInMillis(date);
        if (millis != _debtLend.getDate()) {
            _debtLend.setDate(millis);
            debtLend.setValue(_debtLend);
        }
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

    public InputUtils saveDebtLend()
    {
        Log.i("@@@ saved", "Debt/lend record");
        InputUtils errors = new InputUtils();
        if (_debtLend.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_debtLend.getValue() == -1)
            errors.setError(InputUtils.Type.COST);
        if (errors.hasError())
            return errors;
        if (_debtLend.getDate() == -1) {
            _debtLend.setDate(DateTimeUtils.getCurrentTimeMillis());
        }
        appRepository.insertDebtLend(debtLend.getValue());
        return errors;
    }
}
