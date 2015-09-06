package com.multimedia.eformatic.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.multimedia.eformatic.fragments.QcmTabFragment;
import com.multimedia.eformatic.fragments.VideosTabFragment;

/**
 * Created by Sandra on 23/08/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence mTitles[];
    private int mNbTabs;
    private String mEAN;
    private String mQcmUrl;

    public ViewPagerAdapter(FragmentManager fm, CharSequence titles[], int nbTabs, String trainingEAN, String qcmUrl) {
        super(fm);

        this.mTitles = titles;
        this.mNbTabs = nbTabs;
        this.mEAN = trainingEAN;
        mQcmUrl = qcmUrl;
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            VideosTabFragment tab1 = new VideosTabFragment();
            Bundle extras = new Bundle();
            extras.putString("ean", mEAN);
            tab1.setArguments(extras);
            return tab1;
        } else {
            QcmTabFragment tab2 = new QcmTabFragment();
            Bundle extras = new Bundle();
            extras.putString("ean", mEAN);
            extras.putString("qcm", mQcmUrl);
            tab2.setArguments(extras);
            return tab2;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mNbTabs;
    }
}