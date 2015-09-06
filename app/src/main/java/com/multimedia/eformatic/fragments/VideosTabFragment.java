package com.multimedia.eformatic.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.CategoryAdapter;
import com.multimedia.eformatic.adapters.ItemAdapter;
import com.multimedia.eformatic.adapters.TrainingAdapter;
import com.multimedia.eformatic.managers.CategoryManager;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Item;
import com.multimedia.eformatic.model.Training;

import java.util.List;

/**
 * Created by Sandra on 23/08/15.
 */
public class VideosTabFragment extends Fragment {


    private RecyclerView mVideoRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mEAN;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEAN = getArguments().getString("ean", "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_training_videos, container, false);


        mVideoRecycleView = (RecyclerView) v.findViewById(R.id.videos_recyclerview);
        mVideoRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mVideoRecycleView.setLayoutManager(mLayoutManager);


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        Training training = TrainingManager.getInstance().getTraining(mEAN);

        mAdapter = new ItemAdapter(getActivity(), mEAN, training.getItems());
        mVideoRecycleView.setAdapter(mAdapter);
    }


}