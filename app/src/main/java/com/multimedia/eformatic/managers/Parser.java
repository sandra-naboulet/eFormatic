package com.multimedia.eformatic.managers;

import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandra on 29/08/15.
 */
public class Parser {

    ArrayList<Category> mCategories = new ArrayList<>();
    ArrayList<Training> mTrainings = new ArrayList<>();

    public boolean parseCategories(JSONArray jsonArray) {

        if (jsonArray == null) {
            return false;
        }

        mCategories.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Category c = new Category(jsonArray.getJSONObject(i));
                mCategories.add(c);
            }
        } catch (JSONException e) {
            return false;
        }


        return true;
    }

    public boolean parseTrainings(JSONArray jsonArray) {

        if (jsonArray == null) {
            return false;
        }

        mTrainings.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Training t = new Training(jsonArray.getJSONObject(i));
                mTrainings.add(t);
            }
        } catch (JSONException e) {
            return false;
        }


        return true;
    }


    public ArrayList<Category> getCategories() {
        return mCategories;
    }

    public ArrayList<Training> getTrainings() {
        return mTrainings;
    }

    public void clear() {
        mCategories.clear();
        mTrainings.clear();
    }

}
