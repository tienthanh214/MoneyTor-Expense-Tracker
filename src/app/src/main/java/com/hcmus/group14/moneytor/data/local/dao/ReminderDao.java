package com.hcmus.group14.moneytor.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.lifecycle.LiveData;
import androidx.room.Update;
import java.util.List;
import com.hcmus.group14.moneytor.data.model.Reminder;
import com.hcmus.group14.moneytor.data.model.SpendGoal;

@Dao
public interface ReminderDao {
    //Data manipulation queries
    @Insert
    void insert(Reminder reminder);
    @Update
    public void update(Reminder... reminders);
    @Query("delete from reminder_table")
    public void deleteAllReminders();
    @Query("delete from reminder_table where remID = :id")
    public void deleteReminder(int id);

    //Total data retrieval
    @Query("select * from reminder_table order by date desc")
    public LiveData<List<SpendGoal>> getAllReminders();
}
