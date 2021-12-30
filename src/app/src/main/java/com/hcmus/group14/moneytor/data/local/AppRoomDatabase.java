package com.hcmus.group14.moneytor.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hcmus.group14.moneytor.data.local.dao.DebtLendDao;
import com.hcmus.group14.moneytor.data.local.dao.RelateDao;
import com.hcmus.group14.moneytor.data.local.dao.ReminderDao;
import com.hcmus.group14.moneytor.data.local.dao.SpendGoalDao;
import com.hcmus.group14.moneytor.data.local.dao.SpendingDao;
import com.hcmus.group14.moneytor.data.local.dao.WalletDao;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Reminder;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.Wallet;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.services.options.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Spending.class, Relate.class, SpendingRelateCrossRef.class,
        Reminder.class, SpendGoal.class, DebtLend.class, Wallet.class}, // all table
        version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    static public AppRoomDatabase INSTANCE = null;
    // database write executor async
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);
    // all DAO
    public abstract SpendingDao spendingDao();
    public abstract RelateDao relateDao();
    public abstract ReminderDao reminderDao();
    public abstract SpendGoalDao spendGoalDao();
    public abstract DebtLendDao debtLendDao();
    public abstract WalletDao walletDao();

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "moneytor_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
