package com.example.weathersearch.Adapter;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weathersearch.Common.Common;
import com.example.weathersearch.FavFragment;

public class ViewSearchPageAdapter extends FragmentStatePagerAdapter {
    public ViewSearchPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        FavFragment favFragment = new FavFragment(-1, Common.search_txt, Common.search_loc);
        return favFragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}