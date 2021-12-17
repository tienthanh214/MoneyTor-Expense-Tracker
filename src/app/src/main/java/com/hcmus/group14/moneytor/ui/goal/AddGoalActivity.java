package com.hcmus.group14.moneytor.ui.goal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.DatePickerDialog;
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
import com.hcmus.group14.moneytor.databinding.ActivityGoalSpendingBinding;
import com.hcmus.group14.moneytor.databinding.ActivityNoteSpendingBinding;
import com.hcmus.group14.moneytor.databinding.ActivitySpendingBinding;
import com.hcmus.group14.moneytor.services.goal.SpendGoalViewModel;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.spending.AddSpendingActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddGoalActivity extends NoteBaseActivity<ActivityGoalSpendingBinding> implements AdapterView.OnItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGoalSpendingBinding binding;
    private SpendGoalViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goal_spending;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal_spending);
        viewModel = binding.getViewModel();
        this.setTitle("Spending goal");
        setSpinner();
        setDatePickerDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                // insert spending
                Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.actionDelete:
                // delete spending
                Toast.makeText(getApplicationContext(), "Spending deleted", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGoalActivity.this,
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