package com.example.sufya.draft.search;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.R;
import com.example.sufya.draft.recycler_View_Models.expense_data;
import com.example.sufya.draft.recycler_Views.Recycler_View_Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufyan Ahmed on 22/03/2017.
 */

/**
 * Search expense screen
 */
public class Search extends AppCompatActivity {

    private RecyclerView search_recyclerView;
    public List<expense_data> data = new ArrayList<>();

    /**
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText et_search_search = (EditText) findViewById(R.id.et_search_search);
        search_recyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        CollapsingToolbarLayout collapse_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        et_search_search.addTextChangedListener(text);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);

    }


    /**
     *populate data when user enters texts
     */
    private final TextWatcher text = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            EditText newSearch = (EditText) findViewById(R.id.et_search_search);

            data.clear();
            recyclerViewExpenses(newSearch.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };

    /**
     * initialise recyclerview
     * @param search
     */
    public void recyclerViewExpenses(String search) {

        final List<expense_data> data = fill_with_data(search);

        search_recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        search_recyclerView.setLayoutManager(_sGridLayoutManager);

        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, this);
        search_recyclerView.setAdapter(adapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        search_recyclerView.setItemAnimator(itemAnimator);
    }

    /**
     *
     * @param search search by expense description from database
     * @return results
     */
    public List<expense_data> fill_with_data(String search) {

        //open database
        ExpenseDbAdapter dbHelper = new ExpenseDbAdapter(this);
        dbHelper.open(); //Open Database#

        Cursor cursor2 = dbHelper.searchExpenseByDescription(search);

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
            //close database connection and cursor
            cursor2.close();
            dbHelper.close();
        }

        return data;
    }

}
