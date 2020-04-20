package com.example.sufya.draft.recycler_Views;

import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufya.draft.recycler_View_Models.expense_data;
import com.example.sufya.draft.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by sufyan Ahmed on 02/03/2017.
 */

/**
 *Recyclerview expense list adapter
 */
public class Recycler_View_Adapter_Expense_List extends RecyclerView.Adapter<Recycler_View_Adapter_Expense_List.View_Holder> {

    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView description;
        TextView price;
        TextView category;
        TextView subCategory;
        TextView timestamp;
        ImageView imageView;
        TextView id;

        //initialise views
        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.expenseCardView);
            category = (TextView) itemView.findViewById(R.id.card_expense_list_category);
            subCategory = (TextView) itemView.findViewById(R.id.card_expense_list_subCategory);
            description = (TextView) itemView.findViewById(R.id.card_expense_list_description);
            price = (TextView) itemView.findViewById(R.id.card_expense_list_price);
            timestamp = (TextView) itemView.findViewById(R.id.card_expense_list_timestamp);
            imageView = (ImageView) itemView.findViewById(R.id.card_expense_list_image);
            id = (TextView) itemView.findViewById(R.id.card_expense_list_id);
        }

    }

    List<expense_data> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter_Expense_List(List<expense_data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expense_list, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        String cleanString = list.get(position).price.replaceAll("[£,.]", "");

        double parsed = Double.parseDouble(cleanString);
        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.description.setText(list.get(position).description);
        holder.price.setText(formatted);
        holder.category.setText(list.get(position).category);
        holder.subCategory.setText(list.get(position).subCategory);
        holder.id.setText(list.get(position).id);
        holder.timestamp.setText(list.get(position).timestamp);

        //Picasso to handle images
        String url = "http://www.moblet.club/moblet/images/" + list.get(position).imageId.toLowerCase() + ".jpg";
        Picasso.with(context).load(url).placeholder(R.drawable.reg_bg_small).centerCrop().error(R.drawable.reg_bg_small).resize(300, 167).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Add data to List
     * @param position data position
     * @param data
     */
    public void insert(int position, expense_data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    /**
     *Remove data from list
     * @param data
     */
    public void remove(expense_data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


}
