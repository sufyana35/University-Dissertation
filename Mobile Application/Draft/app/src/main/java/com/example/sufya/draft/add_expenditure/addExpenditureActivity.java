package com.example.sufya.draft.add_expenditure;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufya.draft.Constants;
import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

public class addExpenditureActivity extends Activity {

    private ImageView IV_selectCategory;
    private Button BTN_expenditure_save;
    private TextView TV_expenditure_setCategory, TV_expenditure_setSubCategory;
    private String previousSubCategoryText, previousCategoryText;
    private EditText ET_expenditure_enterDescription, ET_expenditure_enterPrice;
    private String uniqueid;

    //Database declaration
    private ExpenseDbAdapter dbHelper;

    //Array List
    private String[] subCategory, category;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
        final Intent intent = new Intent(this, CategoriesActivity.class);

        //EDIT CODE
        String intent_type = getIntent().getStringExtra("TASK");
        SharedPreferences pref = getSharedPreferences("myprefs", 0);
        uniqueid = pref.getString(Constants.UNIQUE_ID,"");

        /* Initializing views */
        TV_expenditure_setCategory = (TextView) findViewById(R.id.TV_expenditure_setCategory); //Label
        TV_expenditure_setSubCategory = (TextView) findViewById(R.id.TV_expenditure_setSubCategory); //Label
        ET_expenditure_enterDescription = (EditText) findViewById(R.id.ET_expenditure_enterDescription); //TextField
        ET_expenditure_enterPrice = (EditText) findViewById(R.id.ET_expenditure_enterPrice); //TextField

        /* Set Text Watcher listener */
        ET_expenditure_enterDescription.addTextChangedListener(descriptionWatcher);
        ET_expenditure_enterPrice.addTextChangedListener(priceWatcher);

