package com.multimedia.eformatic.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.multimedia.eformatic.adapters.TrainingAdapter;
import com.multimedia.eformatic.constants.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandra on 03/09/15.
 */


public class Training {
    String mTitle;
    String mSubtitle;
    String mEAN;
    String mHtmlDescription;
    String mProductUrl;
    int mDuration;
    boolean mDownload;
    String mQcmUrl;
    String mTeaserText;
    String mCategoryId;
    String mSubcategoryId;
    String mPictureUrl;
    Bitmap mPicture;
    //String mThumb;
    int mVideoCount;

    List<Item> mItems;

    TrainingAdapter mAdapter;

    public Training(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        this.mTitle = jsonObject.optString(Attributes.TITLE);
        this.mSubtitle = jsonObject.optString(Attributes.SUBTITLE);
        this.mEAN = jsonObject.optString(Attributes.EAN);
        this.mHtmlDescription = jsonObject.optString(Attributes.DESCRIPTION);
        this.mProductUrl = jsonObject.optString(Attributes.PRODUCT_URL);
        this.mDuration = jsonObject.optInt(Attributes.DURATION);
        this.mDownload = jsonObject.optBoolean(Attributes.CAN_DOWNLOAD);
        this.mQcmUrl = jsonObject.optString(Attributes.QCM);
        this.mTeaserText = jsonObject.optString(Attributes.TEASER);
        this.mCategoryId = jsonObject.optString(Attributes.CATEGORY);
        this.mSubcategoryId = jsonObject.optString(Attributes.SUBCATEGORY);
        this.mPictureUrl = jsonObject.optString(Attributes.POSTER);
        this.mVideoCount = jsonObject.optInt(Attributes.VIDEO_COUNT);
        this.mTitle = jsonObject.optString(Attributes.TITLE);


        mItems = new ArrayList<>();

        JSONArray array = jsonObject.optJSONArray(Attributes.ITEMS);
        if (mItems != null) {

            try {
                for (int i = 0; i < array.length(); i++) {

                    Item item = new Item(array.getJSONObject(i));
                    if (item.getType() != null) {
                        mItems.add(item);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getEAN() {
        return mEAN;
    }

    public String getHtmlDescription() {
        return mHtmlDescription;
    }

    public int getDuration() {
        return mDuration;
    }

    public boolean isDownload() {
        return mDownload;
    }

    public String getQcmUrl() {
        return mQcmUrl;
    }

    public String getTeaserText() {
        return mTeaserText;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public String getProductUrl(){
        return mProductUrl;
    }

    public int getVideoCount() {
        return mVideoCount;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void loadImage(TrainingAdapter adapter) {
        this.mAdapter = adapter;
        if (mPictureUrl != null && !mPictureUrl.isEmpty()) {
            new ImageLoadTask().execute(mPictureUrl);
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
                mPicture = bmp;
                if(mAdapter != null){
                    mAdapter.notifyDataSetChanged();
                }
            } else {
            }
        }

    }
}






