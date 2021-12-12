package com.hcmus.group14.moneytor.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.lifecycle.LiveData;
import androidx.room.Update;
import java.util.List;
import com.hcmus.group14.moneytor.data.model.SpendGoal;

@Dao
public interface SpendGoalDao {
    //Data manipulation queries
    @Insert
    void insert(SpendGoal spendGoal);
    @Update
    public void update(SpendGoal... spendGoals);
    @Query("delete from spend_goal_table")
    public void deleteAllSpendGoals();
    @Query("delete from spend_goal_table where goalID = :id")
    public void deleteSpendGoal(int id);

    //Total data retrieval
    @Query("select * from spend_goal_table order by date desc")
    public LiveData<List<SpendGoal>> getAllSpendGoals();
}