        //image button
        IV_selectCategory = (ImageView) findViewById(R.id.iv_selectCategory);
        IV_selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 1);
            }
        });

        //BTN_expenditure_save
        BTN_expenditure_save = (Button) findViewById(R.id.BTN_expenditure_save);
        BTN_expenditure_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TV_expenditure_setCategory.toString().isEmpty()
                        || TV_expenditure_setCategory.getText().toString().toLowerCase().equals("category")
                        || TV_expenditure_setSubCategory.getText().toString().toLowerCase().equals("sub - Category")
                        || ET_expenditure_enterDescription.toString().isEmpty() || ET_expenditure_enterDescription.length() == 0 || ET_expenditure_enterDescription.equals("") || ET_expenditure_enterDescription == null
                        || ET_expenditure_enterPrice.toString().isEmpty() || ET_expenditure_enterPrice.length() == 0 || ET_expenditure_enterPrice.equals("") || ET_expenditure_enterPrice == null) {
                    Toast.makeText(getApplicationContext(), "Please Fill All Required Fields", Toast.LENGTH_SHORT).show();
                }
                else {

                    //EDIT CODE
                    String intent_type = getIntent().getStringExtra("TASK");

                    if (intent_type != null && intent_type.equals("EDITEXPENSE")) {

                        Toast.makeText(getApplicationContext(), "TODOCODE", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();

                        final String category = TV_expenditure_setCategory.getText().toString();
                        final String sub_category = TV_expenditure_setSubCategory.getText().toString();
                        final String descriptionQ = ET_expenditure_enterDescription.getText().toString();
                        final String priceQ = ET_expenditure_enterPrice.getText().toString().replaceAll("[£,.$]", "");

                        insertExpense(uniqueid, category, sub_category, descriptionQ, priceQ);
                        finish();
                    }

                }

            }
        });

        if (intent_type != null && intent_type.equals("EDITEXPENSE")) {
            String intent_id = getIntent().getStringExtra("ID");
            String intent_description = getIntent().getStringExtra("DESCRIPTION");
            String intent_category = getIntent().getStringExtra("CATEGORY");
            String intent_subCategory = getIntent().getStringExtra("SUB-CATEGORY");
            String intent_price_edit = getIntent().getStringExtra("PRICE");

            ET_expenditure_enterDescription.setText(intent_description);
            TV_expenditure_setCategory.setText(intent_category);
            TV_expenditure_setSubCategory.setText(intent_subCategory);
            ET_expenditure_enterPrice.setText(intent_price_edit);

            Categorisation(intent_subCategory);
        }

    }

    /**
     * Formats the input price to "£0.00"
     */
    private final TextWatcher priceWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ET_expenditure_enterPrice.removeTextChangedListener(this);

            double parsed = Double.parseDouble(ET_expenditure_enterPrice.getText().toString().replaceAll("[£,.$]", ""));
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

            String newString = formatted.replaceAll("[£$]", "");

            ET_expenditure_enterPrice.setText(newString);
            ET_expenditure_enterPrice.setSelection(newString.length());

            ET_expenditure_enterPrice.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };

    /**
     * Text Watcher function to autocategorise description
     */
    private final TextWatcher descriptionWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (previousSubCategoryText == null) {
                TV_expenditure_setSubCategory.setText(R.string.subCategory);
            } else {
                TV_expenditure_setSubCategory.setText(previousSubCategoryText);
                IV_selectCategory.setBackgroundResource(0);
            }

            if (previousCategoryText == null) {
                TV_expenditure_setCategory.setText(R.string.category);
            } else {
                TV_expenditure_setCategory.setText(previousCategoryText);
                IV_selectCategory.setBackgroundResource(0);
            }

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

            EditText ET_enterDescription = (EditText) findViewById(R.id.ET_expenditure_enterDescription);
            String new_ET_enterDescription_String = ET_enterDescription.getText().toString().toLowerCase().trim();

            dbHelper = new ExpenseDbAdapter(getApplicationContext());
            dbHelper.open(); //Open Database#

            Cursor cursor2 = dbHelper.fetchExpenseByDescription(new_ET_enterDescription_String);

            try {
                while (cursor2.moveToNext()) {
                    final String subcategory = cursor2.getString(cursor2.getColumnIndexOrThrow("subcategory"));

                    Categorisation(subcategory);
                }
            } finally {
                cursor2.close();
                dbHelper.close();
            }

            //Define auto-categorisation values
            switch (new_ET_enterDescription_String) {
                case "xbox":
                case "playstation":
                case "nintendo":
                case "games":
                    Categorisation("Games");
                    break;

                case "itunes":
                case "spotify":
                case "google music":
                case "music":
                    Categorisation("Music");
                    break;

                case "netflix":
                case "movies":
                    Categorisation("Movies");
                    break;

                case "sainsburys":
                case "aldi":
                case "lidl":
                case "iceland":
                case "waitrose":
                case "morrisons":
                case "tesco":
                case "asda":
                case "groceries":
                    Categorisation("Groceries");
                    break;

                case "virgin media":
                case "o2":
                case "ee":
                case "vodafone":
                case "sky":
                case "bt":
                case "talktalk":
                case "phone":
                case "internet":
                case "tv":
                    Categorisation("TV/Phone/Internet");
                    break;

                case "uber":
                case "taxi":
                    Categorisation("Taxi");
                    break;

                case "council tax":
                    Categorisation("Services");
                    break;

                case "rent":
                    Categorisation("Rent");
                    break;

                case "bus":
                case "metro":
                case "underground":
                case "train":
                case "east coast":
                case "west coast":
                case "tram":
                    Categorisation("Bus/Train/Metro");
                    break;

                case "petrol":
                case "diesel":
                    Categorisation("Car");
                    break;

                case "british gas":
                case "edf":
                case "npower":
                case "scottishpower":
                case "sse":
                case "gas":
                    Categorisation("Heat/Gas");
                    break;

                case "ryanair":
                case "virgin atlantic":
                case "british airways":
                case "turkish airlines":
                case "easyjet":
                case "emirates":
                case "plane ticket":
                case "plane":
                    Categorisation("Plane");
                    break;

                case "nandos":
                case "dixy chicken":
                case "mcdonalds":
                case "costa":
                case "starbucks":
                case "frankie and bennys":
                case "dining out":
                    Categorisation("Dining Out");
                    break;

                case "topman":
                case "topshop":
                case "zara":
                case "river island":
                case "burton":
                case "armani":
                case "hugo boss":
                case "h&m":
                case "primark":
                case "debenham":
                case "shirt":
                case "jean":
                case "shoe":
                case "dress":
                    Categorisation("Clothing");
                    break;

            }


        }
    };

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final String subCategory = data.getStringExtra(CategoriesActivity.RESULT_SUBCATEGORY);
            final String category = data.getStringExtra(CategoriesActivity.RESULT_CATEGORY);

            //Set Edit Text Labels To Chosen Value
            TV_expenditure_setCategory.setText(category);
            TV_expenditure_setSubCategory.setText(subCategory);
            previousSubCategoryText = subCategory;
            previousCategoryText = category;

            //Load Image Function
            Categorisation(subCategory);

        }

    }

    /**
     *
     * @param uniqueid users personal ID
     * @param category Expense category
     * @param sub_category sub category
     * @param descriptionQ expense description
     * @param priceQ price of expense
     */
    public void insertExpense(String uniqueid, String category, String sub_category, String descriptionQ, String priceQ){
        //Insert record to Local database SQLITE
        dbHelper = new ExpenseDbAdapter(getApplicationContext());
        dbHelper.open(); //Open Database
        dbHelper.createExpense(uniqueid, category, sub_category, descriptionQ, priceQ, getDateTime());
        dbHelper.close();

    }

    /**
     *
     * @return default android date
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
                //"yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     * @param description expense description, categorise function
     */
    public void Categorisation(String description) {
        //Define auto-categorisation values
        description = description.toLowerCase(); //Ignore case sensitive letters

        category = getResources().getStringArray(R.array.category);
        subCategory = getResources().getStringArray(R.array.subCategory);
        TypedArray imgs = getResources().obtainTypedArray(R.array.categoryImages);

        for (int i = 0; i < subCategory.length; i++) {
            if (description.equals(subCategory[i].toLowerCase())) {
                IV_selectCategory.setImageResource(imgs.getResourceId(i, -1));
                TV_expenditure_setCategory.setText(category[i]);
                TV_expenditure_setSubCategory.setText(subCategory[i]);
                imgs.recycle();
            }
        }

    }

}

