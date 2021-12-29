package com.hcmus.group14.moneytor.firebase;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.data.model.Wallet;


import java.util.ArrayList;

public class FirebaseHelper {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_SPENDING = "spending";
    public static final String COLLECTION_DEBTLEND = "debt_lend";
    public static final String COLLECTION_SPENDGOAL = "spend_goal";
    public static final String COLLECTION_WALLET = "wallet";
    public static final String COLLECTION_RELATE = "relate";
    public static final String TAG = FirebaseHelper.class.getName();


    public static void putUser(FirebaseUser user, UserPref userPref) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .set(userPref)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "User info successfully uploaded");
                    else Log.w(TAG, "Error uploading User info");
                });
    }

    public static void getUser(FirebaseUser user,
                               OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

//    public static void putSpending(Spending spending, FirebaseUser user) {
//        if (user == null) return;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(COLLECTION_USERS).document(user.getUid())
//                .collection(COLLECTION_SPENDING)
//                .add(spending)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, "Spending successfully uploaded");
//                    else Log.w(TAG, "Error uploading Spending");
//                });
//    }
//
//    public static void putDebtLend(DebtLend debtLend, FirebaseUser user) {
//        if (user == null) return;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(COLLECTION_USERS).document(user.getUid())
//                .collection(COLLECTION_DEBTLEND)
//                .add(debtLend)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, "DebtLend successfully uploaded");
//                    else Log.w(TAG, "Error uploading DebtLend");
//                });
//    }
//
//    public static void putSpendGoal(SpendGoal spendGoal, FirebaseUser user) {
//        if (user == null) return;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(COLLECTION_USERS).document(user.getUid())
//                .collection(COLLECTION_SPENDGOAL)
//                .add(spendGoal)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, "SpendGoal successfully uploaded");
//                    else Log.w(TAG, "Error uploading SpendGoal");
//                });
//    }
//
//    public static void putWallet(Wallet wallet, FirebaseUser user) {
//        if (user == null) return;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(COLLECTION_USERS).document(user.getUid())
//                .collection(COLLECTION_WALLET)
//                .add(wallet)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, "Wallet successfully uploaded");
//                    else Log.w(TAG, "Error uploading Wallet");
//                });
//    }
//
//    public static void putRelate(Relate relate, FirebaseUser user) {
//        if (user == null) return;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(COLLECTION_USERS).document(user.getUid())
//                .collection(COLLECTION_RELATE)
//                .add(relate)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, "Relate successfully uploaded");
//                    else Log.w(TAG, "Error uploading Relate");
//                });
//    }


    public static <T> void putDocument(FirebaseUser user, T object, String collection) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection)
                .add(object)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, collection + " successfully uploaded");
                    else Log.w(TAG, "Error uploading Relate");
                });
    }

    public static <T> void getDocuments(FirebaseUser user, String collection, Class<T> type,
                                        OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static <T> void putDocuments(FirebaseUser user, String collection, ArrayList<T> objects,
                                        OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection);
        WriteBatch batch = db.batch();
        for (T object : objects) {
            DocumentReference documentRef = collectionRef.document();
            batch.set(documentRef, object);
        }
        batch.commit().addOnCompleteListener(onCompleteListener);
    }

    public static void uploadData(FirebaseUser user) {
        // TODO: do this in the background if app freezes
        ArrayList<DebtLend> debtlends = new ArrayList<>();
        // TODO: Hoang - get list of DebtLend from db

        FirebaseHelper.putDocuments(user, COLLECTION_DEBTLEND, debtlends, task -> {
            if (task.isSuccessful()) Log.d(TAG, "DebtLend successfully uploaded");
            else Log.w(TAG, "Error uploading DebtLend");
        });

        ArrayList<Relate> relates = new ArrayList<>();
        // TODO: Hoang - get list of Relate from db
        FirebaseHelper.putDocuments(user, COLLECTION_RELATE, relates, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Relate successfully uploaded");
            else Log.w(TAG, "Error uploading Relate");
        });

        ArrayList<SpendGoal> spendGoals = new ArrayList<>();
        // TODO: Hoang - get list of SpendGoal from db
        FirebaseHelper.putDocuments(user, COLLECTION_SPENDGOAL, spendGoals, task -> {
            if (task.isSuccessful()) Log.d(TAG, "SpendGoal successfully uploaded");
            else Log.w(TAG, "Error uploading SpendGoal");
        });

        ArrayList<Spending> spendings = new ArrayList<>();
        // TODO: Hoang - get list of Spending from db
        FirebaseHelper.putDocuments(user, COLLECTION_SPENDING, spendings, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Spending successfully uploaded");
            else Log.w(TAG, "Error uploading Spending");
        });

        ArrayList<Wallet> wallets = new ArrayList<>();
        // TODO: Hoang - get list of Wallet from db
        FirebaseHelper.putDocuments(user, COLLECTION_WALLET, wallets, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Wallet successfully uploaded");
            else Log.w(TAG, "Error uploading Wallet");
        });
    }

    public static void downloadData(FirebaseUser user) {
        // TODO: do this in the background if app freezes
        FirebaseHelper.getDocuments(user, COLLECTION_DEBTLEND, DebtLend.class, task -> {
            if (task.isSuccessful())
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DebtLend debtLend = document.toObject(DebtLend.class);
                    // TODO: Hoang - Insert to db
                }
            else Log.d(TAG, "Error getting documents: ", task.getException());
        });

        FirebaseHelper.getDocuments(user, COLLECTION_RELATE, Relate.class, task -> {
            if (task.isSuccessful())
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Relate relate = document.toObject(Relate.class);
                    // TODO: Hoang - Insert to db
                }
            else Log.d(TAG, "Error getting documents: ", task.getException());
        });

        FirebaseHelper.getDocuments(user, COLLECTION_SPENDGOAL, SpendGoal.class, task -> {
            if (task.isSuccessful())
                for (QueryDocumentSnapshot document : task.getResult()) {
                    SpendGoal spendGoal = document.toObject(SpendGoal.class);
                    // TODO: Hoang - Insert to db
                }
            else Log.d(TAG, "Error getting documents: ", task.getException());
        });

        FirebaseHelper.getDocuments(user, COLLECTION_SPENDING, Spending.class, task -> {
            if (task.isSuccessful())
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Spending spending = document.toObject(Spending.class);
                    // TODO: Hoang - Insert to db
                }
            else Log.d(TAG, "Error getting documents: ", task.getException());
        });

        FirebaseHelper.getDocuments(user, COLLECTION_WALLET, Wallet.class, task -> {
            if (task.isSuccessful())
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Wallet wallet = document.toObject(Wallet.class);
                    // TODO: Hoang - Insert to db
                }
            else Log.d(TAG, "Error getting documents: ", task.getException());
        });
    }
}
