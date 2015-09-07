package com.multimedia.eformatic.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.CategoryAdapter;
import com.multimedia.eformatic.managers.CategoryManager;
import com.multimedia.eformatic.model.Category;

import java.util.List;

public class CategoriesActivity extends ActionBarActivity implements View.OnClickListener, CategoryManager.CategoryRequestListener {

    private ProgressBar mProgressBar;
    private RecyclerView mCategoryRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);

        mCategoryRecycleView = (RecyclerView) findViewById(R.id.categories_recyclerview);
        mProgressBar = (ProgressBar) findViewById(R.id.categories_progressbar);

        mCategoryRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mCategoryRecycleView.setLayoutManager(mLayoutManager);

        mToast = Toast.makeText(this, R.string.click_again_for_quit, Toast.LENGTH_SHORT);

        getSupportActionBar().setTitle(EFormatic.RESOURCES.getString(R.string.courses));

        CategoryManager.getInstance().startGetCategories(this);
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mAdapter != null) {
            //mTrainingRecycleView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onCategoryRequestSuccess(final List<Category> categories) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                List<Category> infoCategories = CategoryManager.getInstance().getInfoSortedCategories();
                mAdapter = new CategoryAdapter(CategoriesActivity.this, infoCategories);
                if (mCategoryRecycleView != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mCategoryRecycleView.setAdapter(mAdapter);
                }
            }
        });

    }

    @Override
    public void onCategoryRequestFail(final String errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CategoriesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Toast mToast = null;
    private boolean mNotifyBeforeQuit = true;
    private boolean mQuitByUserAction = false;

    @Override
    public void onBackPressed() {


        mQuitByUserAction = true;
        if (mNotifyBeforeQuit) {
            mToast.show();
            mNotifyBeforeQuit = false;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNotifyBeforeQuit = true;
                }
            }, 2000);
        } else {
            mToast.cancel();
            LoginManager.getInstance().logOut();
            finish();

        }

       // super.onBackPressed();
    }


}
