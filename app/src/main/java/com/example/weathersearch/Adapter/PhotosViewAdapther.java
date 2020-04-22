package com.example.weathersearch.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weathersearch.R;


import java.util.List;


public class PhotosViewAdapther extends RecyclerView.Adapter<PhotosViewAdapther.PhotosViewHolder> {
    
    private List<String> myDataset;
    private Context myContext;
    private static final String TAG = "PhotosViewAdapther";

    public PhotosViewAdapther(List<String> myDataset, Context myContext) {
        this.myDataset = myDataset;
        this.myContext = myContext;
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public PhotosViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView18);
        }

    }
    // Create new views (invoked by the layout manager)
    @Override
    public PhotosViewAdapther.PhotosViewHolder  onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_photoitem, parent, false);
        PhotosViewHolder vh = new PhotosViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, int position) {
        Glide.with(myContext)
                .asBitmap()
                .load(myDataset.get(position))
                .into(holder.img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
