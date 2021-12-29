package com.hcmus.group14.moneytor.data.local.dao;

import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.lifecycle.LiveData;
import androidx.room.Update;
import java.util.List;
import com.hcmus.group14.moneytor.data.model.Reminder;
import androidx.room.Delete;

@Dao
public interface ReminderDao {
    //Data manipulation queries
    @Insert(onConflict=OnConflictStrategy.IGNORE)
    public void insert(Reminder reminder);
    @Update
    public void update(Reminder... reminders);
    @Query("delete from reminder_table")
    public void deleteAllReminders();
    @Query("delete from reminder_table where remID = :id")
    public void deleteReminder(int id);
    @Delete
    public void deleteReminder(Reminder reminder);

    //Total data retrieval
    @Query("select * from reminder_table order by date desc")
    public LiveData<List<Reminder>> getAllReminders();
    @Query("select * from reminder_table where remID = :id")
    public Reminder[] getReminderByID(int id);
}
