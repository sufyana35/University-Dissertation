package com.example.sufya.draft.recycler_Views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufya.draft.R;
import com.example.sufya.draft.recycler_View_Models.card_month_day;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by sufyan Ahmed on 07/03/2017.
 */

/**
 *For home screen
 */
public class Recycler_View_Adapter_day extends RecyclerView.Adapter<Recycler_View_Adapter_day.View_Holder> {

    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView day;
        TextView viewAll;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.monthDay);
            day = (TextView) itemView.findViewById(R.id.card_day_day);
            viewAll = (TextView) itemView.findViewById(R.id.card_day_viewAll);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //Start broadcast
            sendMessage();

        }

        private void sendMessage() {
            Log.d("sender", "Broadcasting message");
            Intent intent = new Intent("custom-event-name");
            // You can also include some extra data.
            intent.putExtra("recyclerViewDay", String.valueOf(day.getText()));
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    List<card_month_day> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter_day(List<card_month_day> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_day, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //replace symbols and .
        String cleanString = list.get(position).viewAll.replaceAll("[£,.]", "");

        //format price
        double parsed = Double.parseDouble(cleanString);
        String formatted = NumberFormat.getCurrencyInstance().format((parsed/1000));

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.day.setText(list.get(position).day);
        holder.viewAll.setText(formatted);
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
     * @param position insert data to list position
     * @param day expenbse day
     */
    public void insert(int position, card_month_day day) {
        list.add(position, day);
        notifyItemInserted(position);
    }

    /**
     *
     * @param day adapter day
     */
    public void remove(card_month_day day) {
        int position = list.indexOf(day);
        list.remove(position);
        notifyItemRemoved(position);
    }


}
