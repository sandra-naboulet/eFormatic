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
public class HistoryManager {

    public static final String HISTORY = "history";

    public static HistoryManager sInstance = null;


    public static HistoryManager getInstance() {
        if (sInstance == null) {
            return new HistoryManager();
        }
        return sInstance;
    }

    public TrainingHistory getTrainingHistory(String ean) {
        SharedPreferences history = EFormatic.APP_CONTEXT.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);

        if (history == null) {
            return null;
        }

        String trainingHistory = history.getString(ean, null);

        if (trainingHistory == null) {
            return null;
        }

        try {
            JSONObject historyJSON = new JSONObject(trainingHistory);
            TrainingHistory hist = new TrainingHistory(historyJSON);
            return hist;
        } catch (JSONException e) {
            return null;
        }

    }

    public void saveTrainingHistory(String ean, TrainingHistory history) {

        SharedPreferences prefs = EFormatic.APP_CONTEXT.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);

        if (history == null || prefs == null) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ean, history.toJSON().toString());
        editor.commit();
    }

    private void saveTraining(Training t) {
        TrainingHistory history = new TrainingHistory();
        history.setDone(false);
        history.setQCMAvailable(t.getQcmUrl() != null && !t.getQcmUrl().isEmpty());
        history.setQCMDone(false);
        history.setVideoCount(t.getVideoCount());

        saveTrainingHistory(t.getEAN(), history);
    }

    public void saveTrainings(List<Training> trainings) {
        for (Training t : trainings) {
            if (getTrainingHistory(t.getEAN()) == null) {
                saveTraining(t);
            }
        }
    }

    public void setAsWatched(String ean, Item item) {
        TrainingHistory t = getTrainingHistory(ean);

        if (t == null || item.getVideo() == null) {
            return;
        }

        if (!t.getWatchedVideos().contains(item.getVideo().getId())) {
            t.getWatchedVideos().add(item.getVideo().getId());
        }

        // Update credit
        CreditsManager.getInstance().removeCredit(item.getNbCredits(), ean, item.getVideo().getId());

        // Check if training is done
        if (t.getWatchedVideos().size() == t.getVideoCount()) {
            if (!t.isQCMAvailable() || (t.isQCMAvailable() && t.isQCMDone())) {
                t.shouldShowAlert(true);
                t.setDone(true);

            }
        }

        saveTrainingHistory(ean, t);
    }

    public boolean isWatched(String ean, String videoId) {
        TrainingHistory hist = getTrainingHistory(ean);
        if (hist != null) {
            return hist.getWatchedVideos().contains(videoId);
        }

        return false;
    }

    public boolean trainingIsDone(String ean) {
        TrainingHistory t = getTrainingHistory(ean);

        if (t == null) {
            return false;
        }

        return t.isDone();
    }

    public void setQcmDone(String ean) {
        TrainingHistory t = getTrainingHistory(ean);

        if (t == null) {
            return;
        }

        t.setQCMDone(true);

        if (t.getWatchedVideos().size() == t.getVideoCount()) {
            t.setDone(true);
            t.shouldShowAlert(true);
        }

        saveTrainingHistory(ean, t);
    }

    public boolean qcmIsDone(String ean){
        TrainingHistory t = getTrainingHistory(ean);

        if (t == null) {
            return false;
        }

        return t.isQCMDone();

    }


    public float getProgress(String ean) {
        TrainingHistory t = getTrainingHistory(ean);

        if (t == null) {
            return 0;
        }

        int totalVideos = t.getVideoCount();
        int watchedVideos = t.getWatchedVideos().size();

        return (float)watchedVideos / (float)totalVideos;
    }


}
