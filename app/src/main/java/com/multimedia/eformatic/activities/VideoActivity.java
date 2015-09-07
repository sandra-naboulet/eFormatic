package com.multimedia.eformatic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.multimedia.eformatic.R;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Item;
import com.multimedia.eformatic.model.Training;

public class VideoActivity extends ActionBarActivity {


    private Training mTraining;
    private Item mItem;

    private VideoView mVideoView;


    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        Bundle extras = i.getExtras();

        String ean = extras.getString("ean");
        String itemId = extras.getString("itemId");


        mTraining = TrainingManager.getInstance().getTraining(ean);
        mItem = TrainingManager.getInstance().getItem(ean, itemId);


        setContentView(R.layout.activity_video);

        if (mTraining != null) {
            getSupportActionBar().setTitle(mTraining.getTitle());
        }

        mVideoView = (VideoView) findViewById(R.id.video_view);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
            mediaControls.setMinimumHeight(200);
        }

        Uri uri = Uri.parse(mItem.getVideo().getFilepath());
        mVideoView.setMediaController(mediaControls);
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        // Set as watched in history
        HistoryManager.getInstance().setAsWatched(mTraining.getEAN(), mItem);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
