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
}package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

import java.security.MessageDigest;
/*
This is the class entity for the Spending goal table (SPEND_GOAL). The schema is defined as follows:
              |-----------goal_id: INT NOT NULL PRIMARY KEY
              |-----------category: CHAR(30)
              |-----------spending_cap: INT NOT NULL
              |-----------date: DATETIME NOT NULL
              |-----------desc: NVARCHAR(100)
Description: The database saves all spending goals the user has set, placed on the local machine.
Each goal is identified by its unique ID. The detailed information of a spending goal consists of
the spending category (set to null if it is for general spending), the spending cap (also known as
the goal) and the date/time of creation. The user can choose to write a description for the spending
goal which should be 100 characters or less, or leave it blank.
 */


@Entity(tableName = "spend_goal_table")
public class SpendGoal {
    @PrimaryKey(autoGenerate = true)
    private int goalID = 0;

    @ColumnInfo(name = "category") @NonNull
    private String category;
    @ColumnInfo(name = "spending_cap")
    private int spendingCap;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "desc") @Nullable
    private String desc;

    public SpendGoal(@NonNull String category, int spendingCap,
                     long date, @Nullable String desc)
    {
        this.category = category;
        this.spendingCap = spendingCap;
        this.date = date;
        this.desc = desc;
    }
    public String getCategory()
    {
        return category;
    }
    public void setCategory(@NonNull String category)
    {
        this.category = category;
    }
    public int getSpendingCap()
    {
        return spendingCap;
    }
    public void setSpendingCap(int spendingCap)
    {
        this.spendingCap = spendingCap;
    }
    public long getDate()
    {
        return date;
    }
    public void setDate(long date)
    {
        this.date = date;
    }
    public @Nullable String getDesc()
    {
        return desc;
    }
    public void setDesc(@Nullable String desc)
    {
        this.desc = desc;
    }
}