package com.multimedia.eformatic.model;

import com.multimedia.eformatic.constants.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandra on 29/08/15.
 */
public class TrainingHistory {

    private boolean mDone;
    private int mVideoCount;
    private List<String> mVideosWatched = new ArrayList<>();
    private boolean mQCMAvailable;
    private boolean mQCMDone;
    private boolean mShouldAlert;

    public TrainingHistory() {

    }

    public TrainingHistory(JSONObject json) {
        if (json == null) {
            return;
        }

        mDone = json.optBoolean(Attributes.DONE);
        mVideoCount = json.optInt(Attributes.VIDEO_COUNT);
        mShouldAlert = json.optBoolean(Attributes.ALERT);
        mQCMAvailable = json.optBoolean(Attributes.QCM_AVAILABLE);
        mQCMDone = json.optBoolean(Attributes.QCM_DONE);

        mVideosWatched = new ArrayList<>();
        JSONArray array = json.optJSONArray(Attributes.VIDEO_WATCHED);
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                mVideosWatched.add(array.optString(i));
            }
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(Attributes.DONE, mDone);
            json.put(Attributes.VIDEO_COUNT, mVideoCount);
            json.put(Attributes.QCM_AVAILABLE, mQCMAvailable);
            json.put(Attributes.ALERT, mShouldAlert);
            json.put(Attributes.QCM_DONE, mQCMDone);

            JSONArray array = new JSONArray();
            for (String id : mVideosWatched) {
                array.put(id);
            }
            json.put(Attributes.VIDEO_WATCHED, array);


        } catch (JSONException e) {
        }
        return json;
    }

    public boolean isDone() {
        return mDone;
    }

    public boolean shouldAlert() {
        return mShouldAlert;
    }

    public void shouldShowAlert(boolean show) {
        this.mShouldAlert = show;
    }

    public int getVideoCount() {
        return mVideoCount;
    }

    public List<String> getWatchedVideos() {
        return mVideosWatched;
    }

    public boolean isQCMAvailable() {
        return mQCMAvailable;
    }

    public boolean isQCMDone() {
        return mQCMDone;
    }

    public void setDone(boolean mDone) {
        this.mDone = mDone;
    }

    public void setVideoCount(int count) {
        this.mVideoCount = count;
    }

    public void setQCMAvailable(boolean mQCMAvailable) {
        this.mQCMAvailable = mQCMAvailable;
    }

    public void setQCMDone(boolean mQCMDone) {
        this.mQCMDone = mQCMDone;
    }
}
