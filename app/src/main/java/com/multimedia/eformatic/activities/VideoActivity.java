package com.multimedia.eformatic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.appevents.AppEventsLogger;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Item;
import com.multimedia.eformatic.model.Training;

public class VideoActivity extends ActionBarActivity {


    private Training mTraining;
    private Item mItem;

    private VideoView mVideoView;
    private RelativeLayout mLoadingScreen;


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
            getSupportActionBar().setTitle(mItem.getTitle());
        }

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mLoadingScreen = (RelativeLayout) findViewById(R.id.video_loading_screen);

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
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                mLoadingScreen.setVisibility(View.GONE);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        mVideoView.start();

        // Set as watched in history
        HistoryManager.getInstance().setAsWatched(mTraining.getEAN(), mItem);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}
