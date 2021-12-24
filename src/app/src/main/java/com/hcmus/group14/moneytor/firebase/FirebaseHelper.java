package com.hcmus.group14.moneytor.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.data.model.Wallet;

public class FirebaseHelper {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_SPENDING = "spending";
    public static final String COLLECTION_DEBTLEND = "debt_lend";
    public static final String COLLECTION_SPENDGOAL = "spend_goal";
    public static final String COLLECTION_WALLET = "wallet";
    public static final String COLLECTION_RELATE = "relate";
    private static final String TAG = FirebaseHelper.class.getName();

    public static void putUser(UserPref userPref, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .set(userPref)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "User info successfully uploaded");
                    else Log.w(TAG, "Error uploading User info");
                });
    }

    public static void getUser(FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                UserPref userPref = documentSnapshot.toObject(UserPref.class);

                            } else Log.d(TAG, "No such document");
                        } else Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }

    public static void putSpending(Spending spending, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_SPENDING)
                .add(spending)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Spending successfully uploaded");
                    else Log.w(TAG, "Error uploading Spending");
                });
    }

    public static void putDebtLend(DebtLend debtLend, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_DEBTLEND)
                .add(debtLend)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "DebtLend successfully uploaded");
                    else Log.w(TAG, "Error uploading DebtLend");
                });
    }

    public static void putSpendGoal(SpendGoal spendGoal, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_SPENDGOAL)
                .add(spendGoal)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "SpendGoal successfully uploaded");
                    else Log.w(TAG, "Error uploading SpendGoal");
                });
    }

    public static void putWallet(Wallet wallet, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_WALLET)
                .add(wallet)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Wallet successfully uploaded");
                    else Log.w(TAG, "Error uploading Wallet");
                });
    }

    public static void putRelate(Relate relate, FirebaseUser user) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_RELATE)
                .add(relate)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Relate successfully uploaded");
                    else Log.w(TAG, "Error uploading Relate");
                });
    }
}
