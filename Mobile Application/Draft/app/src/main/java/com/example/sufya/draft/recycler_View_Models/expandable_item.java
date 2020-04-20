package com.example.sufya.draft.recycler_View_Models;

/**
 * Created by sufyan Ahmed on 27/03/2017.
 */

public class expandable_item {

    private final String description_expense;
    private final int id;

    public expandable_item(String description_expense, int id) {
        this.description_expense = description_expense;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription_expense() {
        return description_expense;
    }
}
