package com.hcmus.group14.moneytor.services.goal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

public class SpendGoalDetailsViewModel extends AppViewModel {
    public MutableLiveData<SpendGoal> goal;
    private final SpendGoal _goal;

    public SpendGoalDetailsViewModel(@NonNull Application application) {
        super(application);
        _goal = new SpendGoal("", 0, DateTimeUtils.getCurrentTimeMillis(), "");
        goal = new MutableLiveData<>(_goal);
    }

    public String getDescription() {
        return _goal.getDesc();
    }

    public String getDate() {
        return DateTimeUtils.getDate(_goal.getDate());
    }

    public String getCategory() {
        return _goal.getCategory();
    }

    public String getAmount() {
        return String.valueOf(_goal.getSpendingCap());
    }

    public void setDescription(String desc) {
        if (!desc.equals(_goal.getDesc())) {
            _goal.setDesc(desc);
            goal.setValue(_goal);
        }
    }

    public void setDate(String date) {
        long millis = DateTimeUtils.getDateInMillis(date);
        if (millis != _goal.getDate()) {
            _goal.setDate(millis);
            goal.setValue(_goal);
        }
    }

    public void setCategory(String category) {

    }

    public void setAmount(String amount) {
        long value = -1;
        if (!amount.isEmpty())
            value = Long.parseLong(amount);
        if (value != _goal.getSpendingCap()) {
            _goal.setSpendingCap(value);
            goal.setValue(_goal);
        }
    }

    public InputUtils saveSpending() {
        InputUtils errors = new InputUtils();
        if (_goal.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_goal.getSpendingCap() <= 0)
            errors.setError(InputUtils.Type.COST);
        if (errors.hasError())
            return errors;
        appRepository.insertSpendGoal(_goal);
        setUpNotification();
        return errors;
    }

    public void deleteGoal() {
        appRepository.deleteSpendGoal(_goal);
    }

    private void setUpNotification() {
        // TODO: schedule notification here
    }
}
