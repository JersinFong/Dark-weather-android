package com.example.weathersearch;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.weathersearch.Adapter.ViewFavPageAdapter;
import com.example.weathersearch.Adapter.ViewSearchPageAdapter;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.AutoResult;
import com.example.weathersearch.Model.Prediction;
import com.example.weathersearch.Model.WeatherResult;
import com.example.weathersearch.Retrofit.RetrofitClient;
import com.example.weathersearch.Retrofit.WeatherMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static ViewFavPageAdapter viewPagerAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private static TabLayout tabLayout;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Retrofit retrofit = RetrofitClient.getInstance();
    WeatherMap weatherMap = retrofit.create(WeatherMap.class);
    ArrayAdapter<String> newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Common.search_loc = null;
        Common.search_txt = "N/A";
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        //Request Permission
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            buildLocationRequest();
                            buildLocationCallBack();
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Log.d("Permission", "Permission Denied");
                    }
                }).check();



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        searchView.setMaxWidth(1050);
        ComponentName componentName = new ComponentName(getBaseContext(), SearchableActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_light);
        searchAutoComplete.setDropDownAnchor(R.id.toolbar3);

        toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                searchAutoComplete.setDropDownWidth(1200);
                searchAutoComplete.setDropDownHorizontalOffset(125);
                searchAutoComplete.setDropDownVerticalOffset(-20);
            }
        });
        searchAutoComplete.setThreshold(1);
        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {};
        newsAdapter = new ArrayAdapter<>(this, R.layout.my_list_item, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
            }
        });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String text) {
        compositeDisposable.add(weatherMap.getAutoByText(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AutoResult>() {
                    @Override
                    public void accept(AutoResult autoResult) throws Exception {
                        List<String> stringList = new ArrayList<>();
                        for(Prediction pre : autoResult.getData().getPredictions()){
                            stringList.add(pre.getDescription());
                        }
                        newsAdapter.clear();
                        newsAdapter.addAll(stringList);
                        newsAdapter.notifyDataSetChanged();
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Fetch Error", "accept: " + throwable.getMessage());
                    }
                })
        );

    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location current_location = locationResult.getLastLocation();
                double lat = current_location.getLatitude();
                double lon = current_location.getLongitude();
                String cityLoc = lat + "," + lon;
                String cityName = "";
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null) {
                    if (addresses.size() > 0){
                        cityName = addresses.get(0).getLocality()+", " +
                                addresses.get(0).getAdminArea()+", " +
                                addresses.get(0).getCountryCode();
                    }

                }
                //initiate viewpager for favorite
                tabLayout = findViewById(R.id.tab_fav_layout);
                tabLayout.addTab(tabLayout.newTab());
                viewPager = findViewById(R.id.fav_pager);
                viewPagerAdapter = new ViewFavPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                if(Common.cityList.size()<1) {
                    addTab(cityName, cityLoc);
                    SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                            getString(R.string.cityStore), Context.MODE_PRIVATE);
                    Map<String, String> stores = (Map<String, String>) sharedPref.getAll();
                    if (stores != null) {
                        for (Map.Entry<String, String> store : stores.entrySet()) {
                            addTab(store.getKey(), store.getValue());
                        }
                    }
                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(5000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    public static void addTab(String cityName, String cityLoc) {
        tabLayout.addTab(tabLayout.newTab());
        viewPagerAdapter.addTabPage(cityName, cityLoc);
    }

    public static void removeTab(int position) {
        if (tabLayout.getTabCount() >= 1 && position<tabLayout.getTabCount()) {
            tabLayout.removeTabAt(position);
            viewPagerAdapter.removeTabPage(position);
        }
    }


}
