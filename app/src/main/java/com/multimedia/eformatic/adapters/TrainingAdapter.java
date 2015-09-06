package com.multimedia.eformatic.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multimedia.eformatic.R;
import com.multimedia.eformatic.activities.TrainingActivity;
import com.multimedia.eformatic.activities.TrainingsActivity;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Sandra on 03/09/15.
 */
public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    private Context mContext;
    private List<Training> mTrainings;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mNbVideos;
        public ImageView mPicture;
        public ImageView mMedal;
        public RelativeLayout mProgress;

        public String mEAN;
        public String mQCM;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.item_training_title);
            mPicture = (ImageView) v.findViewById(R.id.item_training_img);
            mNbVideos = (TextView) v.findViewById(R.id.item_training_nb_video);
            mMedal = (ImageView) v.findViewById(R.id.item_training_medal);
            mProgress = (RelativeLayout) v.findViewById(R.id.item_training_progress);
        }
    }


    public TrainingAdapter(Context context, List<Training> trainings) {

        this.mContext = context;
        this.mTrainings = trainings;
        /*for (Training t : mTrainings){
            t.loadImage(this);
        }*/
    }


    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training, parent, false);

        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrainingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ean", vh.mEAN);
                bundle.putString("qcm", vh.mQCM);
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Training t = mTrainings.get(position);
        if (t.getPicture() == null) {
            t.loadImage(this);
        }

        holder.mEAN = t.getEAN();
        holder.mQCM = t.getQcmUrl();
        holder.mTitle.setText(t.getTitle());
        holder.mNbVideos.setText(t.getVideoCount() + " vidéos");
        holder.mPicture.setImageBitmap(t.getPicture());

        boolean isDone = HistoryManager.getInstance().trainingIsDone(t.getEAN());
        holder.mMedal.setVisibility(isDone ? View.VISIBLE : View.GONE);

        // Progress
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        p.weight = HistoryManager.getInstance().getProgress(t.getEAN());
        float w = p.weight;
        holder.mProgress.setLayoutParams(p);
    }


    @Override
    public int getItemCount() {
        return mTrainings.size();
    }


}
