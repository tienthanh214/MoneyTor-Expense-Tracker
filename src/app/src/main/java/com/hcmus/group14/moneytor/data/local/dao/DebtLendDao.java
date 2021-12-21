package com.hcmus.group14.moneytor.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface DebtLendDao {
    //Data manipulation
    @Insert
    public void insert(DebtLend debtLend);
    @Update
    public void update(DebtLend... debtLends);
    @Query("delete from debt_lend_table")
    public void deleteAllDebtLends();
    @Query("delete from debt_lend_table where recordId = :id")
    public void deleteDebtLend(int id);
    @Delete
    public void deleteDebtLend(DebtLend debtLend);

    //Data retrieval
    @Query("select * from debt_lend_table order by date desc")
    public LiveData<List<DebtLend>> getAllDebtLends();
    @Query("select * from debt_lend_table where category = :category order by date desc")
    public LiveData<List<DebtLend>> getDebtLends(String category);
    @Query("select * from debt_lend_table where recordId = :id")
    public DebtLend[] getDebtLendByID(int id);
}
