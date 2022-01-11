package com.hcmus.group14.moneytor.ui.debtlend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.databinding.ActivityDebtLendDetailsBinding;
import com.hcmus.group14.moneytor.services.debtlend.DebtLendDetailsViewModel;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.contact.ContactActivity;
import com.hcmus.group14.moneytor.ui.custom.CategoryAdapter;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.Calendar;
import java.util.List;

public class AddDebtLendActivity extends NoteBaseActivity<ActivityDebtLendDetailsBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final int REQUEST_CODE_RELATE_CONTACT = 1234;
    private ActivityDebtLendDetailsBinding binding;
    private DebtLendDetailsViewModel viewModel;
    private int debtLendId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_lend_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        this.setTitle(getString(R.string.toolbar_title_debtlend));

        viewModel = new ViewModelProvider(this).get(DebtLendDetailsViewModel.class);
        binding.setViewModel(viewModel);

        debtLendId = (int) getIntent().getIntExtra("debt_id", -1);
        if (debtLendId != -1) {
            // if click on item list view, load full info of a spending
            viewModel.getDebtLendAndRelateById(debtLendId).observe(this, debtLend -> {
                viewModel.uploadData(debtLend);
            });
        }

        setSpinner();
        setDatePickerDialog();
        setAddTarget();
    }

    private void setAddTarget() {
        EditText target = binding.editTextTarget;
        target.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        // TODO: add option to choose only one contact
        intent.putExtra("type","debtLend");
        startActivityForResult(intent, REQUEST_CODE_RELATE_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_RELATE_CONTACT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                List<Relate> selectedContacts = (List<Relate>) bundle.getSerializable("contacts");
                // bug here
                viewModel.setTarget(selectedContacts.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (debtLendId == -1) {
            menu.findItem(R.id.actionDelete).setEnabled(false);
            menu.findItem(R.id.actionDelete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.actionSave) {
            save();
            return true;
        } else if (itemId == R.id.actionDelete) {
            delete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        boolean check = checkValid();
        if (check) {
            Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Not a valid spending", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValid() {
        EditText cost = binding.inputAmount;
        InputUtils errors = viewModel.saveDebtLend();
        if (errors.hasError()) {
            if (!errors.isValid(InputUtils.Type.COST))
                cost.setError("Amount is required!");
            return false;
        }
        return true;
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            viewModel.deleteDebtLend();
            Toast.makeText(getApplicationContext(), "Debt/Lend deleted",
                    Toast.LENGTH_LONG).show();
            AddDebtLendActivity.this.finish();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

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
        date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddDebtLendActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
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