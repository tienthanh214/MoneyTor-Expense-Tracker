package com.hcmus.group14.moneytor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;

import java.util.List;

@Dao
public abstract class DebtLendDao {
    //Data manipulation
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract public long insert(DebtLend debtLend);
    @Update
    abstract public void update(DebtLend... debtLends);
    @Query("delete from debt_lend_table")
    abstract public void deleteAllDebtLends();
    @Query("delete from debt_lend_table where recordId = :id")
    abstract public void deleteDebtLend(int id);
    @Delete
    abstract public void deleteDebtLend(DebtLend debtLend);

    //Data retrieval
    @Query("select * from debt_lend_table order by date desc")
    abstract public LiveData<List<DebtLend>> getAllDebtLends();
    @Query("select * from debt_lend_table where category = :category order by date desc")
    abstract public LiveData<List<DebtLend>> getDebtLends(String category);
    @Query("select * from debt_lend_table where recordId = :id")
    abstract public DebtLend[] getDebtLendByID(int id);
    //Cross reference data retrieval
    @Transaction @Query("select * from debt_lend_table order by date desc, recordId desc")
    abstract public LiveData<List<DebtLendAndRelate>> getAllDebtLendAndRelate();
    @Transaction @Query("select * from debt_lend_table where recordId = :id")
    abstract public LiveData<List<DebtLendAndRelate>> getDebtLendAndRelateById(int id);
    // insert cross reference data
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract public long insertTarget(Relate relate);
    @Query("SELECT rel_id FROM relate_table WHERE tel LIKE :tel")
    public abstract long[] getRelateIdByTel(String tel);

    @Transaction
    public void insertDebtLendWithTarget(DebtLend debtLend, Relate target) {
        if (target == null) return;
        long targetId = insertTarget(target);
        if (targetId == -1) {
            long[] result = getRelateIdByTel(target.getTel());
            if (result.length > 0)
                targetId = result[0];
            else
                return;
        }
        debtLend.setTarget((int)targetId);
        insert(debtLend);
    }

    @Transaction
    public void updateDebtLendWithTarget(DebtLend debtLend, Relate target) {
        if (target == null) return;
        long targetId = insertTarget(target);
        if (targetId == -1) {
            long[] result = getRelateIdByTel(target.getTel());
            if (result.length > 0)
                targetId = result[0];
        }
        debtLend.setTarget((int)targetId);
        update(debtLend);
    }

    @Transaction
    @Query("SELECT * FROM debt_lend_table WHERE (date BETWEEN :startDate AND :endDate) AND (category IN (:cats))")
    public abstract LiveData<List<DebtLendAndRelate>> filterByCategoryAndTime(List<String> cats, long startDate, long endDate);

    @Transaction
    @Query("SELECT * FROM debt_lend_table WHERE category IN (:cats)")
    public abstract LiveData<List<DebtLendAndRelate>> filterByCategories(List<String> cats);

    @Transaction
    @Query("SELECT * FROM debt_lend_table WHERE date BETWEEN :startDate AND :endDate")
    public abstract LiveData<List<DebtLendAndRelate>> filterByTime(long startDate, long endDate);

    @Query("select * from debt_lend_table")
    public abstract List<DebtLend> getAllDebtLendsNoLiveData();
}
