package com.hcmus.group14.moneytor.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpendingViewModel viewModel = new ViewModelProvider(this).get(SpendingViewModel.class);
        Spending spending = new Spending(1234, "Thanh", "no",
                "no des", 12312312312L);
        spending.setSpendingId(1);
//        viewModel.insertSpending(spending);
        viewModel.deleteSpending(spending);
    }

}