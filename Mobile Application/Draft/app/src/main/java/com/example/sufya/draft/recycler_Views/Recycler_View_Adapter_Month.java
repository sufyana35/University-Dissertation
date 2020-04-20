package com.example.sufya.draft.recycler_Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufya.draft.R;
import com.example.sufya.draft.recycler_View_Models.card_month_data;

import java.util.Collections;
import java.util.List;

/**
 * Created by sufyan Ahmed on 06/03/2017.
 */

public class Recycler_View_Adapter_Month extends RecyclerView.Adapter<Recycler_View_Adapter_Month.View_Holder> {

    //Load local pref file saved on device
    private SharedPreferences pref;

    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView recorded;
        TextView spent;
        TextView month_symbol;
        TextView month;

        public View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.monthCard);
            recorded = (TextView) itemView.findViewById(R.id.card_month_recorded_input);
            spent = (TextView) itemView.findViewById(R.id.card_month_spent_input);
            month_symbol = (TextView) itemView.findViewById(R.id.card_month_symbol_input);
            month = (TextView) itemView.findViewById(R.id.card_month_month_input);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sendMessage();
        }

        private void sendMessage() {
            Log.d("sender", "Broadcasting message");
            Intent intent = new Intent("custom-event-name-send");
            // You can also include some extra data.
            intent.putExtra("recyclerViewMonth", String.valueOf(month_symbol.getText()));
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    List<card_month_data> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter_Month(List<card_month_data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_month, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.recorded.setText(list.get(position).month_recorded);
        holder.spent.setText(list.get(position).month_expense);
        holder.month_symbol.setText(list.get(position).month_symbol);
        holder.month.setText(list.get(position).month);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.invalidate();
    }

    /**
     *
     * @param position
     * @param month
     */
    public void insert(int position, card_month_data month) {
        list.add(position, month);
        notifyItemInserted(position);
    }

    /**
     *
     * @param month
     */
    public void remove(card_month_data month) {
        int position = list.indexOf(month);
        list.remove(position);
        notifyItemRemoved(position);
    }


}

