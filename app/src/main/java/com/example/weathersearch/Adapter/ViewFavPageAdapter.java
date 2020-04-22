package com.example.weathersearch.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weathersearch.Common.Common;
import com.example.weathersearch.FavFragment;


public class ViewFavPageAdapter extends FragmentStatePagerAdapter {
    //private long baseId = 0;
    public ViewFavPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new FavFragment(position, Common.cityList.get(position)[0], Common.cityList.get(position)[1]);
    }

    @Override
    public int getCount() {
        return Common.cityList.size();
    }

    //this is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return ViewFavPageAdapter.POSITION_NONE;
    }


    public void addTabPage(String cityName, String cityLoc) {
        Common.cityList.add(new String[]{cityName, cityLoc});
        notifyDataSetChanged();
    }

    public void removeTabPage(int position) {
        if (!Common.cityList.isEmpty() && position<Common.cityList.size()) {
            Common.cityList.remove(position);
            notifyDataSetChanged();
        }
    }
}
