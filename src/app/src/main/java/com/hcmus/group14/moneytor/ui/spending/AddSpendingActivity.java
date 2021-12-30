package com.hcmus.group14.moneytor.ui.spending;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.databinding.ActivityNoteSpendingBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.spending.SpendingDetailsViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.contact.ContactActivity;
import com.hcmus.group14.moneytor.ui.custom.CategoryAdapter;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.Calendar;
import java.util.List;

public class AddSpendingActivity extends NoteBaseActivity<ActivityNoteSpendingBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNoteSpendingBinding binding;
    private SpendingDetailsViewModel viewModel;
    private final int REQUEST_CODE_RELATE_CONTACT = 1234;
    private int spendingId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_note_spending;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.toolbar_goal_spending));
        binding = getViewDataBinding();

        viewModel = new ViewModelProvider(this).get(SpendingDetailsViewModel.class);
        binding.setViewModel(viewModel);

        spendingId = (int)getIntent().getIntExtra("spending_id", -1);
        if (spendingId != -1) {
            // if click on item list view, load full info of a spending
            viewModel.getSpendingWithRelatesById(spendingId).observe(this, spending -> {
                viewModel.uploadData(spending);
            });
        }
        setSpinner();
        setDatePickerDialog();
        setAddShareWith();
    }

    private void setAddShareWith() {
        EditText share = binding.editTextShareWith;
        share.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("type","spendingList");
        startActivityForResult(intent, REQUEST_CODE_RELATE_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_RELATE_CONTACT){
            if (resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                List<Relate> selectedContacts = (List<Relate>) bundle.getSerializable("contacts");
                viewModel.setRelates(selectedContacts);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (spendingId == -1) {
            menu.findItem(R.id.actionDelete).setEnabled(false);
            menu.findItem(R.id.actionDelete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                saveSpending();
                return true;
            case R.id.actionDelete:
                deleteSpending();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSpending() {
        boolean check = checkValidSpending();
        if (check){
            Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not a valid spending", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidSpending() {
        EditText cost = binding.inputAmount;
        InputUtils errors = viewModel.saveSpending();
        if (errors.hasError()){
            if (!errors.isValid(InputUtils.Type.COST))
                cost.setError("Amount is required!");
            return false;
        }
        return true;
    }

    private void deleteSpending() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteSpending();
                Toast.makeText(getApplicationContext(), "Spending deleted",
                        Toast.LENGTH_LONG).show();
                AddSpendingActivity.this.finish();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSpendingActivity.this,
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
        viewModel.setCategory(CategoriesUtils.getCategoryIdByPosition(position));
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}