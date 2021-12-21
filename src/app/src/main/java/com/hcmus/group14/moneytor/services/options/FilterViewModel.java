package com.hcmus.group14.moneytor.services.options;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.data.model.Spending;

import java.util.List;

public class FilterViewModel extends AppViewModel {
    private final MutableLiveData<FilterState> filterState;
    private final LiveData<List<Spending>> allSpending;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        filterState = new MutableLiveData<>();
        allSpending = Transformations.switchMap(filterState,
                input -> appRepository.filterByCategoryAndTime(input.categories, input.startDate, input.endDate));
    }

    public void setFilterState(FilterState filterState) {
        this.filterState.postValue(filterState);
    }

    public LiveData<List<Spending>> getAllSpending() {
        return allSpending;
    }
}
