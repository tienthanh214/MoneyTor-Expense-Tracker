package com.hcmus.group14.moneytor.services.goal;

import android.app.Application;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;
import com.hcmus.group14.moneytor.utils.NotificationUtils;

import java.sql.Timestamp;
import java.util.List;

public class SpendGoalDetailsViewModel extends AppViewModel {
    private final MutableLiveData<String> _amount;
    private final MutableLiveData<Integer> _category;
    private final MutableLiveData<String> _date;
    private final MutableLiveData<String> _description;

    private SpendGoal _goal;

    public SpendGoalDetailsViewModel(@NonNull Application application) {
        super(application);
        _amount = new MutableLiveData<>("");
        _date = new MutableLiveData<>(DateTimeUtils.getDate(-1));
        _description = new MutableLiveData<>("");
        _category = new MutableLiveData<>();
    }

    public LiveData<List<SpendGoal>> getSpendGoalById(int goalId) {
        return appRepository.getSpendGoalById(goalId);
    }

    public void uploadData(SpendGoal goal) {
        if (goal != null && _goal == null) {
            _goal = goal;
            setAmount(_goal.getSpendingCap());
            setDescription(_goal.getDesc());
            setDate(_goal.getDate());
            setCategory(_goal.getCategory());

        }
    }

    public MutableLiveData<String> getDescription() {
        return _description;
    }

    public void setDescription(String title) {
        _description.setValue(title);
    }

    public MutableLiveData<String> getAmount() {
        return _amount;
    }

    public void setAmount(long amount) {
        _amount.setValue(InputUtils.getCurrencyFormat(amount));
    }

    public MutableLiveData<String> getDate() {
        return _date;
    }

    public void setDate(long date) {
        _date.setValue(DateTimeUtils.getDate(date));
    }

    public MutableLiveData<Integer> getCategory() {
        return _category;
    }

    public void setCategory(String id) {
        int position = CategoriesUtils.findPositionById(id);
        if (position != -1)
            _category.setValue(position);
    }

    public void afterTextChanged(Editable s) {
        _amount.setValue(InputUtils.getCurrencyFormat(s));
    }

    void updateData() {
        if (_goal == null)
            _goal = new SpendGoal();
        _goal.setDesc(_description.getValue());
        _goal.setDate(DateTimeUtils.getDateInMillis(_date.getValue()));
        _goal.setCategory(CategoriesUtils.getCategoryIdByPosition(_category.getValue()));
        if (_amount.getValue() != null && !_amount.getValue().isEmpty()) {
            _goal.setSpendingCap(InputUtils.getCurrencyInLong(_amount.getValue()));
        } else {
            _goal.setSpendingCap(0);
        }
    }

    public InputUtils saveSpendGoal() {
        updateData();
        InputUtils errors = new InputUtils();
        if (_goal.getCategory().isEmpty())
            errors.setError(InputUtils.Type.CATEGORY);
        if (_goal.getSpendingCap() <= 0)
            errors.setError(InputUtils.Type.COST);

        if (errors.hasError())
            return errors;
        if (_goal.getGoalID() == 0) {
            Timestamp timestamp = new Timestamp(DateTimeUtils.getCurrentTimeMillis());
            _goal.setGoalID(timestamp.hashCode());
            appRepository.insertSpendGoal(_goal);
        } else {
            appRepository.updateSpendGoal(_goal);
//            NotificationUtils.cancelGoalNotif(getApplication().getApplicationContext(), _goal);
        }
//        setUpNotification();
        return errors;
    }

    public void deleteGoal() {
        if (_goal == null || _goal.getGoalID() == 0)
            return;
        appRepository.deleteSpendGoal(_goal);
        NotificationUtils.cancelGoalNotif(getApplication().getApplicationContext(), _goal);
    }

    private void setUpNotification() {
        NotificationUtils.scheduleGoalNotif(getApplication().getApplicationContext(), _goal);
    }
}
