package com.hcmus.group14.moneytor.ui.reminder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.databinding.ActivityReminderBinding;
import com.hcmus.group14.moneytor.services.reminder.ReminderViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

public class ReminderActivity extends NoteBaseActivity<ActivityReminderBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityReminderBinding binding;
    private ReminderViewModel viewModel;

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
        TimePicker timePicker = binding.timePicker;
        int reminderHour = timePicker.getCurrentHour();
        int reminderMin = timePicker.getCurrentMinute();
        Log.i("@@@ reminder hour", String.valueOf(reminderHour));
        Log.i("@@@ reminder min", String.valueOf(reminderMin));
    }

    private void setOnClickSaveButton() {
        Button saveButton = binding.buttonSaveReminderTime;
        saveButton.setOnClickListener(view -> {
            viewModel.saveReminder(ReminderActivity.this);
//                ReminderActivity.this.finish();
        });
    }

    private void setReminderSwitchChecked() {
        SwitchCompat reminderSwitch = binding.reminderSwitch;
        viewModel.getReminderStatus().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.setReminderTime.setVisibility(View.INVISIBLE);
            } else {
                binding.setReminderTime.setVisibility(View.VISIBLE);
            }
        });
    }
}