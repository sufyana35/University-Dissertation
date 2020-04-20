package com.example.sufya.draft.recycler_Views.expandable;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sufya.draft.R;
import com.example.sufya.draft.recycler_View_Models.expandable_item;
import com.example.sufya.draft.recycler_View_Models.expandable_section;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @Author bpncool. (2016). .
 * @website https://github.com/bpncool/SectionedExpandableGridRecyclerView/tree/master/app/src/main/res/layout
 *
 * Modfied Function onBindViewHolder and class ViewHolder
 */

public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_item; //TODO : change this

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position)?gridLayoutManager.getSpanCount():1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof expandable_section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM :
                final expandable_item item = (expandable_item) mDataArrayList.get(position);
                holder.itemTextView.setText(item.getDescription_expense());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(item);
                    }
                });

                final String url = "http://www.moblet.club/moblet/images/" + item.getDescription_expense().toLowerCase().replaceAll("\\s", "") + ".jpg"; //format url
                Picasso.with(mContext).load(url).fit().into(holder.IV_card_category_expense, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

                break;
            case VIEW_TYPE_SECTION :
                final expandable_section section = (expandable_section) mDataArrayList.get(position);
                holder.sectionTextView.setText(section.getCategory());
                holder.sectionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(section);
                    }
                });
                holder.sectionToggleButton.setChecked(section.isExpanded);
                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });
                holder.sectionTV_card_category_grouped_text.setText(section.getGrouped_expense());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        TextView sectionTextView;
        TextView sectionTV_card_category_grouped_text;
        ToggleButton sectionToggleButton;

        //for item
        TextView itemTextView;
        ImageView IV_card_category_expense;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemTextView = (TextView) view.findViewById(R.id.TV_card_category_expense_description);
                IV_card_category_expense = (ImageView) itemView.findViewById(R.id.IV_card_category_expense);
            } else {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);
                sectionToggleButton = (ToggleButton) view.findViewById(R.id.IV_card_category);
                sectionTV_card_category_grouped_text = (TextView) view.findViewById(R.id.TV_card_category_grouped_text);
            }
        }
    }
}
