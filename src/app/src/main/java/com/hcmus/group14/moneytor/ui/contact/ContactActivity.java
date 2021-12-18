package com.hcmus.group14.moneytor.ui.contact;

import android.content.Context;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;

import androidx.navigation.ui.AppBarConfiguration;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.hcmus.group14.moneytor.data.model.Relate;

import com.hcmus.group14.moneytor.databinding.ActivityContactBinding;


import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;


import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends NoteBaseActivity<ActivityContactBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityContactBinding binding;
    private SpendingViewModel spendingViewModel;
    private List<Relate> contacts;
    private ContactAdapter contactAdapter;
    private Context context;


    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        this.context = this.getApplicationContext();
        // do what you want
        this.setTitle("Relate");
        contacts = getData();
        initializeViews();
    }

    List<Relate> getData() {
        List<Relate> data = new ArrayList<>();
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        data.add(new Relate("Hieu","01234556789"));
        return data;
    }

    private void initializeViews() {
        contactAdapter = new ContactAdapter(this, contacts);
        binding.contactList.setAdapter(contactAdapter);
        binding.contactList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        //MenuItem searchItem = menu.findItem(R.id.actionSearch);
        return super.onCreateOptionsMenu(menu);
    }
}