package com.multimedia.eformatic.model;

import android.text.Html;

import com.multimedia.eformatic.constants.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandra on 29/08/15.
 */
public class Category {

    protected String mId;
    protected String mTitle;
    protected String mHtmlDescription;
    private List<Category> mSubcategories;
    protected boolean mActive;


    public Category(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        this.mId = jsonObject.optString(Attributes.ID);
        this.mTitle = jsonObject.optString(Attributes.TITLE);
        this.mActive = jsonObject.optBoolean(Attributes.ACTIVE);
        this.mHtmlDescription = jsonObject.optString(Attributes.DESCRIPTION);

        JSONArray array = jsonObject.optJSONArray(Attributes.SUBCATEGORIES);


        mSubcategories = new ArrayList<>();
        if (array != null) {

            try {
                for (int i = 0; i < array.length(); i++) {

                    Category c = new Category(array.getJSONObject(i));
                    mSubcategories.add(c);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    // Getters

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }


    public String getDescription() {
        return Html.fromHtml(mHtmlDescription).toString();
    }


    public List<Category> getSubcategories() {
        return mSubcategories;
    }


    public boolean isActive() {
        return mActive;
    }


}
