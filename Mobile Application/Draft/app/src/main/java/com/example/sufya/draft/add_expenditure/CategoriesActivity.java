package com.example.sufya.draft.add_expenditure;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.sufya.draft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

public class CategoriesActivity extends ListActivity {

    public static String RESULT_SUBCATEGORY = "subCategory";
    public static String RESULT_CATEGORY = "category";
    public String[] category;
    public String[] subCategory;
    private TypedArray categoryImage;
    private List<Categories> categories;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCategoriesList();
        ArrayAdapter<Categories> adapter = new CategoriesListArrayAdapter(this, categories);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categories c = categories.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_SUBCATEGORY, c.getCategory());
                returnIntent.putExtra(RESULT_CATEGORY, c.getSubCategory());
                setResult(RESULT_OK, returnIntent);
                categoryImage.recycle(); //recycle images
                finish();
            }
        });
    }

    /**
     * Populate list with data defined in Strings file
     */
    private void populateCategoriesList() {
        categories = new ArrayList<Categories>();

        category = getResources().getStringArray(R.array.category);
        subCategory = getResources().getStringArray(R.array.subCategory);
        categoryImage = getResources().obtainTypedArray(R.array.categoryImages);
        for(int i = 0; i < subCategory.length; i++){
            categories.add(new Categories(subCategory[i], category[i], categoryImage.getDrawable(i)));
        }
    }

    /**
     *Data model class adapter for ViewList
     */
    public class Categories {
        private Drawable categoryImage;
        private String category;
        private String subCategory;

        /**
         *
         * @param category Category
         * @param subCategory Subcategory
         * @param categoryImage Image of Subcategory
         */
        public Categories(String category, String subCategory, Drawable categoryImage){
            this.categoryImage = categoryImage;
            this.category = category;
            this.subCategory = subCategory;
        }

        public Drawable getSubCat() {
            return categoryImage;
        }
        public String getCategory() {
            return category;
        }
        public String getSubCategory() {
            return subCategory;
        }
    }
}
