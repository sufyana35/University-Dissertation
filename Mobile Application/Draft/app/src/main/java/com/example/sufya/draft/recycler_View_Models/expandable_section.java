package com.example.sufya.draft.recycler_View_Models;

/**
 * Created by sufyan Ahmed on 27/03/2017.
 */

public class expandable_section {

    private final String category;
    private final String grouped_expense;

    public boolean isExpanded;

    public expandable_section(String category, String grouped_expense) {
        this.category = category;
        this.grouped_expense = grouped_expense;
        isExpanded = false;
    }

    public String getCategory() {
        return category;
    }

    public String getGrouped_expense() {
        return grouped_expense;
    }
}
