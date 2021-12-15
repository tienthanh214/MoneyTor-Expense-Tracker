package com.hcmus.group14.moneytor.services.reminder;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.hcmus.group14.moneytor.data.local.AppViewModel;
import com.hcmus.group14.moneytor.data.model.Reminder;
import java.util.List;

public class ReminderViewModel extends AppViewModel {
    private LiveData<List<Reminder>> allReminders;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Reminder>> getAllReminders()
    {
        return allReminders;
    }
    public void insertReminder(Reminder reminder)
    {
        appRepository.insertReminder(reminder);
        allReminders = appRepository.getAllReminders();
    }
}
