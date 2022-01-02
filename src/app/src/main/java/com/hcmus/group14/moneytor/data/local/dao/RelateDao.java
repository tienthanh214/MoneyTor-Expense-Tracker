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

import java.util.List;

@Dao
public interface RelateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRelate(Relate relate);

    @Delete
    void deleteRelate(Relate relate);

    @Update
    void updateRelate(Relate relate);

    @Query("SELECT * FROM relate_table")
    LiveData<List<Relate>> getAllRelates();

    @Transaction
    @Query("SELECT * FROM relate_table")
    LiveData<List<RelateWithSpending>> getRelatesWithSpending();

    @Transaction
    @Query("SELECT * FROM relate_table WHERE rel_id = :id")
    LiveData<List<RelateWithSpending>> getRelateBySpendingId(int id);

    @Query("select * from relate_table where rel_id = :id")
    Relate[] getRelateById(int id);

    @Query("select * from relate_table")
    List<Relate> getAllRelatesNoLiveData();

    @Query("DELETE FROM relate_table")
    void deleteAllRelates();
}
