package com.example.didyouknow.models;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryModel> {
    private CategoryModel[] categories;

    public CategorySpinnerAdapter(Context context, CategoryModel[] categories) {
        super(context, android.R.layout.simple_spinner_item, categories);
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public long getItemId(int position) {
        return categories[position].id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(categories[position].name);
        textView.setTextColor(Color.WHITE);

        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(categories[position].name);
        textView.setHeight(130);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        return textView;
    }
}
