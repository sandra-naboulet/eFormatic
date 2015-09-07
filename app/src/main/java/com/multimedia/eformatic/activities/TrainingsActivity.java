package com.multimedia.eformatic.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.TrainingAdapter;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Training;
import com.multimedia.eformatic.model.TrainingHistory;

import java.util.List;

public class TrainingsActivity extends ActionBarActivity implements View.OnClickListener, TrainingManager.TrainingRequestListener {

    private ProgressBar mProgressbar;
    private RecyclerView mTrainingRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mSubcategoryID;
    private String mSubcategoryTitle;
    private List<Training> mTrainings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        mSubcategoryID = extras.getString("subcategoryID");
        mSubcategoryTitle = extras.getString("subcategoryTitle");

        setContentView(R.layout.activity_trainings);

        mTrainingRecycleView = (RecyclerView) findViewById(R.id.trainings_recyclerview);
        mProgressbar = (ProgressBar) findViewById(R.id.trainings_progressbar);

        mTrainingRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mTrainingRecycleView.setLayoutManager(mLayoutManager);


        getSupportActionBar().setTitle(mSubcategoryTitle);

        TrainingManager.getInstance().startGetTrainings(mSubcategoryID, this);

    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            //mTrainingRecycleView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        // Check if a training is done, and it should display alert_credits
        if (mTrainings != null && !mTrainings.isEmpty()) {
            for (Training t : mTrainings) {
                TrainingHistory hist = HistoryManager.getInstance().getTrainingHistory(t.getEAN());
                if (hist != null) {
                    if (hist.isDone() && hist.shouldAlert()) {
                        hist.shouldShowAlert(false);
                        HistoryManager.getInstance().saveTrainingHistory(t.getEAN(), hist);
                        showCongratsAlert(t);
                        break;
                    }
                }
            }
        }
    }

    private void showCongratsAlert(final Training training) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_congrats);

        TextView formName = (TextView) dialog.findViewById(R.id.alert_formation_name);
        formName.setText(training.getTitle());

        Button okbutton = (Button) dialog.findViewById(R.id.alert_button);

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("FB", "success");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB", "error");
            }

            @Override
            public void onCancel() {
                Log.d("FB", "cancel");
            }
        });

        Button sharebutton = (Button) dialog.findViewById(R.id.alert_share_button);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String trainingUrl = training.getProductUrl();
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(trainingUrl))
                        .build();
                shareDialog.show(content);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onGetTrainingsRequestSuccess(final List<Training> trainings) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTrainings = trainings;
                mAdapter = new TrainingAdapter(TrainingsActivity.this, trainings);
                if (mTrainingRecycleView != null) {
                    mProgressbar.setVisibility(View.GONE);
                    mTrainingRecycleView.setAdapter(mAdapter);
                }
            }
        });

    }

    @Override
    public void onTrainingsRequestFail(final String errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrainingsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
