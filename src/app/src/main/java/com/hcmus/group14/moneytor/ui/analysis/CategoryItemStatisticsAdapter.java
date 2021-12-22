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
import com.hcmus.group14.moneytor.services.analyze.AnalyzeViewModel;
import com.hcmus.group14.moneytor.services.options.Category;

import java.util.List;

public class CategoryItemStatisticsAdapter extends ArrayAdapter<String> {

    private final List<Category> items;
    private final Context context;
    private final AnalyzeViewModel viewModel;

    public CategoryItemStatisticsAdapter(Context context, int resource, List objects, AnalyzeViewModel viewModel) {
        super(context, resource, 0, objects);
        this.context = context;
        items = objects;
        this.viewModel = viewModel;
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

        iconImageView.setImageResource(items.get(position).getResourceId());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(items.get(position).getColor()));
        textView.setText("X.XXX.XXX");
        //textView.setText(viewModel.getDetailsForCategories());
        return view;
    }

}