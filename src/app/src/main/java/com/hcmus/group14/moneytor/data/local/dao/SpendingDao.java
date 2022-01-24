package com.hcmus.group14.moneytor.data.local.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hcmus.group14.moneytor.data.model.Relate;
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

    @Query("DELETE FROM spending_table WHERE spending_id = :id")
    public abstract void deleteSpendingById(int id);

    @Update
    public abstract void updateSpending(Spending spending);

    @Query("DELETE FROM spending_table")
    public abstract void deleteAllSpending();

    @Query("SELECT * FROM spending_table ORDER BY date DESC, spending_id DESC")
    public abstract LiveData<List<Spending>> getAllSpending();

    @Query("select * from spending_table where category in (:cats)")
    public abstract List<Spending> getSpendingByCategories(List<String> cats);

    @Query("select * from spending_table")
    public abstract List<Spending> getAllSpendingsNoLiveData();

    // for bill sharing
    @Query("select * from spending_relate")
    public abstract List<SpendingRelateCrossRef> getAllSpendingRelateCrossRef();
    @Transaction
    @Query("SELECT * FROM spending_table")
    public abstract LiveData<List<SpendingWithRelates>> getAllSpendingWithRelates();

    @Transaction
    @Query("SELECT * FROM spending_table WHERE spending_id = :id LIMIT 1")
    public abstract LiveData<List<SpendingWithRelates>> getSpendingWithRelatesById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertSpendingRelateCrossRef(SpendingRelateCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertAllSpendingRelateCrossRef(List<SpendingRelateCrossRef> crossRefList);

    @Delete
    public abstract void deleteSpendingRelateCrossRef(SpendingRelateCrossRef crossRef);

    @Query("DELETE FROM spending_relate")
    public abstract void deleteAllSpendingRelateCrossRef();

    @Query("DELETE FROM spending_relate WHERE spending_id = :id")
    public abstract void deleteRelateBySpendingId(int id);

    @Query("SELECT * FROM spending_relate")
    public abstract SpendingRelateCrossRef[] getAllSpendingRelate();
    // insert a spending with relate list
    @Transaction
    public void insertSpendingWithRelates(Spending spending, List<Relate> relates) {
        final long spendingId = insertSpending(spending);
        if (spendingId == -1) return;
        if (relates == null) return;
        // insert all relates
        for (Relate relate : relates) {
            long relateId = insertRelate(relate);
            // if exists relate = -1, query to get relate index
            if (relateId == -1) {
                long[] result = getRelateIdByTel(relate.getTel());
                if (result.length > 0)
                    relateId = result[0];
            }
            insertSpendingRelateCrossRef(new SpendingRelateCrossRef((int)spendingId, (int)relateId));
        }
    }
    @Transaction
    public void updateSpendingWithRelates(Spending spending, List<Relate> olds, List<Relate> news) {
        updateSpending(spending);
        if (olds != null) {
            for (Relate relate : olds) {
                deleteSpendingRelateCrossRef(new SpendingRelateCrossRef(spending.getSpendingId(), relate.getRelateId()));
            }
        }
        if (news != null) {
            for (Relate relate : news) {
                long relateId = insertRelate(relate);
                if (relateId == -1) {
                    long[] result = getRelateIdByTel(relate.getTel());
                    if (result.length > 0)
                        relateId = result[0];
                }
                insertSpendingRelateCrossRef(new SpendingRelateCrossRef(spending.getSpendingId(), (int) relateId));
            }
        }
    }

    @Transaction
    public void deleteSpendingWithRelatesById(int id) {
        deleteSpendingById(id);
        deleteRelateBySpendingId(id);
    }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertRelate(Relate relate);
    @Query("SELECT rel_id FROM relate_table WHERE tel LIKE :tel")
    public abstract long[] getRelateIdByTel(String tel);

    // filter
    @Query("SELECT * FROM spending_table WHERE (date BETWEEN :startDate AND :endDate) AND (category IN (:cats))")
    public abstract LiveData<List<Spending>> filterByCategoryAndTime(List<String> cats, long startDate, long endDate);
    @Query("SELECT * FROM spending_table WHERE category IN (:cats)")
    public abstract LiveData<List<Spending>> filterByCategories(List<String> cats);
    @Query("SELECT * FROM spending_table WHERE date BETWEEN :startDate AND :endDate")
    public abstract LiveData<List<Spending>> filterByTime(long startDate, long endDate);

    @Transaction
    public void deleteAllSpendingWithRelate() {
        deleteAllSpending();
        deleteAllSpendingRelateCrossRef();
    }
}
