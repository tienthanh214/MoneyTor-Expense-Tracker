package com.hcmus.group14.moneytor.services.options;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;

import java.util.List;

public class FilterViewModel extends AppViewModel {
    private final MutableLiveData<FilterState> filterState;
    private LiveData<List<Spending>> allSpending = null;
    private LiveData<List<DebtLendAndRelate>> allDebtLend = null;
    private LiveData<List<SpendGoal>> allSpendGoal = null;
    private FilterState _filterState;
    public FilterViewModel(@NonNull Application application) {
        super(application);
        filterState = new MutableLiveData<>();
    }

    public void setFilterState(FilterState filterState) {
        this.filterState.postValue(filterState);
        this._filterState = filterState;
    }

    public FilterState getFilterState() {
        return this._filterState;
    }

    public LiveData<List<Spending>> getAllSpending() {
        if (allSpending == null) {
            allSpending = Transformations.switchMap(filterState, input -> {
                return appRepository.filterSpendingByCategoryAndTime(input.categories, input.startDate, input.endDate);
            });
        }
        return allSpending;
    }

    public LiveData<List<DebtLendAndRelate>> getAllDebtLend() {
        if (allDebtLend == null) {
            allDebtLend = Transformations.switchMap(filterState, input -> {
                return appRepository.filterDebtLendByCategoryAndTime(input.categories, input.startDate, input.endDate);
            });
        }
        return allDebtLend;
    }

    public LiveData<List<SpendGoal>> getAllSpendGoal() {
        if (allSpendGoal == null) {
            allSpendGoal = Transformations.switchMap(filterState, input -> {
                return appRepository.filterSpendGoalByCategoryAndTime(input.categories, input.startDate, input.endDate);
            });
        }
        return allSpendGoal;
    }
}
