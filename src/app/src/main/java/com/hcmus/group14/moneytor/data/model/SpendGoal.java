package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
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
public class SpendGoal implements Note {
    @PrimaryKey
    private int goalID = 0;

    @ColumnInfo(name = "category")
    @NonNull
    private String category;
    @ColumnInfo(name = "spending_cap")
    private long spendingCap;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "desc")
    @Nullable
    private String desc;

    @Ignore
    public SpendGoal() {
        // Required by Firestore. Do not remove
        this("", 0, 0L, "");
    }

    public SpendGoal(@NonNull String category, long spendingCap,
                     long date, @Nullable String desc) {
        this.category = category;
        this.spendingCap = spendingCap;
        this.date = date;
        this.desc = desc;
    }

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public long getSpendingCap() {
        return spendingCap;
    }

    public void setSpendingCap(long spendingCap) {
        this.spendingCap = spendingCap;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public @Nullable
    String getDesc() {
        return desc;
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SpendGoal{" +
                "goalID=" + goalID +
                ", category='" + category + '\'' +
                ", spendingCap=" + spendingCap +
                ", date=" + date +
                ", desc='" + desc + '\'' +
                '}';
    }
}