package com.example.weathersearch;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weathersearch.Adapter.PhotosViewAdapther;
import com.example.weathersearch.Common.Common;
import com.example.weathersearch.Model.Item;
import com.example.weathersearch.Model.PhotosResult;
import com.example.weathersearch.Retrofit.RetrofitClient;
import com.example.weathersearch.Retrofit.WeatherMap;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class PhotosFragment extends Fragment {

    private List<String> imgList;
    private static final String TAG = "PhotosFragment";
    private CompositeDisposable compositeDisposable;
    private Retrofit retrofit;
    private WeatherMap weatherMap;
    private RecyclerView recyclerView;
    private PhotosViewAdapther photosViewAdapther;
    public PhotosFragment() {
        imgList = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        retrofit = RetrofitClient.getInstance();
        weatherMap = retrofit.create(WeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.photo_recycler_view);
        compositeDisposable.add(weatherMap.getImgByName(Common.search_txt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhotosResult>() {
                    @Override
                    public void accept(PhotosResult photosResult) throws Exception {
                        List<Item> items = photosResult.getData().getItems();
                        for(Item item : items){
                            imgList.add(item.getLink());
                        }

                        photosViewAdapther = new PhotosViewAdapther(imgList, getContext());
                        recyclerView.setAdapter(photosViewAdapther);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
