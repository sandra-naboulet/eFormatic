package com.multimedia.eformatic.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.appevents.AppEventsLogger;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.ViewPagerAdapter;
import com.multimedia.eformatic.google.SlidingTabLayout;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Training;

/**
 * Created by Sandra on 23/08/15.
 */
public class TrainingActivity extends ActionBarActivity {

    // Declaring Your View and Variables


    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SlidingTabLayout mTabs;
    private CharSequence mTitles[] = {"Videos", "QCM"};
    private int mNbTabs = 2;

    private String mEAN;
    private String mQCM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        mEAN = extras.getString("ean");
        mQCM = extras.getString("qcm");

        getSupportActionBar().setTitle(TrainingManager.getInstance().getTraining(mEAN).getTitle());


        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mNbTabs, mEAN, mQCM);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);

        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.salmon);
            }
        });

        mTabs.setViewPager(mViewPager);


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

}
