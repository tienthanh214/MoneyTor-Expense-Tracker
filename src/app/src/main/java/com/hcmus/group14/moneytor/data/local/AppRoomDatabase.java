package com.hcmus.group14.moneytor.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hcmus.group14.moneytor.data.local.dao.*;
import com.hcmus.group14.moneytor.data.model.*;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Spending.class, Relate.class, SpendingRelateCrossRef.class,
        Reminder.class, SpendGoal.class}, // all table
        version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    static public AppRoomDatabase INSTANCE = null;
    // database write executor async
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);
    // all DAO
    public abstract SpendingDao spendingDao();
    public abstract RelateDao relateDao();
    public abstract ReminderDao reminderDao();
    public abstract SpendGoalDao spendGoalDao();

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "moneytor_database")
                            .addCallback(sOnOpenCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    // below code for testing database, could be delete
    final static private Callback sOnOpenCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            SpendingDao spendingDao = INSTANCE.spendingDao();
//            RelateDao relateDao = INSTANCE.relateDao();
            Log.i("@@@ create", "complete");
            // test spending && relate
            List<Spending> allRes = new ArrayList<>();

            databaseWriteExecutor.execute(() -> {
                if (spendingDao.getAllSpendingRelate().length < 1) {
                    Spending s1 = (
                            new Spending(1000, "Hello 1", "None", "hihi", 123));
                    Spending s2 = (
                            new Spending(1000, "Hello 2", "Food", "nothing to show", 1010101));
                    Spending s3 = (
                            new Spending(1000, "Hello 3", "Study", "say to show", 141));

                    Relate r1 = new Relate("NDTT", "123123123");
                    Relate r2 = new Relate("hauhau", "000012111");
                    Relate r3 = new Relate("binh", "123321123");

                    spendingDao.insertSpendingWithRelates(s1, new ArrayList<>(Arrays.asList(r1, r2)));
                    spendingDao.insertSpendingWithRelates(s2, new ArrayList<>(Arrays.asList(r1, r3)));
                    spendingDao.insertSpendingWithRelates(s3, new ArrayList<>(Arrays.asList(r1, r3, r2)));
                }
                SpendingRelateCrossRef[] res = spendingDao.getAllSpendingRelate();
                for (SpendingRelateCrossRef x : res) {
                    Log.i("@@@ cross ref", x.spendingId + " " + x.relateId);
                }
                // can't use LiveData in thread pool
            });
        }
    };
}
