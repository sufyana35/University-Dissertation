package com.example.sufya.draft.recycler_Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.expense_list.ViewExpenseList;
import com.example.sufya.draft.add_expenditure.addExpenditureActivity;
import com.example.sufya.draft.recycler_View_Models.expense_data;
import com.example.sufya.draft.R;
import com.squareup.picasso.*;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;


/**
 * Created by sufyan Ahmed on 26/02/2017.
 */

public class Recycler_View_Adapter extends RecyclerView.Adapter<Recycler_View_Adapter.View_Holder> {

    private ExpenseDbAdapter dbHelper;

    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView description;
        TextView price;
        TextView category;
        TextView subCategory;
        TextView timestamp;
        ImageView imageView;
        ImageView overflow;
        TextView id;

        View_Holder(View itemView) {
            super(itemView);
            //intialise views
            cv = (CardView) itemView.findViewById(R.id.cardView);
            category = (TextView) itemView.findViewById(R.id.recent_category);
            subCategory = (TextView) itemView.findViewById(R.id.recent_subCategory);
            description = (TextView) itemView.findViewById(R.id.recent_description);
            price = (TextView) itemView.findViewById(R.id.recent_price);
            timestamp = (TextView) itemView.findViewById(R.id.recent_timestamp);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            id = (TextView) itemView.findViewById(R.id.recent_id);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);

            imageView.setOnClickListener(this);

        }

        /**
         * Attach onclick listener to item
         * @param view
         */
        @Override
        public void onClick(View view) {

            //Start new activity and pass values
            final Intent intent;
            intent =  new Intent(context, ViewExpenseList.class);

            intent.putExtra("TASK", String.valueOf("EDITEXPENSE"));
            intent.putExtra("ID", String.valueOf(id.getText()));
            intent.putExtra("CATEGORY", String.valueOf(category.getText()));
            intent.putExtra("SUB-CATEGORY", String.valueOf(subCategory.getText()));
            intent.putExtra("DESCRIPTION", String.valueOf(description.getText()));
            intent.putExtra("PRICE", String.valueOf(price.getText()).replace("£", ""));
            context.startActivity(intent);
        }
    }

    List<expense_data> list = Collections.emptyList();
    Context context;

    /**
     *
     * @param list
     * @param context
     */
    public Recycler_View_Adapter(List<expense_data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expense, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //format price
        double parsed = Double.parseDouble(list.get(position).price);
        //get local currency symbol
        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.description.setText(list.get(position).description);
        holder.price.setText(formatted);
        holder.category.setText(list.get(position).category);
        holder.subCategory.setText(list.get(position).subCategory);
        holder.id.setText(list.get(position).id);
        holder.timestamp.setText(list.get(position).timestamp);

        //
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.overflow);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_expense);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:

                                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();

                                //Start activity and send intent data
                                Intent intent = new Intent(context, addExpenditureActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("TASK", String.valueOf("EDITEXPENSE"));
                                intent.putExtra("ID", String.valueOf(list.get(position).id));
                                intent.putExtra("CATEGORY", String.valueOf(list.get(position).category));
                                intent.putExtra("SUB-CATEGORY", String.valueOf(list.get(position).subCategory));
                                intent.putExtra("DESCRIPTION", String.valueOf(list.get(position).description));
                                intent.putExtra("PRICE", String.valueOf(list.get(position).price));

                                context.startActivity(intent);

                                break;
                            case R.id.action_delete:
                                //handle menu2 click
                                remove(list.get(position));

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        //load image
        String url = "http://www.moblet.club/moblet/images/" + list.get(position).imageId.toLowerCase() + ".jpg"; //format url
        Picasso.with(context).load(url).placeholder(R.drawable.reg_bg_small).error(R.drawable.reg_bg_small).into(holder.imageView, new Callback() { //coment
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
     *
     * @param position what position has been clicked puon
     * @param data return clicked results data
     */
    public void insert(int position, expense_data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    /**
     *
     * @param data
     */
    public void remove(expense_data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());

        dbHelper = new ExpenseDbAdapter(context);
        dbHelper.open(); //Open Database#
        dbHelper.deleteData(data.id);
        dbHelper.close();
    }

}
