package com.hcmus.group14.moneytor.services.spending;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

public class SpendingDetailsViewModel extends AppViewModel {
    public MutableLiveData<Spending> spending;
    private Spending _spending;

    public SpendingDetailsViewModel(@NonNull Application application) {
        super(application);
        _spending = new Spending();
        spending = new MutableLiveData<>(_spending);
    }

//    public SpendingDetailsViewModel(@NonNull Application application, int spendingId) {
//        super(application);
//        setSpendingData(spendingId);
//    }

    public void setSpendingData(int spendingId) {
        SpendingWithRelates[] result = appRepository.getSpendingWithRelatesById(spendingId);
        if (result.length > 0) {
            _spending = result[0].spending;
        } else {
            _spending = new Spending();
        }
        spending = new MutableLiveData<>(_spending);
    }

    public String getTitle() {
        return _spending.getTitle();
    }

    public String getDescription() {
        return _spending.getDescription();
    }

    public String getCost() {
        Log.i("@@@ getCost", "" + _spending.getCost());
        if (_spending.getCost() < 0)
            return "0";
        return String.valueOf(_spending.getCost());
    }

    public String getDate() {
        if (_spending.getDate() == -1)
            return DateTimeUtils.getDate(DateTimeUtils.getCurrentTimeMillis());
        return DateTimeUtils.getDate(_spending.getDate());
    }

    public int getCategory() {
        int position = CategoriesUtils.findPositionById(_spending.getCategory());
        if (position != -1)
            return position;
        return -1;
    }

    public void setTitle(String title) {
        if (!title.equals(_spending.getTitle())) {
            _spending.setTitle(title);
            spending.setValue(_spending);
        }
    }

    public void setDescription(String description) {
        if (!description.equals(_spending.getDescription())) {
            _spending.setDescription(description);
            spending.setValue(_spending);
        }
    }

    public void setCost(String cost) {
        long value = -1;
        if (!cost.isEmpty())
            value = Long.parseLong(cost);
        if (value != _spending.getCost()) {
            _spending.setCost(value);
            spending.setValue(_spending);
        }
    }

    public void setCategory(int position) {
        if (position != CategoriesUtils.findPositionById(_spending.getCategory())) {
            _spending.setCategory(CategoriesUtils.getCategoryIdByPosition(position));
        }
        Log.i("@@@ cat", _spending.getCategory());
    }

    public void setDate(String date) {
        long millis = DateTimeUtils.getDateInMillis(date);
        if (millis != _spending.getDate()) {
            _spending.setDate(millis);
            spending.setValue(_spending);
        }
    }

    // save spending, if input invalid return all errors
    public InputUtils saveSpending() {
        Log.i("@@@ save", _spending.toString());
        InputUtils errors = new InputUtils();
        if (_spending.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_spending.getCost() == -1)
            errors.setError(InputUtils.Type.COST);
        if (errors.hasError())
            return errors;
        if (_spending.getTitle().isEmpty()) {
            _spending.setTitle(_spending.getCategory());
        }
        if (_spending.getDate() == -1) {
            _spending.setDate(DateTimeUtils.getCurrentTimeMillis());
        }
//        appRepository.insertSpending(_spending);
        return errors;
    }

    public void deleteSpending() {
        appRepository.deleteSpending(_spending);
    }

}
