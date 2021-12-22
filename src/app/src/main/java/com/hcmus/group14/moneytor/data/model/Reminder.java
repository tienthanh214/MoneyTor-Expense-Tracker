package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

import java.io.Serializable;

/*
The class entity for the Reminder table (REMINDER)
              |-----------rem_id: INT NOT NULL PRIMARY KEY
              |-----------type: CHAR(30) NOT NULL
              |-----------when: DATETIME NOT NULL
              |-----------date: DATETIME NOT NULL
              |-----------desc: NVARCHAR(100)
Description: The database saves all reminders that the user have set, placed on the local machine.
Each reminder is identified by its unique ID (rem_id) and consists of the type of reminder (daily,
weekly, monthly or annually), when to remind, date created and description which can be left blank
or should be 100 characters long or lower.
 */

@Entity(tableName = "reminder_table")
public class Reminder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int remID = 0;
    @ColumnInfo(name = "type") @NonNull
    private String type;
    @ColumnInfo(name = "when") @NonNull
    private String when;      // 4 characters, at what hour and what minute
    @ColumnInfo(name = "date")
    private long date;      // used as the basis to when should the reminder starts
    @ColumnInfo(name = "desc") @Nullable
    private String desc;

    @Ignore
    public Reminder()
    {
        this("", "", 0L, "");
    }

    public Reminder(@NonNull String type, @NonNull String when,
                    long date, @Nullable String desc)
    {
        this.type = type;
        this.when = when;
        this.date = date;
        this.desc = desc;
    }

    public int getRemID() {
        return remID;
    }

    public void setRemID(int remID) {
        this.remID = remID;
    }

    @NonNull
    public String getType()
    {
        return type;
    }
    public void setType(@NonNull String type)
    {
        this.type = type;
    }
    @NonNull
    public String getWhen()
    {
        return when;
    }
    public void setWhen(@NonNull String when) {
        assert (when.length() == 4);
        this.when = when;
    }
    public long getDate()
    {
        return date;
    }
    public void setDate(long date)
    {
        this.date = date;
    }
    @Nullable
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(@Nullable String desc)
    {
        this.desc = desc;
    }
}
