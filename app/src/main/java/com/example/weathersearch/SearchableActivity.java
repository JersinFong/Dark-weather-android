package com.example.weathersearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.weathersearch.Adapter.ViewSearchPageAdapter;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.LocationResult;
import com.example.weathersearch.Retrofit.RetrofitClient;
import com.example.weathersearch.Retrofit.WeatherMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "SearchableActivity";
    private CompositeDisposable compositeDisposable;
    private WeatherMap weatherMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        weatherMap = retrofit.create(WeatherMap.class);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String query = "N/A";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
             query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setTitle(query);
        ab.setDisplayHomeAsUpEnabled(true);
    }



    private void doSearch(final String query) {
            //subscribe HTTP request
            compositeDisposable.add(weatherMap.getLocByAddress(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LocationResult>() {
                        @Override
                        public void accept(LocationResult locationResult) throws Exception {
                            String loc = locationResult.getData().getResults().get(0).getGeometry().getLocation().lat
                                    +"," + locationResult.getData().getResults().get(0).getGeometry().getLocation().lng;
                            Log.d(TAG, "accept111: " + loc);
                            Common.search_txt = query;
                            Common.search_loc = loc;
                            ViewPager viewPager = findViewById(R.id.search_pager);
                            ViewSearchPageAdapter viewSearchPageAdapter = new ViewSearchPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                            viewPager.setAdapter(viewSearchPageAdapter);
                        }

                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d("Fetch Error", "accept: " + throwable.getMessage());
                        }
                    })
            );

    }

}
