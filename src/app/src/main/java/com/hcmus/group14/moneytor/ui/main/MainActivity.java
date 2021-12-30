package com.hcmus.group14.moneytor.ui.main;

import static com.hcmus.group14.moneytor.firebase.FirebaseHelper.COLLECTION_USERS;
import static com.hcmus.group14.moneytor.ui.login.LoginActivity.FIRST_TIME_LOGIN;
import static com.hcmus.group14.moneytor.ui.login.LoginActivity.LOGIN_TYPE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.firebase.FirebaseHelper;
import com.hcmus.group14.moneytor.ui.analysis.AnalysisActivity;
import com.hcmus.group14.moneytor.ui.visualize.VisualizeActivity;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // TODO: skip update data for users who have logged in this phone before
        // TODO: show a loading circle while synchronizing data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString(LOGIN_TYPE);
            if (user != null && value.equals(FIRST_TIME_LOGIN)) {
                synchronizeData(user);
            }
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

    private void onOpenSetting(View view) {
        Intent settingIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingIntent);
    }

    public void onOpenVisualize(View view) {
        Intent intent = new Intent(this, VisualizeActivity.class);
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
            Button buttonSetting = (Button) findViewById(R.id.buttonSetting);
            onOpenSetting(buttonSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void synchronizeData(FirebaseUser user) {
        // Check if user already exist and has data on the cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(COLLECTION_USERS).document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Cloud data detected!");
                    builder.setPositiveButton("Retrieve progress",
                            (dialog, id) -> {
                                downloadUserPref(user);
                                FirebaseHelper.downloadData(user);
                            });
                    builder.setNegativeButton("Upload progress",
                            (dialog, id) -> {
                                uploadUserPref(user);
                                FirebaseHelper.uploadData(user);
                            });
                    builder.setNeutralButton("Do nothing",
                            (dialog, id) -> {
                                Toast.makeText(MainActivity.this,
                                        "Data is not uniform between cloud and local database",
                                        Toast.LENGTH_SHORT).show();
                            });
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Log.d(TAG, "No such user");
                    uploadUserPref(user);
                    FirebaseHelper.uploadData(user);
                }
            } else {
                Log.d(TAG, "Check user failed with ", task.getException());
            }
        });
    }

    private void downloadUserPref(FirebaseUser user) {
        // TODO: implement this
        FirebaseHelper.getUser(user, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    UserPref userPref = documentSnapshot.toObject(UserPref.class);
                    UserPref.saveToSharedPref(MainActivity.this, userPref);
                } else Log.d(TAG, "No such document");
            } else Log.d(TAG, "Get failed with ", task.getException());
        });
    }

    private void uploadUserPref(FirebaseUser user) {
        // Update user name
        String name = PreferenceUtils.getString(this,
                UserPref.USER_NAME, getString(R.string.default_username));
        String language = PreferenceUtils.getString(this,
                UserPref.USER_LANGUAGE, getString(R.string.default_username));
        String darkMode = PreferenceUtils.getString(this,
                UserPref.USER_DARK_MODE, "-1");
        int reminderInterval = PreferenceUtils.getInt(this,
                UserPref.USER_REMINDER_INTERVAL, 1);
        UserPref userPref = new UserPref(name, user.getUid(), user.getEmail(), language, darkMode
                , reminderInterval);
        FirebaseHelper.putUser(user, userPref);
    }
}