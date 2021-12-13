package com.hcmus.group14.moneytor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.relation.RelateWithSpending;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.List;

@Dao
public abstract class SpendingDao {
    // for only spending
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertSpending(Spending spending);

    @Delete
    public abstract void deleteSpending(Spending spending);

    @Update
    public abstract void updateSpending(Spending spending);

    @Query("DELETE FROM spending_table")
    public abstract void deleteAllSpending();

    @Query("SELECT * FROM spending_table ORDER BY date")
    public abstract LiveData<List<Spending>> getAllSpending();

    // for bill sharing
    @Transaction
    @Query("SELECT * FROM spending_table")
    public abstract LiveData<List<SpendingWithRelates>> getSpendingWithRelates();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertSpendingRelateCrossRef(SpendingRelateCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertAllSpendingRelateCrossRef(List<SpendingRelateCrossRef> crossRefList);

    @Query("SELECT * FROM spending_relate")
    public abstract SpendingRelateCrossRef[] getAllSpendingRelate();
    // insert a spending with relate list
    @Transaction
    public void insertSpendingWithRelates(Spending spending, List<Relate> relates) {
        final long spendingId = insertSpending(spending);
        for (Relate relate : relates) {
            long relateId = insertRelate(relate);
            insertSpendingRelateCrossRef(new SpendingRelateCrossRef((int)spendingId, (int)relateId));
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertRelate(Relate relate);
}
