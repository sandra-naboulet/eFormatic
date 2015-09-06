package com.multimedia.eformatic.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.activities.CategoriesActivity;
import com.multimedia.eformatic.activities.TrainingsActivity;
import com.multimedia.eformatic.model.Category;
import com.multimedia.eformatic.model.Training;

import java.util.List;

/**
 * Created by Sandra on 03/09/15.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Category> mCategories;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public String mID;
        public TextView mTitle;
        public TextView mDesc;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView)v.findViewById(R.id.item_category_title);
            mDesc = (TextView)v.findViewById(R.id.item_category_desc);
        }

    }


    public CategoryAdapter(Context context, List<Category> categories) {
        mContext = context;
        mCategories = categories;
    }


    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrainingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("subcategoryID", vh.mID);
                bundle.putString("subcategoryTitle", vh.mTitle.getText().toString());
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mID = mCategories.get(position).getId();
        holder.mTitle.setText(mCategories.get(position).getTitle());
        holder.mDesc.setText(mCategories.get(position).getDescription());



    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
