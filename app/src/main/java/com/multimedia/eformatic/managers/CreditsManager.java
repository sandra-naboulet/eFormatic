package com.multimedia.eformatic.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.model.Item;
import com.multimedia.eformatic.model.Training;
import com.multimedia.eformatic.model.TrainingHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sandra on 05/09/15.
 */
public class CreditsManager {

    public static final String HISTORY = "history";
    public static final String CREDITS = "credits";

    public static CreditsManager sInstance = null;


    public static CreditsManager getInstance() {
        if (sInstance == null) {
            return new CreditsManager();
        }
        return sInstance;
    }

    public void saveCredits(int nbCredit) {
        SharedPreferences prefs = EFormatic.APP_CONTEXT.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);

        if (prefs == null) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CREDITS, nbCredit);
        editor.commit();

    }

    public void removeCredit(int nbCredits, String ean, String videoId) {

        if(nbCredits == -1){
            nbCredits = 0;
        }
        SharedPreferences prefs = EFormatic.APP_CONTEXT.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);

        if (prefs == null) {
            return;
        }

        // Check if video is already watched
        TrainingHistory hist = HistoryManager.getInstance().getTrainingHistory(ean);

        if(hist != null){
            if(hist.getWatchedVideos().contains(videoId)){
                return;
            }
        }

        int currentCredits = prefs.getInt(CREDITS, 0);
        saveCredits(currentCredits - nbCredits);
    }


    public boolean canWatch(String ean, String itemId){
       Item item = TrainingManager.getInstance().getItem(ean, itemId);
        if(item != null){
            if(item.getVideo() != null){
                if(HistoryManager.getInstance().isWatched(ean, item.getVideo().getId())){
                    return true;
                }
                else {
                    int currentCredits = getCurrentCredits();
                    int videoCredit = item.getNbCredits();
                    return currentCredits - videoCredit >= 0;
                }
            }
        }
        return true;
    }


    public int getCurrentCredits() {
        SharedPreferences prefs = EFormatic.APP_CONTEXT.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);

        if (prefs == null) {
            return 0;
        }

        return prefs.getInt(CREDITS, 0);
    }


}
