package com.hcmus.group14.moneytor.ui.spending;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

public class SpendingActivity extends NoteBaseActivity<ActivitySpendingBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySpendingBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_spending;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        // do what you want
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}