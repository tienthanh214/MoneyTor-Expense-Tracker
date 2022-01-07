package com.hcmus.group14.moneytor.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.ui.analysis.AnalysisActivity;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.ui.debtlend.DebtLendActivity;
import com.hcmus.group14.moneytor.ui.goal.GoalActivity;
import com.hcmus.group14.moneytor.ui.spending.SpendingActivity;
import com.hcmus.group14.moneytor.ui.visualize.VisualizeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterSelectUtils {
    private List<String> selectedCategories;
    private List<String> selectedCategoriesString;
    private final List<Category> allCategories = CategoriesUtils.getDefaultCategories();
    final private HashMap<String, Category> stringToCategory;
    private final String[] timePeriods = FilterState.timePeriods;
    private String selectedPeriod;
    private final Context context;
    AlertDialog mainDialogInstance;
    AlertDialog periodDialogInstance;
    AlertDialog categoriesDialogInstance;

    public FilterSelectUtils(Context parentContext) {
        selectedPeriod = "";
        selectedCategories = new ArrayList<>();
        selectedCategoriesString = new ArrayList<>();
        stringToCategory = new HashMap<>();
        for (Category category : allCategories) {
            stringToCategory.put(category.getName(), category);
        }
        context = parentContext;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public List<String> getSelectedCategoriesString() {
        return selectedCategoriesString;
    }

    public String getSelectedPeriod() {
        return selectedPeriod;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AlertDialog createCategoriesDialog(View parentView) {
        if(categoriesDialogInstance!=null) return categoriesDialogInstance;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.filter_select_theme);
        View v = LayoutInflater.from(context).inflate(R.layout.filter_select_2, null);
        builder.setView(v);
        builder.setCancelable(false);
        Button confirmBtn = v.findViewById(R.id.confirmBtn);
        Button cancelBtn = v.findViewById(R.id.cancelBtn);
        LinearLayout checkboxGroup = v.findViewById(R.id.groupOfSelection);
        for (Category category : allCategories) {
            CheckBox cb = new CheckBox(context);
            cb.setText(category.getName());
            cb.setTextColor(Color.parseColor("#ffffff"));
            if (selectedCategoriesString.contains(category.getName())) cb.setChecked(true);
            checkboxGroup.addView(cb);
        }
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(v12 -> {
            TextView tv = parentView.findViewById(R.id.categoriesSelect);
            List<String> newSelectedString = new ArrayList<>();
            List<String> newSelectedCategory = new ArrayList<>();
            for (int i = 0; i < checkboxGroup.getChildCount(); i++) {
                CheckBox check = (CheckBox) checkboxGroup.getChildAt(i);
                if (check.isChecked()) {
                    String temp = check.getText().toString();
                    newSelectedString.add(temp);
                    newSelectedCategory.add(stringToCategory.get(temp).getId());
                }
            }
            selectedCategories = newSelectedCategory;
            selectedCategoriesString = newSelectedString;
            if (selectedCategoriesString.size() == 0) tv.setText("Tap to select categories");
            else tv.setText(String.join(", ", selectedCategoriesString));
            alertDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v1 -> alertDialog.dismiss());
        categoriesDialogInstance=alertDialog;
        return alertDialog;
    }

    public AlertDialog createPeriodsDialog(View parentView) {
        if(periodDialogInstance!=null) return periodDialogInstance;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.filter_select_theme);
        View v = LayoutInflater.from(context).inflate(R.layout.filter_select_2, null);
        builder.setView(v);
        builder.setCancelable(false);
        Button confirmBtn = v.findViewById(R.id.confirmBtn);
        Button cancelBtn =  v.findViewById(R.id.cancelBtn);
        LinearLayout wrapper =  v.findViewById(R.id.groupOfSelection);
        RadioGroup radioGroup = new RadioGroup(context);
        for (String timePeriod : timePeriods) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(timePeriod);
            radioButton.setTextColor(Color.parseColor("#ffffff"));
            radioGroup.addView(radioButton);
            if (timePeriod.equals(selectedPeriod)) radioButton.setChecked(true);
        }
        wrapper.addView(radioGroup);
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(v1 -> {
            TextView tv =  parentView.findViewById(R.id.periodSelect);
            for (int i = 0; i < timePeriods.length; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (radioButton.isChecked()) selectedPeriod = radioButton.getText().toString();
            }
            if (selectedPeriod.equals("")) tv.setText("Tap to select period");
            else tv.setText(selectedPeriod);
            alertDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v12 -> alertDialog.dismiss());
        periodDialogInstance=alertDialog;
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AlertDialog createMainDialog() {
        if(mainDialogInstance!=null) return  mainDialogInstance;
        AlertDialog.Builder builder;
        selectedCategories = new ArrayList<>();
        selectedCategoriesString = new ArrayList<>();
        builder = new AlertDialog.Builder(context, R.style.filter_select_theme);
        View v = LayoutInflater.from(context).inflate(R.layout.filter_select, null);
        Button confirmBtn =  v.findViewById(R.id.confirmBtn);
        Button cancelBtn =  v.findViewById(R.id.cancelBtn);
        TextView categoriesSelect =  v.findViewById(R.id.categoriesSelect);
        TextView periodSelect =  v.findViewById(R.id.periodSelect);
        builder.setView(v);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        confirmBtn.setOnClickListener(v1 -> {
            // filter data lại từ selectedCategories
            if(context instanceof SpendingActivity){
//                ((SpendingActivity) context).testFilter();
                Log.d("aaaaa","spending");
                // gọi hàm filter public ở class tương ứng truyền category ở scope này vào.
            }
            if(context instanceof GoalActivity){
                Log.d("aaaaa","goal");
            }
            if(context instanceof DebtLendActivity){
                Log.d("aaaaa","debtLend");
            }
            if(context instanceof AnalysisActivity){
                Log.d("aaaaa","analysis");
            }
            if(context instanceof VisualizeActivity){
                Log.d("aaaaa","visualize");
            }


            ((NoteBaseActivity<ViewDataBinding>)context).setFilter(getSelectedCategories(), getSelectedPeriod());

            alertDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v14 -> alertDialog.dismiss());
        categoriesSelect.setOnClickListener(v13 -> {
            AlertDialog categoriesDialog = createCategoriesDialog(v13);
            categoriesDialog.show();
        });
        periodSelect.setOnClickListener(v12 -> {
            AlertDialog periodDialog = createPeriodsDialog(v12);
            periodDialog.show();
        });
        mainDialogInstance=alertDialog;
        return alertDialog;
    }
}
