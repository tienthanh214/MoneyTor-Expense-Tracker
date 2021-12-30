package com.hcmus.group14.moneytor.ui.reminder;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.databinding.ActivityReminderBinding;
import com.hcmus.group14.moneytor.services.reminder.ReminderViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class ReminderActivity extends NoteBaseActivity<ActivityReminderBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityReminderBinding binding;
    private ReminderViewModel viewModel;
    private TimePicker timePicker;
    private Button saveButton;
    private SwitchCompat reminderSwitch;
    private int reminderHour;
    private int reminderMin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reminder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Daily reminder");
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        binding.setViewModel(viewModel);

        setReminderSwitchChecked();
        getReminderTime();
        setOnClickSaveButton();
    }

    private void getReminderTime() {
        timePicker = binding.timePicker;
        reminderHour = timePicker.getCurrentHour();
        reminderMin = timePicker.getCurrentMinute();
        Log.i("@@@ reminder hour", String.valueOf(reminderHour));
        Log.i("@@@ reminder min", String.valueOf(reminderMin));
    }

    private void setOnClickSaveButton() {
        saveButton = binding.buttonSaveReminderTime;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: call save reminder time from VM

                ReminderActivity.this.finish();
            }
        });
    }

    private void setReminderSwitchChecked() {
        reminderSwitch = binding.reminderSwitch;
        reminderSwitch.setOnCheckedChangeListener((c, value) -> {
            boolean preValue = PreferenceUtils.getBoolean(ReminderActivity.this,
                    "reminder", false);
            if (value && !preValue) {
                LinearLayout setReminderTime = binding.setReminderTime;
                setReminderTime.setVisibility(View.INVISIBLE);
            } else {
                // dump
            }
            PreferenceUtils.putBoolean(ReminderActivity.this, "reminder", value);
        });
    }
}