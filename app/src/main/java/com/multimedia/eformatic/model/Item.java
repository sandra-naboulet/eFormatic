package com.multimedia.eformatic.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.multimedia.eformatic.adapters.ItemAdapter;
import com.multimedia.eformatic.adapters.TrainingAdapter;
import com.multimedia.eformatic.constants.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sandra on 03/09/15.
 */


public class Item {

    public enum Type {
        CHAPTER("chapter"), VIDEO("video"), UNKNOWN("");

        private String mValue;

        Type(String value) {
            mValue = value;
        }

        public static Type fromString(String value) {
            if (value.equalsIgnoreCase("chapter")) {
                return CHAPTER;
            } else if (value.equalsIgnoreCase("video")) {
                return VIDEO;
            } else return UNKNOWN;
        }

    }

    private String mId;
    private String mTitle;
    private Type mType;
    private int mDuration;
    private String mPosterUrl;
    private int mNbCredits;
    private boolean mFree;
    private Bitmap mThumb;
    private String mThumbUrl;
    private Video mVideo;

    private ItemAdapter mAdapter;

    public Item(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        mId = jsonObject.optString(Attributes.ID);
        mTitle = jsonObject.optString(Attributes.TITLE);
        mType = Type.fromString(jsonObject.optString(Attributes.TYPE));
        mDuration = jsonObject.optInt(Attributes.DURATION);
        mPosterUrl = jsonObject.optString(Attributes.FIELD_POSTER);
        mFree = jsonObject.optBoolean(Attributes.FREE);
        mNbCredits = jsonObject.optInt(Attributes.NB_CREDITS);

        // Get thumb url
        JSONArray thumbArray = jsonObject.optJSONArray(Attributes.FIELD_THUMB);
        if (thumbArray != null && thumbArray.length() > 0) {
            JSONObject thumbObj = thumbArray.optJSONObject(0);
            if (thumbObj != null) {
                mThumbUrl = thumbObj.optString(Attributes.FILE_PATH);
            }
        }

        // Get video infos
        JSONArray array = jsonObject.optJSONArray(Attributes.FIELD_VIDEO);
        if (array != null && array.length() > 0) {
            try {
                mVideo = new Video(array.getJSONObject(0));
            } catch (JSONException e) {
            }
        }
    }


    public String getTitle() {
        return mTitle;
    }

    public Type getType() {
        return mType;
    }

    public String getDuration() {
        String durStr = "";
        int min = mDuration / 60;
        int s = mDuration % 60;
        int h = min / 60;
        if (h != 0) {
            durStr += h + " h ";
        }

        if (min != 0) {
            durStr += min + " min ";
        }

        if (s != 0) {
            durStr += s + " sec";
        }

        return durStr;
    }

    public String getId(){
        return mId;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public boolean isFree() {
        return mFree;
    }

    public int getNbCredits() {
        return mNbCredits;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public Bitmap getThumb() {
        return mThumb;
    }

    public Video getVideo() {
        return mVideo;
    }


    public void loadImage(ItemAdapter adapter) {
        this.mAdapter = adapter;
        if (mThumbUrl != null && !mThumbUrl.isEmpty()) {
            new ImageLoadTask().execute(mThumbUrl);
        }
    }


    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
        }

        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {


            URL url = null;
            try {
                url = new URL(param[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap bmp) {
            if (bmp != null) {
                mThumb = bmp;
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            } else {
            }
        }

    }
}
