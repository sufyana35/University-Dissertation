package com.example.sufya.draft.home;

import android.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sufya.draft.recycler_View_Models.expense_data;
import com.example.sufya.draft.Database.ExpenseDbAdapter;

import com.example.sufya.draft.R;
import com.example.sufya.draft.recycler_Views.Recycler_View_Adapter;
import com.example.sufya.draft.recycler_Views.Recycler_View_Adapter_Month;
import com.example.sufya.draft.recycler_Views.Recycler_View_Adapter_day;
import com.example.sufya.draft.recycler_View_Models.card_month_data;
import com.example.sufya.draft.recycler_View_Models.card_month_day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

/**
 * Home screen
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ExpenseDbAdapter dbHelper;
    public double priceTotal, expenseNumber;
    public String Rmonth;
    public String Rday;
    public int Ryear;

    public List<expense_data> data = new ArrayList<>();
    public Recycler_View_Adapter adapter;
    public RecyclerView recyclerViewDay, recyclerViewMonth, recyclerViewExpenses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        View view = inflater.inflate(R.layout.fragment_home,parent,false);
        return view;
    }

    @Override
    public void onClick(View v) {


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        Ryear = Calendar.getInstance().get(Calendar.YEAR);

        //Recent Activity
        recyclerViewExpenses = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerViewExpenses("default", "");
        //recyclerViewExpenses.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewExpenses, this));

        //Months View
        recyclerViewMonth = (RecyclerView) view.findViewById(R.id.recyclerviewMonth);
        recyclerViewMonth();

        //Days view
        recyclerViewDay = (RecyclerView) view.findViewById(R.id.recyclerviewDay);
        recyclerViewDay();

        //Attaching Broadcasts for recyclerview days and months
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastDay,
                new IntentFilter("custom-event-name"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastMonth,
                new IntentFilter("custom-event-name-send"));

    }

    /**
     *
     * @param type search by months or days
     * @param send define search by date
     * @return
     */
    public List<expense_data> fill_with_data(String type, String send) {

        //clear populated list
        data.clear();

        Calendar c = Calendar.getInstance();

        switch (type) {
            case "recyclerViewMonth":
                String from = Ryear +"-" + Rmonth + "-" + "01";
                String to = Ryear +"-" + Rmonth + "-" + "31";

                getExpenses(from, to);

                break;
            case "recyclerViewDay":
                getExpenses(send, send);

                break;

            default:

                dbHelper = new ExpenseDbAdapter(getActivity());
                dbHelper.open(); //Open Database#

                Cursor cursor2 = dbHelper.fetchAllExpenses();

                try {
                    while (cursor2.moveToNext()) {
                        final String description = cursor2.getString(cursor2.getColumnIndexOrThrow("description"));
                        final String price = cursor2.getString(cursor2.getColumnIndexOrThrow("price"));
                        final String category = cursor2.getString(cursor2.getColumnIndexOrThrow("category"));
                        final String subcategory = cursor2.getString(cursor2.getColumnIndexOrThrow("subcategory"));
                        final String timestamp = cursor2.getString(cursor2.getColumnIndexOrThrow("timestamp"));
                        final String id = cursor2.getString(cursor2.getColumnIndexOrThrow("_id"));

                        String newDescription = description.replaceAll("\\s", "");

                        data.add(new expense_data(description, price, category, subcategory, timestamp, newDescription.trim(), id));

                    }
                } finally {
                    cursor2.close();
                    dbHelper.close();
                }

                break;
        }

        return data;
    }

    /**
     *
     * @param from search expenses from date
     * @param to search expenses to date
     */
    public void getExpenses(String from, String to) {

        dbHelper = new ExpenseDbAdapter(getActivity());
        dbHelper.open(); //Open Database#

        Cursor cursor = dbHelper.fetchExpenseByDate(String.valueOf(from), String.valueOf(to));

        try {
            while (cursor.moveToNext()) {

                final String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                final String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                final String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                final String subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"));
                final String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                final String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                String newDescription = description.replaceAll("\\s", "");

                data.add(new expense_data(description, price, category, subcategory, timestamp, newDescription.trim(), id));

            }
        } finally {
            cursor.close();
            dbHelper.close();
        }
    }

    /**
     *
     * @return data, populate recyclerview Months and return data specified
     */
    public List<card_month_data> month_data() {

        List<card_month_data> data = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<String> months_names = new ArrayList<>();

        months.add("01"); months_names.add("january");
        months.add("02"); months_names.add("february");
        months.add("03"); months_names.add("march");
        months.add("04"); months_names.add("april");
        months.add("05"); months_names.add("may");
        months.add("06"); months_names.add("june");
        months.add("07"); months_names.add("july");
        months.add("08"); months_names.add("august");
        months.add("09"); months_names.add("september");
        months.add("10"); months_names.add("october");
        months.add("11"); months_names.add("november");
        months.add("12"); months_names.add("december");

        for (int i=0; i<months.size(); i++) {

            data.add(new card_month_data(String.valueOf(""), String.valueOf(""), String.valueOf(months.get(i)), String.valueOf(months_names.get(i))));

        }

        return data;
    }

    /**
     *
     * @return populate days recyclerview by android calendar function and calculate days price
     */
    public List<card_month_day> day_data() {

        List<card_month_day> data = new ArrayList<>();

        int month_c;

        Calendar c = Calendar.getInstance();

        if (Rmonth != null) {

            month_c = Integer.parseInt(Rmonth);

        } else {

            month_c = c.get(Calendar.MONTH) + 1;

        }

        String date = Ryear + "-" + + month_c + "-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException ex) {

        }
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int co = 0; co < maxDay; co++) {

            String day = sdf.format(c.getTime());

            c.add(Calendar.DATE, 1);

            dbHelper = new ExpenseDbAdapter(getActivity());
            dbHelper.open(); //Open Database#

            Cursor cursor = dbHelper.fetchExpenseBySingleDate(String.valueOf(day));

            try {
                while (cursor.moveToNext()) {

                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                    priceTotal = priceTotal + price;

                    expenseNumber++;

                }
            } finally {
                cursor.close();
                dbHelper.close();
            }


            data.add(new card_month_day(String.valueOf(day), String.valueOf(priceTotal)));
            priceTotal = 0;
            expenseNumber = 0;
        }

        return data;
    }

    /**
     *recieve messages
     */
    private BroadcastReceiver broadcastMonth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String month = intent.getStringExtra("recyclerViewMonth");
            Rmonth = month;;

            recyclerViewDay();
            recyclerViewExpenses("recyclerViewMonth", month);
        }
    };

    /**
     *recieve messages
     */
    private BroadcastReceiver broadcastDay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String day = intent.getStringExtra("recyclerViewDay");
            Rday = day;

            recyclerViewDay();
            recyclerViewExpenses("recyclerViewDay", day);
        }
    };

    /**
     *initialise recyclerview
     */
    public void recyclerViewDay() {

        final List<card_month_day> days = day_data();

        Recycler_View_Adapter_day adapter = new Recycler_View_Adapter_day(days, getActivity());
        recyclerViewDay.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerViewDay.setLayoutManager(linearLayoutManager);
    }

    /**
     *initialise recyclerview and data
     */
    public void recyclerViewMonth() {

        final List<card_month_data> month = month_data();

        SnapHelper snapHelperMonth = new LinearSnapHelper();
        snapHelperMonth.attachToRecyclerView(recyclerViewMonth);

        Recycler_View_Adapter_Month adapter = new Recycler_View_Adapter_Month(month, getActivity());
        recyclerViewMonth.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerViewMonth.setLayoutManager(linearLayoutManager);
    }

    /**
     *
     * @param type months or days
     * @param send dates
     */
    public void recyclerViewExpenses(String type, String send) {

        int rows= 2;

        //Recent Activity
        final List<expense_data> data = fill_with_data(type, send);

        recyclerViewExpenses.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerViewExpenses.setLayoutManager(_sGridLayoutManager);

        adapter = new Recycler_View_Adapter(data, getActivity());
        recyclerViewExpenses.setAdapter(adapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerViewExpenses.setItemAnimator(itemAnimator);
    }

}

