package com.hcmus.group14.moneytor.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.hcmus.group14.moneytor.ui.login.LoginActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private ImageView ivPhoto;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
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
            if (key.equals("user_name")) {
                tvUsername.setText(PreferenceUtils.getString(this,
                        UserPref.USER_NAME,
                        getString(R.string.default_username)));
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private void prepareView() {
        ivPhoto = findViewById(R.id.iv_photo);
        tvUsername = findViewById(R.id.tv_username);
        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void displayUserInfo() {
        // Set user profile photo
        Uri photoUri = Uri.parse(PreferenceUtils.getString(this,
                UserPref.USER_PHOTO,
                String.format("android.resource://com.hcmus.group14.moneytor/%d",
                        R.drawable.account_circle_fill)));
        ivPhoto.setImageURI(photoUri);
        // Set user name
        tvUsername.setText(PreferenceUtils.getString(this,
                UserPref.USER_NAME,
                getString(R.string.default_username)));
    }

    void logout() {
        AuthUI.getInstance()
                .signOut(SettingsActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
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