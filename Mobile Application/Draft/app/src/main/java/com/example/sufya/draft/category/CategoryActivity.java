package com.example.sufya.draft.category;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.R;
import com.example.sufya.draft.expense_list.ViewExpenseList;
import com.example.sufya.draft.recycler_View_Models.expandable_item;
import com.example.sufya.draft.recycler_View_Models.expandable_section;
import com.example.sufya.draft.recycler_Views.expandable.ItemClickListener;
import com.example.sufya.draft.recycler_Views.expandable.SectionedExpandableLayoutHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

/**
 * Category Screen
 */
public class CategoryActivity extends AppCompatActivity implements ItemClickListener {

    RecyclerView mRecyclerView;
    private String[] category;
    private List<String> al = new ArrayList<>();
    private ExpenseDbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //setting the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addData();

    }

    /**
     *
     * @param item return position and value of item clicked
     */
    @Override
    public void itemClicked(expandable_item item) {
        Toast.makeText(this, "expandable_item: " + item.getDescription_expense() + " clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ViewExpenseList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("DESCRIPTION", String.valueOf(item.getDescription_expense()));

        this.startActivity(intent);
    }

    /**
     *
     * @param section return position and value of item clicked
     */
    @Override
    public void itemClicked(expandable_section section) {
        Toast.makeText(this, "expandable_section: " + section.getCategory() + " clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ViewExpenseList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("DESCRIPTION", String.valueOf(section.getCategory()));

        this.startActivity(intent);
    }

    /**
     * populate recyclerview with data from database
     */
    public void addData() {

        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this,
                mRecyclerView, this, 3);

        ArrayList<expandable_item> arrayList = new ArrayList<>();
        category = getResources().getStringArray(R.array.category_modified);

        for (int i = 0; i < category.length; i++) {

            dbHelper = new ExpenseDbAdapter(getApplicationContext());
            dbHelper.open(); //Open Database#

            Cursor cursor2 = dbHelper.fetchExpenseByCategory(category[i]);

            try {
                while (cursor2.moveToNext()) {
                    final String description = cursor2.getString(cursor2.getColumnIndexOrThrow("description"));

                    al.add(description);

                }
            } finally {
                cursor2.close();
                dbHelper.close();
            }

            Set<String> hs = new HashSet<>();
            hs.addAll(al);
            al.addAll(hs);

            int z = 0;
            for (String s : hs) {
                arrayList.add(new expandable_item((s), z));
                z++;
            }
            al.clear();
            hs.clear();
            sectionedExpandableLayoutHelper.addSection(category[i], String.valueOf(z), arrayList);
            arrayList = new ArrayList<>();

        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();

    }
}
