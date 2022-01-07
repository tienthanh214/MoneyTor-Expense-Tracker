package com.hcmus.group14.moneytor.ui.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.hcmus.group14.moneytor.data.model.FilterState;
import com.hcmus.group14.moneytor.services.options.FilterViewModel;

import java.util.List;

public abstract class NoteBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    private T viewDataBinding;
    protected FilterViewModel filterViewModel;

    public abstract @LayoutRes int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.executePendingBindings();
    }

    protected T getViewDataBinding() {
        return viewDataBinding;
    }

    public void setFilter(List<String> cats, String period) {
        if (filterViewModel != null) {
            filterViewModel.setFilterState(new FilterState(cats, period));
        }
    }
}
