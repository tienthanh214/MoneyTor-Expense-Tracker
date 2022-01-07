package com.hcmus.group14.moneytor.data.local;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hcmus.group14.moneytor.data.firebase.FirebaseHelper;
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
import com.hcmus.group14.moneytor.data.model.relation.DebtLendAndRelate;
import com.hcmus.group14.moneytor.data.model.relation.SpendingRelateCrossRef;
import com.hcmus.group14.moneytor.data.model.relation.SpendingWithRelates;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    // all dao
    final private SpendingDao spendingDao;
    final private ReminderDao reminderDao;
    final private SpendGoalDao spendGoalDao;
    final private DebtLendDao debtLendDao;
    final private RelateDao relateDao;
    final private WalletDao walletDao;
    //Data repositories
    private final LiveData<List<Spending>> allSpending;
    private final LiveData<List<Reminder>> allReminders;
    private final LiveData<List<SpendGoal>> allSpendGoals;
    private final LiveData<List<DebtLend>> allDebtLends;
    private final LiveData<List<Wallet>> allWallets;
    //For FireBase interactions


    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        // get all DAO
        spendingDao = db.spendingDao();
        reminderDao = db.reminderDao();
        spendGoalDao = db.spendGoalDao();
        debtLendDao = db.debtLendDao();
        relateDao = db.relateDao();
        walletDao = db.walletDao();

        allSpending = spendingDao.getAllSpending();
        allReminders = reminderDao.getAllReminders();
        allSpendGoals = spendGoalDao.getAllSpendGoals();
        allDebtLends = debtLendDao.getAllDebtLends();
        allWallets = walletDao.getAllWallets();
    }

    // -------------- spending note --------------
    public LiveData<List<SpendingWithRelates>> getSpendingWithRelatesById(int id) {
        return spendingDao.getSpendingWithRelatesById(id);
    }

    public LiveData<List<Spending>> getAllSpending() {
        return allSpending;
    }

    public List<Spending> getSpendingByCategories(List<String> cats) {
        return spendingDao.getSpendingByCategories(cats);
    }

    public void insertSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertSpending(spending);
        });
    }

    public void insertAllSpendingRelateCrossRef(List<SpendingRelateCrossRef> crossRefList) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertAllSpendingRelateCrossRef(crossRefList);
        });
    }


    public void insertSpendingWithRelates(Spending spending, List<Relate> relates) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertSpendingWithRelates(spending, relates);
        });
    }

    // delete a spending with all relates
    public void deleteSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.deleteSpendingWithRelatesById(spending.getSpendingId());
        });
    }

    public void updateSpending(Spending spending) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.updateSpending(spending);
        });
    }

    public LiveData<List<Spending>> filterSpendingByCategoryAndTime(List<String> cats,
                                                                    long startDate, long endDate) {
        if (startDate > endDate) {
            long temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        if ((cats == null || cats.isEmpty()) && endDate == -1) // wrong filter
            return spendingDao.getAllSpending();
        if ((cats != null && !cats.isEmpty()) && endDate != -1) // valid filter
            return spendingDao.filterByCategoryAndTime(cats, startDate, endDate);
        else if (endDate != -1)  // if only time valid
            return spendingDao.filterByTime(startDate, endDate);
        else  // if only cats valid
            return spendingDao.filterByCategories(cats);
    }

    // update spending with new relates, remove old relates
    public void updateSpendingWithRelates(Spending spending, List<Relate> olds, List<Relate> news) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.updateSpendingWithRelates(spending, olds, news);
        });
    }

    private void insertSpendingRelateCrossRef(SpendingRelateCrossRef crossRef) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            spendingDao.insertSpendingRelateCrossRef(crossRef);
        });
    }

    // -------------- wallet --------------
    public LiveData<List<Wallet>> getAllWallets() {
        return allWallets;
    }

    public LiveData<List<Wallet>> getWalletByProvider(String provider) {
        return walletDao.getWalletByProvider(provider);
    }

    public void insertWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.insertWallet(wallet);
        });
    }

    public void deleteWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.deleteWallet(wallet);
        });
    }

    public void updateWallet(Wallet wallet) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            walletDao.updateWallet((wallet));
        });
    }

    // -------------- Relate (share bill, debt target) --------------
    public Relate getRelateById(int id) {
        return relateDao.getRelateById(id)[0];
    }

    public void insertRelate(Relate relate) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            relateDao.insertRelate(relate);
        });
    }

    //---------------------Reminders-----------------------
    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public void insertReminder(Reminder reminder) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> reminderDao.insert(reminder));
    }

    public Reminder[] getReminderById(int id) {
        return reminderDao.getReminderByID(id);
    }

    //---------------------Spending goals------------------
    public LiveData<List<SpendGoal>> getAllSpendGoals() {
        return allSpendGoals;
    }

    public void insertSpendGoal(SpendGoal spendGoal) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> spendGoalDao.insert(spendGoal));
    }

    public void deleteSpendGoal(SpendGoal spendGoal) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> spendGoalDao.deleteSpendGoal(spendGoal));
    }

    public void updateSpendGoal(SpendGoal spendGoal) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> spendGoalDao.update(spendGoal));
    }

    public LiveData<List<SpendGoal>> getSpendGoalById(int id) {
        return spendGoalDao.getSpendGoalByID(id);
    }

    public LiveData<List<SpendGoal>> filterSpendGoalByCategoryAndTime(List<String> cats,
                                                                      long startDate,
                                                                      long endDate) {
        if (startDate > endDate) {
            long temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        if ((cats == null || cats.isEmpty()) && endDate == -1) // wrong filter
            return spendGoalDao.getAllSpendGoals();
        if ((cats != null && !cats.isEmpty()) && endDate != -1) // valid filter
            return spendGoalDao.filterByCategoryAndTime(cats, startDate, endDate);
        else if (endDate != -1)  // if only time valid
            return spendGoalDao.filterByTime(startDate, endDate);
        else  // if only cats valid
            return spendGoalDao.filterByCategories(cats);
    }

    //---------------------Debt/lends----------------------
    public LiveData<List<DebtLend>> getAllDebtLends() {
        return allDebtLends;
    }

    public void insertDebtLend(DebtLend debtLend) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> debtLendDao.insert(debtLend));
    }

    public DebtLend[] getDebtLendById(int id) {
        return debtLendDao.getDebtLendByID(id);
    }

    public LiveData<List<DebtLendAndRelate>> getAllDebtLendAndRelate() {
        return debtLendDao.getAllDebtLendAndRelate();
    }

    public LiveData<List<DebtLendAndRelate>> getDebtLendAndRelateById(int id) {
        return debtLendDao.getDebtLendAndRelateById(id);
    }

    public void updateDebtLend(DebtLend debtLend) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> debtLendDao.update(debtLend));
    }

    public void insertDebtLendWithTarget(DebtLend debtLend, Relate target) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> debtLendDao.insertDebtLendWithTarget(debtLend, target));
    }

    public void updateDebtLendWithTarget(DebtLend debtLend, Relate target) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> debtLendDao.updateDebtLendWithTarget(debtLend, target));
    }

    public void deleteDebtLend(DebtLend debtLend) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> debtLendDao.deleteDebtLend(debtLend));
    }

    public LiveData<List<DebtLendAndRelate>> filterDebtLendByCategoryAndTime(List<String> cats,
                                                                             long startDate,
                                                                             long endDate) {
        if (startDate > endDate) {
            long temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        if ((cats == null || cats.isEmpty()) && endDate == -1) // wrong filter
            return debtLendDao.getAllDebtLendAndRelate();
        if ((cats != null && !cats.isEmpty()) && endDate != -1) // valid filter
            return debtLendDao.filterByCategoryAndTime(cats, startDate, endDate);
        else if (endDate != -1)  // if only time valid
            return debtLendDao.filterByTime(startDate, endDate);
        else  // if only cats valid
            return debtLendDao.filterByCategories(cats);
    }

    // delete
    private void deleteAllData() {
        spendingDao.deleteAllSpendingWithRelate();
        debtLendDao.deleteAllDebtLends();
        walletDao.deleteAllWallets();
        spendGoalDao.deleteAllSpendGoals();
        relateDao.deleteAllRelates();
    }
    //----------------FireBase interaction services--------------------

    public void uploadData(FirebaseUser user) {
        ArrayList<DebtLend> debtLends =
                (ArrayList<DebtLend>) debtLendDao.getAllDebtLendsNoLiveData();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_DEBTLEND, debtLends,
                task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "DebtLend successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading DebtLend");
                });

        ArrayList<Relate> relates = (ArrayList<Relate>) relateDao.getAllRelatesNoLiveData();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_RELATE, relates,
                task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "Relate successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading Relate");
                });

        ArrayList<SpendGoal> spendGoals =
                (ArrayList<SpendGoal>) spendGoalDao.getAllSpendGoalsNoLiveData();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_SPENDGOAL, spendGoals,
                task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "SpendGoal successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading SpendGoal");
                });

        ArrayList<Spending> spendings =
                (ArrayList<Spending>) spendingDao.getAllSpendingsNoLiveData();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_SPENDING, spendings,
                task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "Spending successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading Spending");
                });

        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.getAllWalletsNoLiveData();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_WALLET, wallets,
                task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "Wallet successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading Wallet");
                });

        ArrayList<SpendingRelateCrossRef> spendingRelateCrossRefs =
                (ArrayList<SpendingRelateCrossRef>) spendingDao.getAllSpendingRelateCrossRef();
        FirebaseHelper.putCollection(user, FirebaseHelper.COLLECTION_SPENDING_RELATE_CROSS_REF,
                spendingRelateCrossRefs, task -> {
                    if (task.isSuccessful())
                        Log.d(FirebaseHelper.TAG, "SpendingRelateCrossRef successfully uploaded");
                    else Log.w(FirebaseHelper.TAG, "Error uploading SpendingRelateCrossRef");
                });
    }

    public void downloadData(FirebaseUser user) {
        deleteAllData();

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_DEBTLEND, DebtLend.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DebtLend debtLend = document.toObject(DebtLend.class);
                            insertDebtLend(debtLend);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_RELATE, Relate.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Relate relate = document.toObject(Relate.class);
                            insertRelate(relate);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_SPENDGOAL, SpendGoal.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SpendGoal spendGoal = document.toObject(SpendGoal.class);
                            insertSpendGoal(spendGoal);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_SPENDING, Spending.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Spending spending = document.toObject(Spending.class);
                            insertSpending(spending);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_WALLET, Wallet.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Wallet wallet = document.toObject(Wallet.class);
                            insertWallet(wallet);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_SPENDING_RELATE_CROSS_REF,
                SpendingRelateCrossRef.class, task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SpendingRelateCrossRef spendingRelateCrossRef =
                                    document.toObject(SpendingRelateCrossRef.class);
                            insertSpendingRelateCrossRef(spendingRelateCrossRef);
                        }
                    else
                        Log.d(FirebaseHelper.TAG, "Error getting documents: ", task.getException());
                });
    }
}
