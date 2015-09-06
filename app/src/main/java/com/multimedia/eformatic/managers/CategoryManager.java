package com.multimedia.eformatic.managers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.constants.Urls;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sandra on 29/08/15.
 */
public class CategoryManager {

    public static CategoryManager sInstance = null;

    private RequestQueue mRequestQueue;
    private Parser mParser;

    // Datas
    private List<Category> mCategories = new ArrayList<Category>();

    // Listeners
    private CategoryRequestListener mCategoryListener;

    private CategoryManager() {
        mParser = new Parser();
        mRequestQueue = Volley.newRequestQueue(EFormatic.getAppContext());
    }


    public static synchronized CategoryManager getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryManager();
        }

        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;

    }

    /**
     * Get all categories
     *
     * @param listener
     */
    public void startGetCategories(CategoryRequestListener listener) {

        mCategoryListener = listener;

        RequestQueue requestQueue = CategoryManager.getInstance().getRequestQueue();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Urls.GET_CATEGORIES, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("VOLLEY", "response : " + response);
                mCategories.clear();

                if (mParser.parseCategories(response)) {
                    mCategories = mParser.getCategories();
                    mCategoryListener.onCategoryRequestSuccess(mCategories);
                } else {
                    mCategoryListener.onCategoryRequestFail("Error while parsing categories");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "error " + error);
                mCategoryListener.onCategoryRequestFail(error.toString());

            }
        });

        requestQueue.add(request);
    }


    // Getters

    public Category getCategoryByTitle(String title) {
        for (Category c : mCategories) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                return c;
            }
        }
        return null;
    }

    public List<Category> getCodeCategories() {
        for (Category c : mCategories) {
            if (c.getId().equalsIgnoreCase("55d7429d475dfe0d0030e5d1")) {
                return c.getSubcategories();
            }
        }
        return null;
    }

    public List<Category> getCodeSortedCategories() {
        for (Category c : mCategories) {
            if (c.getId().equalsIgnoreCase("55d7429d475dfe0d0030e5d1")) {
                List<Category> subcategories = c.getSubcategories();
                Collections.sort(subcategories, new CategoryComparator());
                return subcategories;
            }
        }
        return null;
    }



    // Listeners
    public interface CategoryRequestListener {
        void onCategoryRequestSuccess(List<Category> categories);

        void onCategoryRequestFail(String errorMsg);
    }


    public List<Category> getSortedCategories() {
        Collections.sort(mCategories, new CategoryComparator());
        return mCategories;
    }


    private class CategoryComparator implements Comparator<Category> {

        @Override
        public int compare(Category c1, Category c2) {
            if (c1.getTitle() != null && c2.getTitle() != null) {
                return c1.getTitle().compareTo(c2.getTitle());
            }

            return 0;
        }

    }


}
