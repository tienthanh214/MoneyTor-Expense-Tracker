package com.hcmus.group14.moneytor.services.reminder;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Reminder;
import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> reminderStatus;

    public ReminderViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<Boolean> getReminderStatus() {
        return reminderStatus;
    }


}
