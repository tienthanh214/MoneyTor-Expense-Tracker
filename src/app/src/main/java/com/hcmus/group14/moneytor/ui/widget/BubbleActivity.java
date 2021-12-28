package com.hcmus.group14.moneytor.ui.widget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.databinding.ActivityBubbleBinding;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.services.spending.SpendingDetailsViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.contact.ContactActivity;
import com.hcmus.group14.moneytor.ui.custom.CategoryAdapter;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;
import com.hcmus.group14.moneytor.utils.DateTimeUtils;
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.Calendar;
import java.util.List;

public class BubbleActivity extends NoteBaseActivity<ActivityBubbleBinding> {
    private final int REQUEST_CODE_RELATE_CONTACT = 4123;

    private SpendingDetailsViewModel viewModel;
    private ActivityBubbleBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bubble;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this).get(SpendingDetailsViewModel.class);
        binding.noteSpending.setViewModel(viewModel);

        setSpinner();
        setDatePickerDialog();
        setAddShareWith();

        binding.bubbleSave.setOnClickListener(v -> saveSpending());

        binding.bubbleCancel.setOnClickListener(v -> cancelSpending());
    }

    private void setAddShareWith() {
        EditText share = binding.noteSpending.editTextShareWith;
        share.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivityForResult(intent, REQUEST_CODE_RELATE_CONTACT);
        });
    }

    private void setDatePickerDialog() {
        EditText date = binding.noteSpending.editTextDate;

        date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BubbleActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private void setSpinner() {
        Spinner spinner = binding.noteSpending.spinnerCategory;
//        spinner.setOnItemSelectedListener(this);

        final List<Category> categories = CategoriesUtils.getDefaultCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,
                R.layout.category_item, categories);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
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

    private void saveSpending() {
        boolean check = checkValidSpending();
        if (check){
            Toast.makeText(getApplicationContext(), "Spending saved", Toast.LENGTH_SHORT).show();
            cancelSpending();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not a valid spending", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidSpending() {
        InputUtils errors = viewModel.saveSpending();
        if (errors.hasError()){
            if (!errors.isValid(InputUtils.Type.COST))
                binding.noteSpending.inputAmount.setError("Amount is required!");
            return false;
        }
        return true;
    }

    private void cancelSpending() {
        viewModel.setCategory(Category.OTHERS.getId());
        viewModel.setRelates(null);
        viewModel.setTitle("");
        viewModel.setDescription("");
        viewModel.setCost(0);
        viewModel.setDate(-1);
    }
}