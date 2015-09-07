package com.multimedia.eformatic.managers;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.constants.Urls;
import com.multimedia.eformatic.model.Item;
import com.multimedia.eformatic.model.Training;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sandra on 29/08/15.
 */
public class TrainingManager {

    public static TrainingManager sInstance = null;

    private RequestQueue mRequestQueue;
    private Parser mParser;

    // Datas
    private List<Training> mTrainings = new ArrayList<Training>();

    // Listeners
    private TrainingRequestListener mListener;

    private TrainingManager() {
        mParser = new Parser();
        mRequestQueue = Volley.newRequestQueue(EFormatic.getAppContext());
    }


    public static synchronized TrainingManager getInstance() {
        if (sInstance == null) {
            sInstance = new TrainingManager();
        }

        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;

    }

    /**
     * Get training
     *
     * @param listener
     */
    public void startGetTrainings(String subcategoryId, TrainingRequestListener listener) {

        mListener = listener;

        RequestQueue requestQueue = TrainingManager.getInstance().getRequestQueue();

        String url = Urls.GET_SUBCATEGORIES + subcategoryId + Urls.TRAININGS;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("VOLLEY", "response : " + response);
                mTrainings.clear();

                if (mParser.parseTrainings(response)) {
                    mTrainings = mParser.getTrainings();
                    saveTrainings();
                    mListener.onGetTrainingsRequestSuccess(mTrainings);
                } else {
                    mListener.onTrainingsRequestFail("Error while parsing trainings");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "error " + error);
                mListener.onTrainingsRequestFail(error.toString());

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                EFormatic.API_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }


    // Save history
    public void saveTrainings() {
        if (mTrainings != null && !mTrainings.isEmpty()) {
            HistoryManager.getInstance().saveTrainings(mTrainings);
        }
    }


    // Listeners
    public interface TrainingRequestListener {
        void onGetTrainingsRequestSuccess(List<Training> trainings);

        void onTrainingsRequestFail(String errorMsg);
    }


    public List<Training> getTrainings() {
        return mTrainings;
    }

    public Training getTraining(String ean) {
        Training training = null;
        for (Training t : mTrainings) {
            if (t.getEAN().equalsIgnoreCase(ean)) {
                training = t;
                break;
            }
        }

        return training;
    }

    public Item getItem(String ean, String itemId) {
        Training t = getTraining(ean);

        if (t == null) {
            return null;
        }

        for (Item item : t.getItems()) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                return item;
            }
        }

        return null;
    }

}
