package com.example.sufya.draft.recycler_View_Models;

/**
 * Created by sufyan Ahmed on 06/03/2017.
 */

/**
 *Data Model for months on home screen
 */
public class card_month_data {

    public String month_expense;
    public String month_recorded;
    public String month_symbol;
    public String month;

    public card_month_data(String month_expense, String month_recorded, String month_symbol, String month) {
        this.month_expense = month_expense;
        this.month_recorded = month_recorded;
        this.month_symbol = month_symbol;
        this.month = month;

    }
}
