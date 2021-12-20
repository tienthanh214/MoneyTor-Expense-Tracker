package com.hcmus.group14.moneytor.ui.debtlend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.databinding.ActivityDebtLendDetailsBinding;
import com.hcmus.group14.moneytor.services.debtlend.DebtLendDetailsViewModel;
import com.hcmus.group14.moneytor.services.goal.SpendGoalDetailsViewModel;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDebtLendActivity extends NoteBaseActivity<ActivityDebtLendDetailsBinding> implements AdapterView.OnItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDebtLendDetailsBinding binding;
    private DebtLendDetailsViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_lend_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(DebtLendDetailsViewModel.class);
        viewModel = binding.getViewModel();
        this.setTitle("Manage debt and lend");
        setSpinner();
        setDatePickerDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                save();
                return true;
            case R.id.actionDelete:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        boolean check = checkValid();
        if (check){
            Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not a valid spending", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValid() {
        EditText cost = binding.inputAmount;
        //InputUtils errors = viewModel.saveGoal();
        //if (errors.hasError()){
        if (cost.length() == 0){
            // if error type is cost
            cost.setError("Amount of spending is required!");
            // if error type is category
            // cost.setError("Category of spending is required!");
            return false;
        }
        return true;
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // view model delete spending
                Toast.makeText(getApplicationContext(), "Spending deleted",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_spending_menu, menu);

        return true;
    }

    private void setDatePickerDialog() {
        EditText date = binding.editTextDate;
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDebtLendActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {
        Spinner spinner = binding.spinnerCategory;

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("cat1");
        categories.add("cat2");
        categories.add("cat3");
        categories.add("cat4");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}