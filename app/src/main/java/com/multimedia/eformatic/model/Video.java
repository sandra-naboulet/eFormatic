package com.multimedia.eformatic.model;

import com.multimedia.eformatic.constants.Attributes;

import org.json.JSONObject;

/**
 * Created by Sandra on 05/09/15.
 */
public class Video {

    private String mId;
    private int mDuration;
    private String mFilename;
    private String mFilepath;


    public Video(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        mId = jsonObject.optString(Attributes.ID);
        mDuration = jsonObject.optInt(Attributes.DURATION);
        mFilename = jsonObject.optString(Attributes.FILE_NAME);
        mFilepath = jsonObject.optString(Attributes.FILE_PATH);

    }

    public String getId(){
        return mId;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getFilename() {
        return mFilename;
    }

    public String getFilepath() {
        return mFilepath;
    }
}
