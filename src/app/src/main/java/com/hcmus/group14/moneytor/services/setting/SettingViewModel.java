package com.hcmus.group14.moneytor.services.setting;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.services.notification.receiver.GoalBroadcastReceiver;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class SettingViewModel extends AndroidViewModel {
    MutableLiveData<Boolean> widgetStatus;
    MutableLiveData<String> username;

    // login

    public SettingViewModel(@NonNull Application application) {
        super(application);
        widgetStatus = new MutableLiveData<>(PreferenceUtils.getBoolean(
                application.getApplicationContext(),
                "homescreen_widget", false));
        username = new MutableLiveData<>(PreferenceUtils.getString(
                application.getApplicationContext(),
                UserPref.USER_NAME,
                application.getString(R.string.default_username)));
    }

    public MutableLiveData<Boolean> getWidgetStatus() {
        return widgetStatus;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void onWidgetCheckedChange(Context context, boolean value) {
        if (value) {
            GoalBroadcastReceiver notification = new GoalBroadcastReceiver();
            notification.setupWidget(context, new SpendGoal());
        }
        PreferenceUtils.putBoolean(context, "homescreen_widget", value);
    }

    public void setWidgetStatus(boolean value) {
        widgetStatus.setValue(value);
    }

    public void setUsername(String username) {
        if (!username.isEmpty())
            this.username.setValue(username);
    }

    public boolean isLoggedIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null;
    }
}
