package com.hcmus.group14.moneytor.ui.main;

import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_DEBTLEND;
import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_RELATE;
import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_SPENDGOAL;
import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_SPENDING;
import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_USERS;
import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_WALLET;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.data.model.Wallet;
import com.hcmus.group14.moneytor.firebase.FirebaseHelper;
import com.hcmus.group14.moneytor.ui.analysis.AnalysisActivity;
import com.hcmus.group14.moneytor.ui.analysis.VisualizeActivity;
import com.hcmus.group14.moneytor.ui.debtlend.DebtLendActivity;
import com.hcmus.group14.moneytor.ui.goal.GoalActivity;
import com.hcmus.group14.moneytor.ui.setting.SettingsActivity;
import com.hcmus.group14.moneytor.ui.spending.SpendingActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = FirebaseHelper.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sync user data for first login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // TODO: skip update data for users who have logged in this phone before
        // TODO: show a loading circle while synchronizing data
        if (user != null) updateData(user);
    }

    public void onOpenSpendingList(View view) {
        Intent intent = new Intent(this, SpendingActivity.class);
        startActivity(intent);
    }

    public void onOpenSpendingGoal(View view) {
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
    }

    public void onOpenDebtLend(View view) {
        Intent intent = new Intent(this, DebtLendActivity.class);
        startActivity(intent);
    }

    public void onOpenAnalysis(View view) {
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }

    private void onOpenSetting() {
        Intent settingIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingIntent);
    }

    public void onOpenVisualize(View view) {
        Intent intent = new Intent(this, VisualizeActivity.class);
        startActivity(intent);
    }

    public void onOpenSetting(View view) {
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionSetting) {
            onOpenSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData(FirebaseUser user) {
        // Check if user already exist and has data on the cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(COLLECTION_USERS).document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Cloud data detected");
                    builder.setPositiveButton("Retrieve progress",
                            (dialog, id) -> {
                                downloadUserPref(user);
                                downloadData(user);
                            });
                    builder.setNegativeButton("Upload progress",
                            (dialog, id) -> {
                                uploadUserPref(user);
                                uploadData(user);
                            });
                    builder.setNeutralButton("Do nothing",
                            (dialog, id) -> {
                            });
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Log.d(TAG, "No such user");
                    // TODO: upload data to Firestore
                    uploadUserPref(user);
                    uploadData(user);
                }
            } else {
                Log.d(TAG, "Check user failed with ", task.getException());
            }
        });
    }

    private void downloadData(FirebaseUser user) {
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

    private void downloadUserPref(FirebaseUser user) {
        // TODO: implement this
        FirebaseHelper.getUser(user, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        UserPref userPref = documentSnapshot.toObject(UserPref.class);
                        UserPref.saveToSharedPref(MainActivity.this, userPref);
                    } else Log.d(TAG, "No such document");
                } else Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void uploadData(FirebaseUser user) {
        // TODO: do this in the background if app freezes
        // TODO: Hoang - get list of DebtLend from db
        ArrayList<DebtLend> debtlends = new ArrayList<>();
        FirebaseHelper.putDocuments(user, COLLECTION_DEBTLEND, debtlends, task -> {
            if (task.isSuccessful()) Log.d(TAG, "DebtLend successfully uploaded");
            else Log.w(TAG, "Error uploading DebtLend");
        });
        // TODO: Hoang - get list of Relate from db
        ArrayList<Relate> relates = new ArrayList<>();
        FirebaseHelper.putDocuments(user, COLLECTION_RELATE, relates, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Relate successfully uploaded");
            else Log.w(TAG, "Error uploading Relate");
        });
        // TODO: Hoang - get list of SpendGoal from db
        ArrayList<SpendGoal> spendGoals = new ArrayList<>();
        FirebaseHelper.putDocuments(user, COLLECTION_SPENDGOAL, spendGoals, task -> {
            if (task.isSuccessful()) Log.d(TAG, "SpendGoal successfully uploaded");
            else Log.w(TAG, "Error uploading SpendGoal");
        });
        // TODO: Hoang - get list of Spending from db
        ArrayList<Spending> spendings = new ArrayList<>();
        FirebaseHelper.putDocuments(user, COLLECTION_SPENDING, spendings, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Spending successfully uploaded");
            else Log.w(TAG, "Error uploading Spending");
        });
        // TODO: Hoang - get list of Wallet from db
        ArrayList<Wallet> wallets = new ArrayList<>();
        FirebaseHelper.putDocuments(user, COLLECTION_WALLET, wallets, task -> {
            if (task.isSuccessful()) Log.d(TAG, "Wallet successfully uploaded");
            else Log.w(TAG, "Error uploading Wallet");
        });
    }

    private void uploadUserPref(FirebaseUser user) {
        // Update user name
        String name = PreferenceUtils.getString(this,
                PreferenceUtils.USER_PROFILE,
                PreferenceUtils.USER_NAME,
                getString(R.string.default_username));
        UserPref userPref = new UserPref(name, user.getUid(), user.getEmail());
        FirebaseHelper.putUser(userPref, user);
    }
}