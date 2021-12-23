package com.hcmus.group14.moneytor.ui.setting;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {
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
        displayUserInfo();
    }

    private void displayUserInfo() {
        ivPhoto = findViewById(R.id.iv_photo);
        tvUsername = findViewById(R.id.tv_username);
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
                "New user"));
    }

    void logout(View view) {
        //FirebaseAuth.getInstance().signOut();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}