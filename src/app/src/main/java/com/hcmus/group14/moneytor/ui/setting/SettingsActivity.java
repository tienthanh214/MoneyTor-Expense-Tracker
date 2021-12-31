package com.hcmus.group14.moneytor.ui.setting;

import static com.hcmus.group14.moneytor.data.firebase.FirebaseHelper.COLLECTION_USERS;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.firebase.FirebaseHelper;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.databinding.ActivitySettingsBinding;
import com.hcmus.group14.moneytor.services.setting.SettingViewModel;
import com.hcmus.group14.moneytor.ui.login.LoginActivity;
import com.hcmus.group14.moneytor.ui.reminder.ReminderActivity;
import com.hcmus.group14.moneytor.utils.LanguageUtils;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getName();
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private ImageView ivPhoto;
    private SettingViewModel viewModel;
    private ActivitySettingsBinding binding;
    private TextView reminderSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup view model
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // TODO: Show profile picture and name
        prepareView();
        setPreferenceListener();
        displayUserInfo();
        setOnClickReminderSetting();
    }

    private void setOnClickReminderSetting() {
        binding.reminderSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this)
                .registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this)
                .unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }


    private void setPreferenceListener() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        sharedPreferenceChangeListener = (sharedPreferences1, key) -> {
            switch (key) {
                case UserPref.USER_NAME:
                    // TODO: apply all setting
                    // TODO: sync change to firestore
                    viewModel.setUsername(
                            PreferenceUtils.getString(this,
                                    UserPref.USER_NAME, getString(R.string.default_username)));
                    break;
                case UserPref.USER_DARK_MODE:
                    AppCompatDelegate.setDefaultNightMode(Integer.parseInt(
                            PreferenceUtils.getString(this,
                                    UserPref.USER_DARK_MODE, "-1")));
                    break;
                case UserPref.USER_WIDGET:
                    viewModel.setWidgetStatus(
                            PreferenceUtils.getBoolean(this,
                                    UserPref.USER_WIDGET, false));
                    break;
                case UserPref.USER_LANGUAGE:
                    LanguageUtils.setLocale(this, PreferenceUtils.getString(this,
                            UserPref.USER_LANGUAGE, "en"));
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private void prepareView() {
        ivPhoto = binding.ivPhoto;
        binding.btnLogout.setOnClickListener(v -> logout());
        // setup widget
        viewModel.getWidgetStatus().observe(this, aBoolean -> {
            viewModel.onWidgetCheckedChange(SettingsActivity.this, aBoolean);
        });
        binding.btnSync.setOnClickListener(
                v -> synchronizeData(FirebaseAuth.getInstance().getCurrentUser()));
    }

    private void displayUserInfo() {
        // Set user profile photo
        Uri photoUri = Uri.parse(PreferenceUtils.getString(this,
                UserPref.USER_PHOTO,
                String.format("android.resource://com.hcmus.group14.moneytor/%d",
                        R.drawable.account_circle_fill)));
        ivPhoto.setImageURI(photoUri);
        // login or logout
        if (!viewModel.isLoggedIn()) {
            binding.btnLogout.setText("Log in");
        }
    }

    void logout() {
        if (viewModel.isLoggedIn()) {
            AuthUI.getInstance()
                    .signOut(SettingsActivity.this)
                    .addOnCompleteListener(task -> backToLoginScreen());
        } else {
            backToLoginScreen();
        }
    }

    private void backToLoginScreen() {
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("Cloud data detected!");
                    builder.setPositiveButton("Retrieve progress",
                            (dialog, id) -> {
                                downloadUserPref(user);
                                viewModel.downloadData(user);
                            });
                    builder.setNegativeButton("Upload progress",
                            (dialog, id) -> {
                                uploadUserPref(user);
                                viewModel.uploadData(user);
                            });
                    builder.setNeutralButton("Do nothing",
                            (dialog, id) -> {
                                Toast.makeText(SettingsActivity.this,
                                        "Data is not uniform between cloud and local database",
                                        Toast.LENGTH_SHORT).show();
                            });
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Log.d(TAG, "No such user");
                    uploadUserPref(user);
                    viewModel.uploadData(user);
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
                    UserPref.saveToSharedPref(SettingsActivity.this, userPref);
                } else Log.d(TAG, "No such document");
            } else Log.d(TAG, "Get failed with ", task.getException());
        });
    }

//    private void applyUserPref() {
//        // Apply dark mode setting
//        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(
//                PreferenceUtils.getString(this,
//                        UserPref.USER_DARK_MODE, "-1")));
//        // Apply language setting
//        LanguageUtils.setLocale(this, PreferenceUtils.getString(this,
//                "user_language", "en"));
//    }

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
        boolean widget = PreferenceUtils.getBoolean(this,
                UserPref.USER_WIDGET, false);
        int reminder = PreferenceUtils.getInt(this,
                UserPref.USER_REMINDER, 1);
        UserPref userPref = new UserPref(name, user.getUid(), user.getEmail(), language, darkMode
                , reminderInterval, widget, reminder);
        FirebaseHelper.putUser(user, userPref);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}