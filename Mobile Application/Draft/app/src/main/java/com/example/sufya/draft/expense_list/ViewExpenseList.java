package com.example.sufya.draft.expense_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.R;
import com.example.sufya.draft.add_expenditure.addExpenditureActivity;
import com.example.sufya.draft.recycler_View_Models.expense_data;
import com.example.sufya.draft.recycler_Views.BlurTransformation;
import com.example.sufya.draft.recycler_Views.RecyclerItemClickListener;
import com.example.sufya.draft.recycler_Views.Recycler_View_Adapter_Expense_List;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

public class ViewExpenseList extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    private ExpenseDbAdapter dbHelper;
    public double runningValue;
    private TextView year, month;
    private Spinner spinner_month, spinner_year;
    public List<expense_data> data = new ArrayList<>();
    private String date, to;
    public String intent_id, intent_category, intent_subCategory, intent_description, intent_price;
    int month_c;
    int year_c;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense_list);

        //
        intent_id = getIntent().getStringExtra("ID");
        intent_category = getIntent().getStringExtra("CATEGORY");
        intent_subCategory = getIntent().getStringExtra("SUB_CATEGORY");
        intent_description = getIntent().getStringExtra("DESCRIPTION");
        intent_price = getIntent().getStringExtra("PRICE");
        String newDescription = intent_description.replaceAll("\\s", "").toLowerCase();
        String url = "http://www.moblet.club/moblet/images/" + newDescription + ".jpg";

        /* Initializing views */
        year = (TextView) findViewById(R.id.view_expense_list_year_input);
        month = (TextView) findViewById(R.id.view_expense_list_month_input);
        spinner_month = (Spinner) findViewById(R.id.view_expense_list_spinner_month);
        spinner_year = (Spinner) findViewById(R.id.view_expense_list_spinner_year);
        ImageView background = (ImageView) findViewById(R.id.bg_viewExpenseList);

        //Year price
        ArrayList<String> years = new ArrayList<String>();
        year_c = Calendar.getInstance().get(Calendar.YEAR);
        date = year_c + "-" + monthParse(month_c) + "-01";
        to = year_c + "-" +  monthParse(month_c) + "-31";
        for (int i = 2000; i <= year_c; i++) {
            years.add(Integer.toString(i));
        }

        //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinner_year.setAdapter(adapter);
        spinner_year.setSelection(((ArrayAdapter<String>)spinner_year.getAdapter()).getPosition(String.valueOf(year_c)));

        //
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selected_year = spinner_year.getSelectedItem().toString();
                year_c = Integer.parseInt(selected_year);
                final String date = selected_year + "-" + monthParse(month_c) + "-01";
                final String to = selected_year + "-" + monthParse(month_c) + "-31";
                data.clear();
                year_c = Integer.parseInt(selected_year);

                double parsed = Double.parseDouble(priceFunctionMonth(date, to));
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                year.setText(formatted);
                viewExpenses();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        //Month price
        month_c = c.get(Calendar.MONTH) + 1;
        month_c = Integer.parseInt(monthParse(month_c));;
        date = year_c + "-" + monthParse(month_c) + "-01";
        to = year_c + "-" + monthParse(month_c) + "-31";
        spinner_month.setSelection(((ArrayAdapter<String>)spinner_month.getAdapter()).getPosition(monthParse(month_c)));

        //
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selected_month = spinner_month.getSelectedItem().toString();
                final String date = year_c + "-" + selected_month + "-01";
                final String to = year_c + "-" + selected_month + "-31";
                data.clear();
                month_c = Integer.parseInt(selected_month);

                double parsed = Double.parseDouble(priceFunctionMonth(date, to));
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                month.setText(formatted);
                viewExpenses();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        //Picasso load image and blur
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.reg_bg_small) // optional
                .error(R.drawable.reg_bg_small)         // optional
                .transform(new BlurTransformation(this))
                .into(background);

        //
        viewExpenses();

    }

    //populate and set recyclerView for expense data
    public void viewExpenses() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_view_expense_list);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        Recycler_View_Adapter_Expense_List adapter = new Recycler_View_Adapter_Expense_List(data, getApplicationContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * populate data from database
     * @return data
     */
    public List<expense_data> fill_with_data() {

        data.clear();

        dbHelper = new ExpenseDbAdapter(getApplicationContext());
        dbHelper.open(); //Open Database#

        Cursor cursor = dbHelper.fetchExpenseByDescription(intent_description);

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

        return data;
    }

    /**
     *
     * @param from Date from
     * @param to Date to
     * @return
     */
    public String priceFunctionMonth(String from, String to){

        runningValue = 0;

        dbHelper = new ExpenseDbAdapter(this);
        dbHelper.open(); //Open Database#

        Cursor fetch = dbHelper.fetchExpenseByDateAndDescription(String.valueOf(from), String.valueOf(to), String.valueOf(intent_description));

        try {
            while (fetch.moveToNext()) {

                final String description = fetch.getString(fetch.getColumnIndexOrThrow("description"));
                final String price = fetch.getString(fetch.getColumnIndexOrThrow("price"));
                final String category = fetch.getString(fetch.getColumnIndexOrThrow("category"));
                final String subcategory = fetch.getString(fetch.getColumnIndexOrThrow("subcategory"));
                final String timestamp = fetch.getString(fetch.getColumnIndexOrThrow("timestamp"));
                final String id = fetch.getString(fetch.getColumnIndexOrThrow("_id"));

                String newDescription = description.replaceAll("\\s", "");

                double parsed = Double.parseDouble(price);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                data.add(new expense_data(description, formatted, category, subcategory, timestamp, newDescription.trim(), id));

                double value = Double.parseDouble(price);
                runningValue = runningValue + value;

            }
        } finally {

            fetch.close();
            dbHelper.close();
        }

        return String.valueOf(runningValue);
    }

    /**
     *
     * @param month input month such as "1", "12"
     * @return m_final @return formatted number such as "01", "12"
     */
    public String monthParse (int month) {

        String m_final;
        m_final = "0";

        switch (month) {
            case 1:
                m_final = "01";
                break;

            case 2:
                m_final = "02";
                break;

            case 3:
                m_final = "03";
                break;

            case 4:
                m_final = "04";
                break;

            case 5:
                m_final = "05";
                break;

            case 6:
                m_final = "06";
                break;

            case 7:
                m_final = "07";
                break;

            case 8:
                m_final = "08";
                break;

            case 9:
                m_final = "09";
                break;

            case 10:
                m_final = "10";
                break;

            case 11:
                m_final = "11";
                break;

            case 12:
                m_final = "12";
                break;
        }

        return m_final;
    }

    /**
     *
     * @param childView View of the item that was clicked.
     * @param position  Position of the item that was clicked.
     */
    @Override
    public void onItemClick(View childView, int position) {
        Toast.makeText(this, "Edit Expense", Toast.LENGTH_SHORT).show();

        //sending data and starting new activity
        Intent intent = new Intent(this, addExpenditureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("TASK", String.valueOf("EDITEXPENSE"));
        intent.putExtra("ID", String.valueOf(data.get(position).id));
        intent.putExtra("CATEGORY", String.valueOf(data.get(position).category));
        intent.putExtra("SUB-CATEGORY", String.valueOf(data.get(position).subCategory));
        intent.putExtra("DESCRIPTION", String.valueOf(data.get(position).description));
        intent.putExtra("PRICE", String.valueOf(data.get(position).price).replace("£", ""));

        this.startActivity(intent);
    }

    /**
     *
     * @param childView View of the item that was long pressed.
     * @param position  Position of the item that was long pressed.
     */
    @Override
    public void onItemLongPress(View childView, int position) {

        dbHelper = new ExpenseDbAdapter(this);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewExpenseList.this, R.style.MyDialogTheme);
        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want delete " + data.get(position).description + " : " + data.get(position).price);
        alertDialog.setIcon(R.drawable.reg_logo);
        final String p = data.get(position).id;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dbHelper.open(); //Open Database#
                dbHelper.deleteData(p);
                dbHelper.close();

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        alertDialog.show();

    }



}
