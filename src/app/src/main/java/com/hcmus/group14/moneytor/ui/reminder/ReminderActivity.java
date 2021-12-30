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
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private SwitchCompat reminderSwitch;
    private LinearLayout setReminderTime;

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: call save reminder time from VM

                Toast.makeText(getApplicationContext(), "Set reminder successfully!",
                        Toast.LENGTH_SHORT).show();
                ReminderActivity.this.finish();
            }
        });
    }

    private void setReminderSwitchChecked() {
        reminderSwitch = binding.reminderSwitch;
        reminderSwitch.setOnCheckedChangeListener((c, value) -> {
            setReminderTime = binding.setReminderTime;
            if (value == false) {
                setReminderTime.setVisibility(View.INVISIBLE);
            } else {
                openDialog();
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
                setReminderTime.setVisibility(View.VISIBLE);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                reminderSwitch.setChecked(false);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}