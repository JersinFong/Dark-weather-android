package com.example.weathersearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.weathersearch.Adapter.ViewDetailPageAdapter;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.WeatherResult;
import com.google.android.material.tabs.TabLayout;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private TabLayout tabLayout;
    private WeatherResult weather = null;
    private int[] tabIcons = {
            R.drawable.calendar_today,
            R.drawable.trending_up,
            R.drawable.google_photos
    };
    private int[] tabIcons_dark = {
            R.drawable.calendar_today_dark,
            R.drawable.trending_up_dark,
            R.drawable.goodle_photos_dark
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        //ab.setTitle(query);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Common.search_txt);
        Intent intent = getIntent();

        if (intent != null) {
             weather = (WeatherResult) intent.getSerializableExtra("weather_pass");
            Log.d(TAG, "onCreate: " + weather.getData().getTimezone());
        }

        ViewPager viewPager = findViewById(R.id.details_pager);
        ViewDetailPageAdapter viewPagerAdapter = new ViewDetailPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, weather);
        tabLayout = findViewById(R.id.tab_details_layout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_dark[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_dark[2]);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < tabLayout.getTabCount(); i++){
                    tabLayout.getTabAt(0).setIcon(tabIcons_dark[0]);
                    tabLayout.getTabAt(1).setIcon(tabIcons_dark[1]);
                    tabLayout.getTabAt(2).setIcon(tabIcons_dark[2]);
                }
                tabLayout.getTabAt(position).setIcon(tabIcons[position]);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String url = "https://twitter.com/intent/tweet?text=Check%20Out%20"+Common.search_txt
                + "’s%20Weather!%20It%20is%20"+weather.getData().getCurrently().getTemperature()+"°F!&&hashtags=CSCI571WeatherSearch";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        return super.onOptionsItemSelected(item);
    }
}
