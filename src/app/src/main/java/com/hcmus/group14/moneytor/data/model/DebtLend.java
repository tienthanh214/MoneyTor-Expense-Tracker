package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/*
              |-----------record_id: INT NOT NULL PRIMARY KEY
              |-----------category: CHAR(30)
              |-----------value: INT NOT NULL
              |-----------target: INT NOT NULL
              |-----------debt: INT CHECK (debt = 0 OR debt = 1)
              |-----------date: DATETIME NOT NULL
              |-----------desc: NVARCHAR(100)
Description: The database saves all debt/lend records the user has noted, placed on the local
machine.
Each debt/lend record is identified by its unique ID (record_id). The detailed information of a
record
consists of:
    - Whether is this record for a debt or a lend (0 if lend, 1 if debt)
    - Category of the debt/lend (not necessary)
    - How much money loaned/lent
    - Who to loan/lend
    - Date/time of the debt/lend
    - Description (up to 100 characters, optional)
 */
@Entity(tableName = "debt_lend_table")
public class DebtLend implements Note {

    @PrimaryKey(autoGenerate = true)
    private int recordId = 0;

    @ColumnInfo(name = "category")
    @Nullable
    private String category;
    @ColumnInfo(name = "value")
    private long value;
    @ColumnInfo(name = "target")
    private int target;
    @ColumnInfo(name = "debt")
    private int debt; //assert(debt==0||debt==1);
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "desc")
    @Nullable
    private String desc;

    @Ignore
    public DebtLend() {
        // Required by Firestore. Do not remove
        this("", 0, 0, 0, 0L, "");
    }

    public DebtLend(@Nullable String category, long value, int target,
                    int debt, long date, @Nullable String desc) {
        assert (debt == 0 || debt == 1);
        this.category = category;
        this.value = value;
        this.target = target;
        this.debt = debt;
        this.date = date;
        this.desc = desc;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public @Nullable
    String getCategory() {
        return category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        assert (debt == 0 || debt == 1);
        this.debt = debt;
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

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "DebtLend{" +
                "recordId=" + recordId +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", target=" + target +
                ", debt=" + debt +
                ", date=" + date +
                ", desc='" + desc + '\'' +
                '}';
    }
}
