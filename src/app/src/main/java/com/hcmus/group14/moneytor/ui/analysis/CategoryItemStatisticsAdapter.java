package com.hcmus.group14.moneytor.ui.analysis;

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
import com.hcmus.group14.moneytor.utils.InputUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CategoryItemStatisticsAdapter extends ArrayAdapter<String> {

    private final List<Category> items;
    private final Context context;

    // statistics data
    private HashMap<Category, Long> categoryDetail;

    public CategoryItemStatisticsAdapter(Context context, int resource, List objects) {
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
        final View view = LayoutInflater.from(context).inflate(R.layout.category_item_statistics,
                parent, false);

        TextView textView = view.findViewById(R.id.amount_stt);
        ImageView iconImageView = view.findViewById(R.id.icon_imageview_stt);

        Category category = items.get(position);
        iconImageView.setImageResource(category.getResourceId());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getColor()));
        // TODO: get category statistics from view model
        if (categoryDetail != null) {
            Long value = categoryDetail.get(category);
            if (value == null) value = 0L;
            textView.setText(InputUtils.getCurrency(value));
        }
        return view;
    }

    // function for update data
    void setItems(HashMap<Category, Long> categoriesDetails) {
        // if want to sort by value -> reorder items by value
        Collections.sort(items, (Category x, Category y) -> {
            Long valueX = categoriesDetails.get(x);
            Long valueY = categoriesDetails.get(y);
            if (valueX == null) valueX = 0L;
            if (valueY == null) valueY = 0L;
            return Long.compare(valueY, valueX);
        });
        this.categoryDetail = categoriesDetails;
        notifyDataSetChanged();
    }

}