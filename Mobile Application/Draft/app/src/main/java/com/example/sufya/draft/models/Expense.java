package com.example.sufya.draft.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

/**
 *Data model for Displaying expenses on the home screen, search expenses and view expense list
 */
public class Expense {

    private String uniqueid;
    private String category;
    private String sub_category;
    private String description;
    private String price;
    private String timestamp;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}