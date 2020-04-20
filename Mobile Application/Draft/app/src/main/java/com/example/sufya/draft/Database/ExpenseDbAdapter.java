package com.example.sufya.draft.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

public class ExpenseDbAdapter {

    //DEFINE VARIABLES
    public static final String KEY_ROWID = "_id";
    public static final String KEY_UNIQUEID = "uniqueid";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_SUBCATEGORY = "subcategory";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRICE = "price";
    public static final String KEY_TIMESTAMP = "timestamp";

    private static final String TAG = "ExpenseDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //Database Variable Names
    private static final String DATABASE_NAME = "expenses";
    private static final String SQLITE_TABLE = "records";
    private static final int DATABASE_VERSION = 1;

    //Context Name
    private final Context mCtx;

    //CREATE DATABASE PARAMETERS
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_UNIQUEID + "," +
                    KEY_CATEGORY + "," +
                    KEY_SUBCATEGORY + "," +
                    KEY_DESCRIPTION + "," +
                    KEY_PRICE + "," +
                    KEY_TIMESTAMP + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    /**
     *
     * @param ctx
     */
    public ExpenseDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ExpenseDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close database connection
     */
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     *
     * @param uniqueid user ID
     * @param category category
     * @param subcategory subcategory
     * @param description expense description
     * @param price expense price
     * @param timestamp current timestamp
     * @return return @param values
     */
    public long createExpense(String uniqueid, String category,
                              String subcategory, String description, String price, String timestamp) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_UNIQUEID, uniqueid);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_SUBCATEGORY, subcategory);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_TIMESTAMP, timestamp);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    /**
     *
     * @return boolean state
     */
    public boolean deleteAllExpenses() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    /**
     *
     * @param inputText Search by expense description value
     * @return return results
     * @throws SQLException throw error
     */
    public Cursor searchExpenseByDescription(String inputText) throws SQLException {
        Cursor mCursor = null;

            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_UNIQUEID, KEY_CATEGORY, KEY_SUBCATEGORY, KEY_DESCRIPTION, KEY_PRICE, KEY_TIMESTAMP},
                    KEY_DESCRIPTION + " like '%" + inputText + "%'", null,
                    null, null, null, null);

        return mCursor;
    }

    /**
     *
     * @param description search database by description
     * @return results
     * @throws SQLException throw error
     */
    public Cursor fetchExpenseByDescription(String description) throws SQLException {
        Cursor mCursor = null;
            String[] projection= {KEY_ROWID,
                    KEY_UNIQUEID, KEY_CATEGORY, KEY_SUBCATEGORY, KEY_DESCRIPTION, KEY_PRICE, KEY_TIMESTAMP};
            String[] where={description};
            mCursor = mDb.query(SQLITE_TABLE, projection, KEY_DESCRIPTION+"=?", where, null, null, "timestamp DESC", null);
        return mCursor;
    }

    /**
     *
     * @param day search expenses by day
     * @return results
     * @throws SQLException throw error
     */
    public Cursor fetchExpenseBySingleDate(String day) throws SQLException {
        Cursor mCursor = null;
            String[] projection= {KEY_ROWID,
                KEY_UNIQUEID, KEY_CATEGORY, KEY_SUBCATEGORY, KEY_DESCRIPTION, KEY_PRICE, KEY_TIMESTAMP};
            String[] where={day};
            mCursor = mDb.query(SQLITE_TABLE, projection, KEY_TIMESTAMP+"=?", where, null, null, null);
        return mCursor;
    }

    /**
     *
     * @param from Search expense date from
     * @param to search expense date to
     * @return results
     * @throws SQLException throw error
     */
    public Cursor fetchExpenseByDate(String from, String to) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE + " where " + KEY_TIMESTAMP + " between '" + from + "' and '" + to + "' ORDER BY "+ KEY_TIMESTAMP+" DESC", null);
        return mCursor;
    }

    /**
     *
     * @param from Search expense date from
     * @param to search expense date to
     * @param description search database by dates
     * @return results
     * @throws SQLException throw error
     */
    public Cursor fetchExpenseByDateAndDescription(String from, String to, String description) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE + " where " + KEY_DESCRIPTION + " = '" +description+ "' and " + KEY_TIMESTAMP + " between '" + from + "' and '" + to + "' ORDER BY "+ KEY_TIMESTAMP+" DESC", null);
        return mCursor;

    }

    /**
     *
     * @return results
     */
    public Cursor fetchAllExpenses() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_UNIQUEID, KEY_CATEGORY, KEY_SUBCATEGORY, KEY_DESCRIPTION, KEY_PRICE, KEY_TIMESTAMP},
                null, null, null, null, "timestamp DESC", "200");

        return mCursor;
    }

    /**
     *
     * @param category search database by category
     * @return results
     * @throws SQLException throw error
     */
    public Cursor fetchExpenseByCategory(String category) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE + " where " + KEY_CATEGORY + " = '" +category + "'", null);
        return mCursor;

    }

    /**
     *Function to insert expense records, testing purposes
     */
    public void insertSomeExpenses() {
        //Sample example createExpense("unique_id", "Category", "Sub-Category", "Description", "Price", "Timestamp");
    }

    /**
     *
     * @param id Expense unique rowID
     * @return results
     */
    public int deleteData (String id) {
        mDb.execSQL("delete from "+SQLITE_TABLE+" where _id='"+id+"'");
        return  mDb.delete(SQLITE_TABLE, "_id=?", new String[]{id});

    }

    /**
     *
     * @param id KeyRowID
     * @param uniqueid user personal ID
     * @param category category
     * @param subcategory subcategory
     * @param description expense description
     * @param price expense price
     * @param timestamp current timestamp
     * @return results
     */
    public long updateExpense(String id, String uniqueid, String category,
                              String subcategory, String description, String price, String timestamp) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_UNIQUEID, uniqueid);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_SUBCATEGORY, subcategory);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_TIMESTAMP, timestamp);

        String[] projection= {KEY_ROWID,
                KEY_UNIQUEID, KEY_CATEGORY, KEY_SUBCATEGORY, KEY_DESCRIPTION, KEY_PRICE, KEY_TIMESTAMP};
        String[] where={id};

        mDb.update(SQLITE_TABLE, initialValues, KEY_ROWID+"=?", where);

        return mDb.update(SQLITE_TABLE, initialValues, KEY_ROWID+"=?", where);
    }

}


