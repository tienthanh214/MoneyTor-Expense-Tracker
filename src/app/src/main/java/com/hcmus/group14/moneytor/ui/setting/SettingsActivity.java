package com.hcmus.group14.moneytor.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.ui.login.LoginActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private TextView tvUsername;
    private Button btnLogout;

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
        displayUserInfo();
    }

    private void prepareView() {
        ivPhoto = findViewById(R.id.iv_photo);
        tvUsername = findViewById(R.id.tv_username);
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void displayUserInfo() {
        // Set user profile photo
        Uri photoUri = Uri.parse(PreferenceUtils.getString(
                PreferenceUtils.getInstance(this, "user_profile"),
                "user_name",
                "android.resource://com.hcmus.group14.moneytor/" + R.drawable.account_circle_fill
        ));
        ivPhoto.setImageURI(photoUri);
        // Set user name
        tvUsername.setText(PreferenceUtils.getString(
                PreferenceUtils.getInstance(this, "user_profile"),
                "user_name",
                getString(R.string.default_username)));
    }

    void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}