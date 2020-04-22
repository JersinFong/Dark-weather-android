package com.example.weathersearch.Adapter;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weathersearch.Model.WeatherResult;
import com.example.weathersearch.PhotosFragment;
import com.example.weathersearch.TodayFragment;
import com.example.weathersearch.WeeklyFragment;

public class ViewDetailPageAdapter extends FragmentStatePagerAdapter {

    WeatherResult weatherResult;
    public ViewDetailPageAdapter(@NonNull FragmentManager fm, int behavior, WeatherResult weatherResult) {
        super(fm, behavior);
        this.weatherResult = weatherResult;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TodayFragment todayFragment = new TodayFragment(this.weatherResult.getData().getCurrently());
                return todayFragment;
            case 1:
                WeeklyFragment weeklyFragment = new WeeklyFragment(this.weatherResult.getData().getDaily());
                return weeklyFragment;
            case 2:
                PhotosFragment photosFragment= new PhotosFragment();
                return photosFragment;
                default:
                    return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "TODAY";
            case 1:
                return "WEEKLY";
            case 2:
                return "PHOTOS";
            default:
                return null;
        }

    }
}
