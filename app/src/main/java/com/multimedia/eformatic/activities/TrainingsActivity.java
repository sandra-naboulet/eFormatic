package com.multimedia.eformatic.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.adapters.CategoryAdapter;
import com.multimedia.eformatic.adapters.TrainingAdapter;
import com.multimedia.eformatic.managers.CategoryManager;
import com.multimedia.eformatic.managers.CreditsManager;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.managers.TrainingManager;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;
import com.multimedia.eformatic.model.TrainingHistory;

import java.util.List;

public class TrainingsActivity extends ActionBarActivity implements View.OnClickListener, TrainingManager.TrainingRequestListener {


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

        mTrainingRecycleView = (RecyclerView) findViewById(R.id.training_recyclerview);


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
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mTrainingRecycleView.setAdapter(mAdapter);
        }

        // Check if a training is done, and it should display alert
        if (mTrainings != null && !mTrainings.isEmpty()) {
            for (Training t : mTrainings) {
                TrainingHistory hist = HistoryManager.getInstance().getTrainingHistory(t.getEAN());
                if (hist != null) {
                    if (hist.isDone() && hist.shouldAlert()) {
                        showCongratsAlert(t.getTitle());
                        hist.shouldShowAlert(false);
                        HistoryManager.getInstance().saveTrainingHistory(t.getEAN(), hist);
                        break;
                    }
                }
            }
        }
    }

    private void showCongratsAlert(String trainingName) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert);

        TextView title = (TextView) dialog.findViewById(R.id.alert_title);
        TextView message = (TextView) dialog.findViewById(R.id.alert_message);
        ImageView icon = (ImageView) dialog.findViewById(R.id.alert_icon);

        title.setText(EFormatic.RESOURCES.getString(R.string.alert_congrats_title));
        message.setText(EFormatic.RESOURCES.getString(R.string.alert_congrats_message, trainingName));

        icon.setImageResource(R.mipmap.medal);

        Button button = (Button) dialog.findViewById(R.id.alert_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
