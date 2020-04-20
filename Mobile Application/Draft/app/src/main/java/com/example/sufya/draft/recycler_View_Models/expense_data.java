package com.example.sufya.draft.recycler_View_Models;

/**
 * Created by sufyan Ahmed on 26/02/2017.
 */

/**
 *Data model for ViewExpenseList Screen
 */
public class expense_data {
    public String description;
    public String price;
    public String category;
    public String subCategory;
    public String timestamp;
    public String imageId;
    public String id;

    public expense_data(String description, String price, String category, String subCategory, String timestamp, String imageId, String id) {
        this.description = description;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.timestamp = timestamp;
        this.imageId = imageId;
        this.id = id;
    }

}
