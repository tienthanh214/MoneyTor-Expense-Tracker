package com.hcmus.group14.moneytor.ui.contact;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Spending;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Relate> contacts;
    private List<Relate> selectedContacts;
    private List<String> selectedString;
    private Context context;
    private TextView selectedTextView;
    private int limitSelect;

    public List<Relate> getSelectedContacts(){
        return this.selectedContacts;
    }

    public ContactAdapter(Context context, List<Relate> contactsList, TextView selectedData,int limit) {
        selectedTextView = selectedData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contacts = contactsList;
        this.selectedContacts = new ArrayList<Relate>();
        this.selectedString = new ArrayList<String>();
        this.limitSelect = limit;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.content_contact, parent, false);
        ContactViewHolder holder = new ContactViewHolder(itemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Relate currentContact = contacts.get(position);
        holder.setPhone(currentContact.getTel());
        String name = currentContact.getName();
        holder.setName(name);
        if(selectedString.contains(name)){
            holder.toggleDoneIcon(true);
        }
        else holder.toggleDoneIcon(false);
    }

    public void filterList(List<Relate> filteredList) {
        contacts = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null) return contacts.size();
        else return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ContactAdapter adapter;
        private TextView nameView;
        private TextView phoneView;

        public void setName(String name) {
            nameView.setText(name);
        }

        public void setPhone(String phone) {
            phoneView.setText(phone);
        }

        public ContactViewHolder(@NonNull View itemView, ContactAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.nameView = itemView.findViewById(R.id.nameContactItem);
            this.phoneView = itemView.findViewById(R.id.phoneNumberContactItem);
            itemView.setOnClickListener(this);
        }

        void toggleDoneIcon(boolean isDone) {
            if(isDone) nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
            else  nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Relate currentContact = contacts.get(position);
            String currentName = currentContact.getName();
            if (selectedString.contains(currentName)) {
                selectedContacts.remove(currentContact);
                selectedString.remove(currentName);
                toggleDoneIcon(false);
            } else {
                if(selectedString.size()<limitSelect) {
                    selectedContacts.add(currentContact);
                    selectedString.add(currentName);
                    toggleDoneIcon(true);
                }
            }
            selectedTextView.setText(String.join(", ", selectedString));
        }
    }
}
