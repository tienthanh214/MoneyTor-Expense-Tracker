package com.hcmus.group14.moneytor.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.hcmus.group14.moneytor.data.model.Relate;

import com.hcmus.group14.moneytor.data.model.Spending;
import com.hcmus.group14.moneytor.databinding.ActivityContactBinding;


import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.spending.SpendingViewModel;
import com.hcmus.group14.moneytor.ui.base.NoteBaseActivity;
import com.hcmus.group14.moneytor.utils.ContactUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends NoteBaseActivity<ActivityContactBinding> {

    private AppBarConfiguration appBarConfiguration;
    private ActivityContactBinding binding;
    private SpendingViewModel spendingViewModel;
    private List<Relate> contacts;
    private ContactAdapter contactAdapter;
    private Context context;
    private SearchView searchView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        this.context = this.getApplicationContext();
        this.setTitle(getString(R.string.toolbar_title_relate));
        contacts = ContactUtils.getAllContacts(this, this.getApplicationContext());
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        initializeViews(type);
    }

    private void initializeViews(String type) {
        int limit = type.equals("spendingList") ? 99999 : 1;
        contactAdapter = new ContactAdapter(this, contacts, findViewById(R.id.selectedContact), limit);
        binding.contactList.setAdapter(contactAdapter);
        binding.contactList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Relate> filter(String text) {
        List<Relate> filteredList = new ArrayList<>();
        for (Relate item : contacts) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                contactAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contactAdapter.filterList(filter(s));
                return false;
            }
        });
        MenuItem doneButton = menu.findItem(R.id.actionConfirm);
        doneButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<Relate> selectedContacts= contactAdapter.getSelectedContacts();
                Intent replyIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("contacts", (Serializable) selectedContacts);
                replyIntent.putExtras(bundle);
                setResult(RESULT_OK, replyIntent);
                finish();
                return true;
            }
        });
        return true;

    }
}