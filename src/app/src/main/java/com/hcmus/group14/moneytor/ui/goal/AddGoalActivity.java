package com.hcmus.group14.moneytor.ui.goal;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.databinding.ActivityGoalDetailsBinding;
import com.hcmus.group14.moneytor.services.goal.SpendGoalDetailsViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.custom.CategoryAdapter;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.Calendar;
import java.util.List;

public class AddGoalActivity extends NoteBaseActivity<ActivityGoalDetailsBinding> implements AdapterView.OnItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGoalDetailsBinding binding;
    private SpendGoalDetailsViewModel viewModel;
    private SpendGoal goal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goal_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.toolbar_title_goal));
        binding = getViewDataBinding();

        viewModel = new ViewModelProvider(this).get(SpendGoalDetailsViewModel.class);
        binding.setViewModel(viewModel);

        goal = (SpendGoal)getIntent().getSerializableExtra("goal_obj");
        if (goal != null) {
            // if click on item list view, load full info of a goal
            viewModel.uploadData(goal);
        }

        setSpinner();
        setDatePickerDialog();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (goal == null) {
            menu.findItem(R.id.actionDelete).setEnabled(false);
            menu.findItem(R.id.actionDelete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.actionSave) {
            saveGoal();
            return true;
        } else if (itemId == R.id.actionDelete) {
            deleteGoal();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGoal() {
        boolean check = checkValidGoal();
        if (check){
            Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not a valid spending", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidGoal() {
        EditText cost = binding.inputAmount;
        InputUtils errors = viewModel.saveSpendGoal();
        if (errors.hasError()){
            cost.setError("Amount is required!");
            return false;
        }
        return true;
    }

    private void deleteGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteGoal();
                Toast.makeText(getApplicationContext(), "Goal deleted",
                        Toast.LENGTH_LONG).show();
                AddGoalActivity.this.finish();
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

        final List<Category> categories = CategoriesUtils.getDefaultCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,
                R.layout.category_item, categories);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i("@@@ item selected", position + "");
        viewModel.setCategory(CategoriesUtils.getCategoryIdByPosition(position));
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}