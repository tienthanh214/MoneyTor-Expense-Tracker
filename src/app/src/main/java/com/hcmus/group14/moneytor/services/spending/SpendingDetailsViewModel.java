package com.hcmus.group14.moneytor.services.spending;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

public class SpendingDetailsViewModel extends AppViewModel {
    public MutableLiveData<Spending> spending;
    final private Spending _spending;

    public SpendingDetailsViewModel(@NonNull Application application) {
        super(application);
        _spending = new Spending();
    }

    public SpendingDetailsViewModel(@NonNull Application application, int spendingId) {
        super(application);
        SpendingWithRelates[] result = appRepository.getSpendingWithRelatesById(spendingId);
        if (result.length > 0) {
            _spending = result[0].spending;
        } else {
            _spending = new Spending();
        }
    }

    public String getTitle() {
        return _spending.getTitle();
    }

    public String getDescription() {
        return _spending.getDescription();
    }

    public String getCost() {
        return String.valueOf(_spending.getCost());
    }

    public String getDate() {
        return DateTimeUtils.getDate(_spending.getDate());
    }

    public String getCategory() {
        return _spending.getCategory();
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

    public void setCategory(String category) {
        if (!category.equals(_spending.getCategory())) {
            _spending.setCategory(category);
            spending.setValue(_spending);
        }
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
        appRepository.insertSpending(_spending);
        return errors;
    }

    public void deleteSpending() {
        appRepository.deleteSpending(_spending);
    }

}
