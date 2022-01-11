package com.hcmus.group14.moneytor.services.spending;

import android.app.Application;
import android.text.Editable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.List;

public class SpendingDetailsViewModel extends AppViewModel {
    private final MutableLiveData<String> _title;
    private final MutableLiveData<String> _description;
    private final MutableLiveData<String> _cost;
    private final MutableLiveData<Integer> _category;
    private final MutableLiveData<String> _date;
    private final MutableLiveData<String> _relates;

    private Spending _spending;
    private List<Relate> _oldRelates;
    private List<Relate> _newRelates;

    public SpendingDetailsViewModel(@NonNull Application application) {
        super(application);
        _title = new MutableLiveData<>("");
        _description = new MutableLiveData<>("");
        _cost = new MutableLiveData<>("");
        _category = new MutableLiveData<>();
        _date = new MutableLiveData<>(DateTimeUtils.getDate(-1));
        _relates = new MutableLiveData<>("");
    }

    public LiveData<List<SpendingWithRelates>> getSpendingWithRelatesById(int spendingId) {
        return appRepository.getSpendingWithRelatesById(spendingId);
    }

    public void uploadData(List<SpendingWithRelates> spendingWithRelatesList) {
        if (spendingWithRelatesList.size() > 0) {
            _spending = spendingWithRelatesList.get(0).spending;
            _oldRelates = spendingWithRelatesList.get(0).relates;
            setTitle(_spending.getTitle());
            setDate(_spending.getDate());
            setCategory(_spending.getCategory());
            setDescription(_spending.getDescription());
            setCost(_spending.getCost());
            setRelates(_oldRelates);
        }
    }

    public MutableLiveData<String> getTitle() {
        return _title;
    }

    public MutableLiveData<String> getDescription() {
        return _description;
    }

    public MutableLiveData<String> getCost() {
        return _cost;
    }

    public MutableLiveData<String> getDate() {
        return _date;
    }

    public MutableLiveData<Integer> getCategory() {
        return _category;
    }

    public MutableLiveData<String> getRelates() {
        return _relates;
    }

    public void setTitle(String title) {
        _title.setValue(title);
    }

    public void setDescription(String description) {
        _description.setValue(description);
    }

    public void setCost(long cost) {
        _cost.setValue(InputUtils.getCurrencyFormat(cost));
    }

    public void setCategory(String id) {
        int position = CategoriesUtils.findPositionById(id);
        if (position != -1)
            _category.setValue(position);
    }

    public void setDate(long date) {
        _date.setValue(DateTimeUtils.getDate(date));
    }

    public void setRelates(List<Relate> relates) {
        // TODO : test relates
        this._newRelates = relates;
        StringBuilder str = new StringBuilder();
        if (relates != null) {
            for (int i = 0; i < relates.size(); ++i) {
                str.append(relates.get(i).getName());
                if (i != relates.size() - 1) {
                    str.append(", ");
                }
            }
        }
        _relates.setValue(str.toString());
    }

    public void afterTextChanged(Editable s) {
        _cost.postValue(InputUtils.getCurrencyFormat(s));
    }

    void updateData() {
        if (_spending == null)
            _spending = new Spending();
        _spending.setTitle(_title.getValue());
        _spending.setDescription(_description.getValue());
        _spending.setDate(DateTimeUtils.getDateInMillis(_date.getValue()));
        _spending.setCategory(CategoriesUtils.getCategoryIdByPosition(_category.getValue()));
        if (_cost.getValue() != null && !_cost.getValue().isEmpty()) {
            _spending.setCost(InputUtils.getCurrencyInLong(_cost.getValue()));
        } else {
            _spending.setCost(-1);
        }
    }
    // find old relates removed, new relates inserted
    void computeRelates() {
        if (_oldRelates == _newRelates) {
            // no change
            _oldRelates = _newRelates = null;
        }
    }

    // save spending, if input invalid return all errors
    public InputUtils saveSpending() {
        updateData();
        Log.i("@@@ spending", _spending.toString());
        InputUtils errors = new InputUtils();
        if (_spending.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_spending.getCost() < 0)
            errors.setError(InputUtils.Type.COST);
        if (errors.hasError())
            return errors;
        if (_spending.getTitle().isEmpty()) {
            _spending.setTitle(CategoriesUtils.getCategoryNameById(_spending.getCategory()));
        }
        if (_spending.getDate() == -1) {
            _spending.setDate(DateTimeUtils.getCurrentTimeMillis());
        }

        if (_spending.getSpendingId() == 0) { // insert new note
            appRepository.insertSpendingWithRelates(_spending, _newRelates);
        } else {   // update old note
            computeRelates(); // what relates should be removed, inserted
            appRepository.updateSpendingWithRelates(_spending, _oldRelates, _newRelates);
        }
        return errors;
    }

    public void deleteSpending() {
        if (_spending == null || _spending.getSpendingId() == 0)
            return; // if not a saved note then cancel
        appRepository.deleteSpending(_spending);
    }
}
