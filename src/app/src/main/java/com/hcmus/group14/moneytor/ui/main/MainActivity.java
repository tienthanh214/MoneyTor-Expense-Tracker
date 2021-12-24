package com.hcmus.group14.moneytor.ui.main;

import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_USERS;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.firebase.FirebaseHelper;
import com.hcmus.group14.moneytor.ui.analysis.AnalysisActivity;
import com.hcmus.group14.moneytor.ui.debtlend.DebtLendActivity;
import com.hcmus.group14.moneytor.ui.goal.GoalActivity;
import com.hcmus.group14.moneytor.ui.setting.SettingsActivity;
import com.hcmus.group14.moneytor.ui.spending.SpendingActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = FirebaseHelper.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sync user data for first login
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // TODO: skip update data for users who have logged in this phone before
            updateData(user);
        }
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
                    // TODO: let user choose between import cloud data or upload current data
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
        // TODO: do this asynchronously and in the background
    }

    private void downloadUserPref(FirebaseUser user) {

    }

    private void uploadData(FirebaseUser user) {
        // TODO: do this asynchronously and in the background
    }

    private void uploadUserPref(FirebaseUser user) {
        // Update user name
        String name = PreferenceUtils.getString(
                PreferenceUtils.getInstance(this, getString(R.string.user_profile)),
                getString(R.string.user_name),
                getString(R.string.default_username));
    }
}