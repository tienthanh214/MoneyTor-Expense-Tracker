package com.hcmus.group14.moneytor.ui.contact;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.Relate;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Relate> contacts;
    private Context context;

    public ContactAdapter(Context context, List<Relate> contactsList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contacts = contactsList;
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
        holder.setName(currentContact.getName());
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
        }

        @Override
        public void onClick(View v) {
            Drawable img = context.getResources().getDrawable(R.drawable.ic_done);
            ((TextView)(v.findViewById(R.id.nameContactItem))).setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
    }
}
