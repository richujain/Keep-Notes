package com.example.keepnotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by User on 1/17/2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<byte[]> mImageUrls = new ArrayList<>();
    private ArrayList<Integer> mId = new ArrayList<>();
    private Context mContext;

    public NotesAdapter(Context context, ArrayList<Integer> id, ArrayList<String> names,ArrayList<String> descriptions, ArrayList<byte[]> imageUrls) {
        mNames = names;
        mDescription = descriptions;
        mId = id;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_notes_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);


        holder.name.setText(mNames.get(position));
        holder.name.setText(mDescription.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, AddNotes.class);
                myIntent.putExtra("categoryId", mId.get(position));
                ((Activity)mContext).startActivity(myIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.task_imageview_widget);
            this.name = itemView.findViewById(R.id.task_name_widget);
            this.description = itemView.findViewById(R.id.task_description_widget);
        }
    }
}