package com.hcmus.group14.moneytor.ui.reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.databinding.ActivityReminderBinding;
import com.hcmus.group14.moneytor.services.reminder.ReminderViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.spending.AddSpendingActivity;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class ReminderActivity extends NoteBaseActivity<ActivityReminderBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityReminderBinding binding;
    private ReminderViewModel viewModel;
    private Button saveButton;
    private boolean hasTurnOnReminder = false;

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
        setOnClickSaveButton();
    }

    private void setOnClickSaveButton() {
        saveButton = binding.buttonSaveReminderTime;
        saveButton.setOnClickListener(view -> {
            // TODO: call save reminder time from VM
            viewModel.saveReminder(ReminderActivity.this);
            Toast.makeText(getApplicationContext(), "Set reminder successfully!",
                    Toast.LENGTH_SHORT).show();
            ReminderActivity.this.finish();
        });
    }

    private void setReminderSwitchChecked() {
        viewModel.getReminderStatus().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.setReminderTime.setVisibility(View.INVISIBLE);
                viewModel.dismissReminder(ReminderActivity.this);
            } else {
                if (!hasTurnOnReminder && !viewModel.hasAccept()) {
                    openDialog();
                } else {
                    binding.setReminderTime.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Allow this app to send notifications to your device?");
        builder.setTitle("");
        builder.setCancelable(false);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.setReminderTime.setVisibility(View.VISIBLE);
                hasTurnOnReminder = true;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                binding.reminderSwitch.setChecked(false);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}