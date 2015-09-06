package com.multimedia.eformatic.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.CategoryAdapter;
import com.multimedia.eformatic.managers.CategoryManager;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoriesActivity extends ActionBarActivity implements View.OnClickListener, CategoryManager.CategoryRequestListener {


    private RecyclerView mCategoryRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);

        mCategoryRecycleView = (RecyclerView) findViewById(R.id.categories_recyclerview);


        mCategoryRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mCategoryRecycleView.setLayoutManager(mLayoutManager);

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
            mCategoryRecycleView.setAdapter(mAdapter);
        }
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
                List<Category> codeCategories = CategoryManager.getInstance().getCodeSortedCategories();
                mAdapter = new CategoryAdapter(CategoriesActivity.this, codeCategories);
                if (mCategoryRecycleView != null) {
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


}
