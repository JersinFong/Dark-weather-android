package com.example.weathersearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.weathersearch.Adapter.RecyclerViewAdapter;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.Currently;

import static com.example.weathersearch.Common.Common.setMainImage;


public class TodayFragment extends Fragment {

    private Currently curr;
    private static final String TAG = "TodayFragment";
    private TextView windSpeed, pressure, precipitation,
    temperature, summary, humidity, visibility, cloudCover,
    ozone;
    private ImageView img;
    public TodayFragment(Currently curr) {
        // Required empty public constructor
        this.curr = curr;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        windSpeed = view.findViewById(R.id.textView13);
        pressure = view.findViewById(R.id.textView17);
        precipitation = view.findViewById(R.id.textView23);
        temperature = view.findViewById(R.id.textView25);
        summary = view.findViewById(R.id.textView28);
        humidity = view.findViewById(R.id.textView29);
        visibility = view.findViewById(R.id.textView31);
        cloudCover = view.findViewById(R.id.textView33);
        ozone = view.findViewById(R.id.textView35);
        img = view.findViewById(R.id.imageView13);

        Common.setMainImage(curr.getIcon(), img);
        temperature.setText((int)curr.getTemperature() + " \u00B0F");
        summary.setText(curr.getIcon().replace('-', ' '));
        humidity.setText((int)(curr.getHumidity() * 100) + "%");
        windSpeed.setText(String.format("%.2f", curr.getWindSpeed()) + " mph" );
        visibility.setText(String.format("%.2f",curr.getVisibility()) + " km" );
        pressure.setText(String.format("%.2f", curr.getPressure()) + " mb" );
        precipitation.setText(String.format("%.2f", curr.getPrecipIntensity()) + " mmph" );
        cloudCover.setText(String.format("%.2f", curr.getCloudCover()) + "%" );
        ozone.setText(String.format("%.2f", curr.getOzone()) + " DU" );


        super.onViewCreated(view, savedInstanceState);

    }

}
