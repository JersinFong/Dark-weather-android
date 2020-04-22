package com.example.weathersearch.Adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weathersearch.Model.Datum3;
import com.example.weathersearch.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.weathersearch.Common.Common.setMainImage;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private List<Datum3>  mDataset;
    private static final String TAG = "RecyclerViewAdapter";
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<Datum3> myDataset) {
        mDataset = myDataset;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public TextView textView3;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView19);
            textView2 = itemView.findViewById(R.id.textView20);
            textView3 = itemView.findViewById(R.id.textView21);
            imageView = itemView.findViewById(R.id.imageView9);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Date date = new Date(mDataset.get(position).getTime() * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(date);
        holder.textView.setText(formattedDate);
        holder.textView2.setText(Integer.toString((int)mDataset.get(position).getTemperatureLow()));
        holder.textView3.setText(Integer.toString((int)mDataset.get(position).getTemperatureHigh()));
        setMainImage(mDataset.get(position).getIcon(), holder.imageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
