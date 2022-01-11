package com.hcmus.group14.moneytor.services.debtlend;

import android.app.Application;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.List;

public class DebtLendDetailsViewModel extends AppViewModel {
    private DebtLend _debtLend;

    private final MutableLiveData<Integer> category;
    private final MutableLiveData<String> value;
    private final MutableLiveData<String> target;
    private final MutableLiveData<Boolean> debt; //assert(debt==0||debt==1);
    private final MutableLiveData<String> date;
    private final MutableLiveData<String> desc;


    private Relate oldRelate;
    private Relate newRelate;

    public DebtLendDetailsViewModel(@NonNull Application application) {
        super(application);
        desc = new MutableLiveData<>("");
        value = new MutableLiveData<>("");
        category = new MutableLiveData<>();
        date = new MutableLiveData<>(DateTimeUtils.getDate(-1));
        target = new MutableLiveData<>("");
        debt = new MutableLiveData<>(true);
    }

    public LiveData<List<DebtLendAndRelate>> getDebtLendAndRelateById(int id) {
        return appRepository.getDebtLendAndRelateById(id);
    }

    public void afterTextChanged(Editable s) {
        value.setValue(InputUtils.getCurrencyFormat(s));
    }

    public void uploadData(List<DebtLendAndRelate> debtLendAndRelateList) {
        if (debtLendAndRelateList.size() > 0) {
            _debtLend = debtLendAndRelateList.get(0).debtLend;
            oldRelate = debtLendAndRelateList.get(0).relate;
            setDate(_debtLend.getDate());
            setCategory(_debtLend.getCategory());
            setDesc(_debtLend.getDesc());
            setValue(_debtLend.getValue());
            setTarget(oldRelate);
            setDebt(_debtLend.getDebt() != 0);
        }
    }

    //gets
    public MutableLiveData<String> getDesc() {
        return desc;
    }

    public MutableLiveData<String> getValue() {
        return value;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public MutableLiveData<Integer> getCategory() {
        return category;
    }

    public MutableLiveData<String> getTarget() {
        return target;
    }

    public MutableLiveData<Boolean> getDebt() {
        return debt;
    }

    //sets
    public void setCategory(@Nullable String pCategory) {
        int position = CategoriesUtils.findPositionById(pCategory);
        category.setValue(position);
    }

    public void setValue(long pValue) {value.setValue(InputUtils.getCurrencyFormat(pValue));
    }

    public void setDebt(boolean pDebt) {
        debt.setValue(pDebt);
    }

    public void setDate(long pDate) {
        date.setValue(DateTimeUtils.getDate(pDate));
    }

    public void setTarget(Relate pTarget) {
        target.setValue(pTarget.getName());
        newRelate = pTarget;
    }

    public void setDesc(@Nullable String pDesc) {
        desc.setValue(pDesc);
    }

    void updateData() {
        if (_debtLend == null)
            _debtLend = new DebtLend();
        _debtLend.setCategory(CategoriesUtils.getCategoryIdByPosition(category.getValue()));
        _debtLend.setDesc(desc.getValue());
        _debtLend.setDate(DateTimeUtils.getDateInMillis(date.getValue()));
        // _debtLend.setDebt(debt.getValue());
        Boolean pDebt = debt.getValue();
        if (pDebt == null || pDebt)
            _debtLend.setDebt(1);
        else _debtLend.setDebt(0);
        if (newRelate == null)
        {
            if (oldRelate != null)
                _debtLend.setTarget(oldRelate.getRelateId());
            else
                _debtLend.setTarget(-1);
        }
        else
            _debtLend.setTarget(newRelate.getRelateId());


        if (value.getValue() != null && ! value.getValue().isEmpty()) {
            _debtLend.setValue(InputUtils.getCurrencyInLong(value.getValue()));
        } else {
            _debtLend.setValue(-1);
        }
    }

    public InputUtils saveDebtLend()
    {
        updateData();

        InputUtils errors = new InputUtils();
        if (_debtLend.getCategory() == null || _debtLend.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_debtLend.getValue() == -1)
            errors.setError(InputUtils.Type.COST);
        if (_debtLend.getTarget() < 0)
            errors.setError((InputUtils.Type.DEBT_LEND_TARGET));
        if (errors.hasError())
            return errors;
        
        
        if (_debtLend.getDate() == -1) {
            _debtLend.setDate(DateTimeUtils.getCurrentTimeMillis());
        }

        if (_debtLend.getRecordId() == 0) {
            appRepository.insertDebtLendWithTarget(_debtLend, newRelate);
        }
        else {
            appRepository.updateDebtLendWithTarget(_debtLend, newRelate);
        }
        return errors;
    }
    public void deleteDebtLend()
    {
        if (_debtLend != null && _debtLend.getRecordId() != 0)
            appRepository.deleteDebtLend(_debtLend);
    }

}
