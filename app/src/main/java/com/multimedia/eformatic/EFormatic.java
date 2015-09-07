package com.multimedia.eformatic;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Sandra on 28/08/15.
 */
public class EFormatic extends Application {

    public static Context APP_CONTEXT;
    public static Resources RESOURCES;

    public final static String API_URL = "http://eas.elephorm.com/api/v1/";
    public final static int API_TIMEOUT = 5000;

    @Override
    public void onCreate() {
        super.onCreate();

        APP_CONTEXT = getApplicationContext();
        RESOURCES = getResources();
    }

    public static Context getAppContext() {
        return APP_CONTEXT;
    }
}
