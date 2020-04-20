package com.example.sufya.draft.add_expenditure;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufya.draft.R;

import java.util.List;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

public class CategoriesListArrayAdapter extends ArrayAdapter<CategoriesActivity.Categories> {

    private final List<CategoriesActivity.Categories> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView tv_subCategory, tv_category;
        protected ImageView iv_categoryImage;
    }

    /**
     *
     * @param context passing values to activity
     * @param list List storing the information
     */
    public CategoriesListArrayAdapter(Activity context, List<CategoriesActivity.Categories> list) {
        super(context, R.layout.activity_categorylist_row, list);
        this.context = context;
        this.list = list;
    }

    /**
     *
     * @param position Position the item is clicked and where data is located
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_categorylist_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tv_category = (TextView) view.findViewById(R.id.tv_category);
            viewHolder.tv_subCategory = (TextView) view.findViewById(R.id.tv_subCategory);
            viewHolder.iv_categoryImage = (ImageView) view.findViewById(R.id.iv_categoryImage);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tv_subCategory.setText(list.get(position).getCategory());
        holder.tv_category.setText(list.get(position).getSubCategory());
        holder.iv_categoryImage.setImageDrawable(list.get(position).getSubCat());

        return view;
    }
}
