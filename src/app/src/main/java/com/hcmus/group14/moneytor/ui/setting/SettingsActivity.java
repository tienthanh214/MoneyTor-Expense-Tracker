package com.hcmus.group14.moneytor.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.databinding.ActivitySettingsBinding;
import com.hcmus.group14.moneytor.services.setting.SettingViewModel;
import com.hcmus.group14.moneytor.ui.login.LoginActivity;
import com.hcmus.group14.moneytor.utils.LanguageUtils;
import com.hcmus.group14.moneytor.ui.reminder.ReminderActivity;
import com.hcmus.group14.moneytor.utils.NotificationUtils;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private ImageView ivPhoto;
    private SettingViewModel viewModel;
    private ActivitySettingsBinding binding;
    private TextView tvUsername;
    private SwitchCompat widgetSwitch;
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
        // Show profile picture and name
        prepareView();
        setPreferenceListener();
        displayUserInfo();
        setOnClickReminderSetting();
    }

    private void setOnClickReminderSetting() {
        reminderSetting = findViewById(R.id.reminderSetting);
        reminderSetting.setOnClickListener(new View.OnClickListener() {
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
                case "user_name":
                    // TODO: sync change to firestore
                    viewModel.setUsername(
                            PreferenceUtils.getString(this,
                                    UserPref.USER_NAME, getString(R.string.default_username)));
                    break;
                case "user_dark_mode":
                    AppCompatDelegate.setDefaultNightMode(Integer.parseInt(
                            PreferenceUtils.getString(this,
                                    UserPref.USER_DARK_MODE, "-1")));
                    break;
                case "homescreen_widget":
                    viewModel.setWidgetStatus(
                            PreferenceUtils.getBoolean(this,
                                    "homescreen_widget", false));
                    break;
                case "user_language":
                    LanguageUtils.setLocale(this, PreferenceUtils.getString(this,
                            "user_language", "en"));
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
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            backToLoginScreen();
                        }
                    });
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference namePref = findPreference("name");
            ListPreference languagePref = findPreference("language");
            SwitchPreference darkModePref = findPreference("dark_mode");
        }
    }
}