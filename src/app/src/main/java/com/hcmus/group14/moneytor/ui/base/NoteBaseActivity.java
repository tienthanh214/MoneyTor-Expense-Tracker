package com.hcmus.group14.moneytor.ui.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class NoteBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    private T viewDataBinding;

    public abstract @LayoutRes int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    protected T getViewDataBinding() {
        return viewDataBinding;
    }
}
