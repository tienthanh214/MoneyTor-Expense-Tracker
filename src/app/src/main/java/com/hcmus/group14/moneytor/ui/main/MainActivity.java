package com.hcmus.group14.moneytor.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.ui.debtlend.DebtLendActivity;
import com.hcmus.group14.moneytor.ui.goal.GoalActivity;
import com.hcmus.group14.moneytor.ui.spending.SpendingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onOpenSpendingList(View view) {
        Intent intent = new Intent(this, SpendingActivity.class);
        startActivity(intent);
    }

    public void onOpenSpendingGoal(View view){
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
    }

    public void opOpenDebtLend(View view){
        Intent intent = new Intent(this, DebtLendActivity.class);
        startActivity(intent);
    }
}