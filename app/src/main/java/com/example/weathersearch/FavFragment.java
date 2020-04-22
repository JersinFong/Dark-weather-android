package com.example.weathersearch;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weathersearch.Adapter.RecyclerViewAdapter;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.WeatherResult;
import com.example.weathersearch.Retrofit.RetrofitClient;
import com.example.weathersearch.Retrofit.WeatherMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.example.weathersearch.Common.Common.setMainImage;


public class FavFragment extends Fragment {

    private static final String TAG = "FavFragment";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CompositeDisposable compositeDisposable;
    private WeatherMap weatherMap;
    private ImageView mainImage;
    private TextView temp, summary, location, humidity, windSpeed, visibility, pressure;
    private ConstraintLayout currentPanel;
    private ConstraintLayout progressBar;
    private FloatingActionButton fab;
    private CardView cardView;
    private WeatherResult weather;
    private int position;
    private String cityName;
    private String cityLoc;
    private boolean addAction;
    private SharedPreferences sharedPref;

    public FavFragment(int position, String cityName, String cityLoc) {
        this.position = position;
        this.cityName = cityName;
        this.cityLoc = cityLoc;
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        weatherMap = retrofit.create(WeatherMap.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        temp = view.findViewById(R.id.textView);
        mainImage = view.findViewById(R.id.imageView2);
        summary = view.findViewById(R.id.textView3);
        location = view.findViewById(R.id.textView4);
        humidity = view.findViewById(R.id.textView5);
        windSpeed = view.findViewById(R.id.textView7);
        visibility = view.findViewById(R.id.textView9);
        pressure = view.findViewById(R.id.textView11);
        currentPanel = view.findViewById(R.id.currPanel);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.list_recycler_view);
        cardView = view.findViewById(R.id.cardView);
        fab = view.findViewById(R.id.floatingActionButton);
        fab.hide();
        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.cityStore), Context.MODE_PRIVATE);
        if(position < 0 && sharedPref.getString(this.cityName, null) == null){
            this.addAction = true;
        }
        getWeatherInfo();

    }
    private void getWeatherInfo() {
        if(this.position != 0){
            fab.show();
            if(!this.addAction){
                fab.setImageResource(R.drawable.map_marker_minus);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if(FavFragment.this.addAction){
                        editor.putString(FavFragment.this.cityName, FavFragment.this.cityLoc);
                        MainActivity.addTab(FavFragment.this.cityName, FavFragment.this.cityLoc);
                        fab.setImageResource(R.drawable.map_marker_minus);
                        Toast.makeText(view.getContext(), cityName + " was  added to favorites", Toast.LENGTH_LONG).show();
                    }else{
                        editor.remove(FavFragment.this.cityName);
                        if(FavFragment.this.position == -1){
                            for(int i = 1; i < Common.cityList.size(); i++){
                                if(Common.cityList.get(i)[0].equals(FavFragment.this.cityName)){
                                    MainActivity.removeTab(i);
                                }
                            }
                        }
                        else MainActivity.removeTab(FavFragment.this.position);
                        fab.setImageResource(R.drawable.map_marker_plus);
                        Toast.makeText(view.getContext(), cityName + " was  removed from favorites", Toast.LENGTH_LONG).show();
                    }
                    editor.apply();
                    FavFragment.this.addAction = !FavFragment.this.addAction;
                }
            });
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.search_txt = FavFragment.this.cityName;
                Intent intent = new Intent(getActivity() , DetailsActivity.class);
                intent.putExtra("weather_pass", FavFragment.this.weather);
                startActivity(intent);
            }
        });

        location.setText(this.cityName);
        Common.search_txt = this.cityName;
        //subscribe HTTP request
        compositeDisposable.add(weatherMap.getWeatherByLatLng(this.cityLoc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        //load info(first two cards)
                        temp.setText((int)weatherResult.getData().getCurrently().getTemperature() + " \u00B0F");
                        setMainImage(weatherResult.getData().getCurrently().getIcon(), mainImage);
                        summary.setText(weatherResult.getData().getCurrently().getSummary());
                        humidity.setText((int)(weatherResult.getData().getCurrently().getHumidity() * 100) + "%");
                        windSpeed.setText(String.format("%.2f", weatherResult.getData().getCurrently().getWindSpeed()) + " mph" );
                        visibility.setText(String.format("%.2f",weatherResult.getData().getCurrently().getVisibility()) + " km" );
                        pressure.setText(String.format("%.2f", weatherResult.getData().getCurrently().getPressure()) + " mb" );

                        //set third card
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        mAdapter = new RecyclerViewAdapter(weatherResult.getData().getDaily().getData());
                        recyclerView.setAdapter(mAdapter);
                        //show panel
                        currentPanel.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        FavFragment.this.weather = weatherResult;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Fetch Error", "accept: " + throwable.getMessage());
                    }
                })
        );
    }




}
