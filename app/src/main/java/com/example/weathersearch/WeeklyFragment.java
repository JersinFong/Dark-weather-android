package com.example.weathersearch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.Daily;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;



public class WeeklyFragment extends Fragment {
    private Daily daily;
    private static final String TAG = "WeeklyFragment";
    LineData data;
    public WeeklyFragment(Daily daily) {
        this.daily = daily;
        List<Entry> min = new ArrayList<>();
        List<Entry> max = new ArrayList<>();
        for(int i = 0; i < daily.getData().size(); i++){
            min.add(new Entry(i, (float) daily.getData().get(i).getTemperatureLow()));
            max.add(new Entry(i, (float) daily.getData().get(i).getTemperatureHigh()));
        }
        LineDataSet setMin = new LineDataSet(min, "Minimum Temperature");
        setMin.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setMax = new LineDataSet(max, "Maximum Temperature");
        setMax.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        setMin.setColor(0xFFBB86FC);
        setMax.setColor(0xFFFF8800);
        dataSets.add(setMin);
        dataSets.add(setMax);
        data = new LineData(dataSets);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView summary = view.findViewById(R.id.textView15);
        summary.setText(daily.getSummary());
        Common.setMainImage(daily.getIcon(), (ImageView) view.findViewById(R.id.imageView5));

        LineChart chart = view.findViewById(R.id.chart);
        chart.setData(data);
        chart.invalidate(); // refresh

        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getXAxis().setTextColor(Color.WHITE);
        Legend l = chart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setTextSize(16f);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }
}
