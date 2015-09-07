package com.multimedia.eformatic.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.activities.VideoActivity;
import com.multimedia.eformatic.managers.CreditsManager;
import com.multimedia.eformatic.managers.HistoryManager;
import com.multimedia.eformatic.model.Item;

import java.util.List;

/**
 * Created by Sandra on 03/09/15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private String mEAN;
    private List<Item> mItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public String mItemId;
        public boolean mIsVideo;
        public int mNbCredits;

        public TextView mTitle;
        public TextView mDuration;
        public TextView mCredits;
        public ImageView mThumb;
        public RelativeLayout mThumbContainer;
        public RelativeLayout mWatchedScreen;


        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.item_item_title);
            mDuration = (TextView) v.findViewById(R.id.item_item_duration);
            mCredits = (TextView) v.findViewById(R.id.item_item_credit);
            mThumb = (ImageView) v.findViewById(R.id.item_item_img);
            mThumbContainer = (RelativeLayout) v.findViewById(R.id.item_item_thumb_container);
            mWatchedScreen = (RelativeLayout) v.findViewById(R.id.item_item_watched_screen);
        }
    }


    public ItemAdapter(Context context, String ean, List<Item> items) {
        this.mContext = context;
        this.mEAN = ean;

        if (items != null && items.size() > 1 && (items.get(0).getType() == Item.Type.CHAPTER)) {
            // THis is the training title, no need to display it
            items.remove(0);
        }
        this.mItems = items;
    }


    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        final ViewHolder vh = new ViewHolder(v);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ean", mEAN);
                bundle.putString("itemId", vh.mItemId);
                intent.putExtras(bundle);
                if (vh.mIsVideo) {
                    boolean canWatch = CreditsManager.getInstance().canWatch(mEAN, vh.mItemId);

                    if (canWatch) {
                        mContext.startActivity(intent);
                    } else {
                        showCreditAlert();
                    }
                }
            }
        });


        return vh;
    }


    private void showCreditAlert() {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.alert_credits);

        TextView message = (TextView) dialog.findViewById(R.id.alert_message);
        message.setText(EFormatic.RESOURCES.getString(R.string.alert_credit_message, CreditsManager.getInstance().getCurrentCredits()));

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Item item = mItems.get(position);

        if (item.getThumb() == null) {
            item.loadImage(this);
        }

        holder.mItemId = item.getId();
        holder.mIsVideo = item.getVideo() != null;
        holder.mNbCredits = item.getNbCredits();

        holder.mTitle.setText(item.getTitle());
        holder.mDuration.setText(item.getDuration());

        holder.mThumbContainer.setVisibility(item.getType() == Item.Type.CHAPTER ? View.GONE : View.VISIBLE);
        holder.mCredits.setVisibility(item.getType() == Item.Type.CHAPTER ? View.GONE : View.VISIBLE);
        holder.mDuration.setVisibility(item.getType() == Item.Type.CHAPTER ? View.GONE : View.VISIBLE);

        boolean isFree = item.isFree();
        holder.mCredits.setText(isFree ? EFormatic.RESOURCES.getString(R.string.free) : EFormatic.RESOURCES.getString(R.string.credits, item.getNbCredits()));
        holder.mCredits.setTextColor(EFormatic.RESOURCES.getColor(isFree ? R.color.green : R.color.orange));
        holder.mThumb.setImageBitmap(item.getThumb());

        if (item.getVideo() != null) {
            boolean isWatched = HistoryManager.getInstance().isWatched(mEAN, item.getVideo().getId());
            holder.mWatchedScreen.setVisibility(isWatched ? View.VISIBLE : View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


}
