package com.hcmus.group14.moneytor.ui.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.services.options.Category;
import com.hcmus.group14.moneytor.utils.CategoriesUtils;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> {

    private final List<Category> items;
    private final Context context;

    public CategoryAdapter(Context context, int resource, List objects) {
        super(context, resource, 0, objects);
        this.context = context;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);

        TextView textView = view.findViewById(R.id.item_category);
        ImageView iconImageView = view.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(items.get(position).getResourceId());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(items.get(position).getColor()));
        textView.setText(items.get(position).getName());

        return view;
    }

}

