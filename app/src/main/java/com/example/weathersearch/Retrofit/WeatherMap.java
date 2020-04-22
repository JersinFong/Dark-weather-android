package com.example.weathersearch.Retrofit;

import com.example.weathersearch.Model.AutoResult;
import com.example.weathersearch.Model.LocationResult;
import com.example.weathersearch.Model.PhotosResult;
import com.example.weathersearch.Model.WeatherResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherMap {
    @GET("weather/{loc}")
    Observable<WeatherResult> getWeatherByLatLng(@Path("loc") String LatLng);

    @GET("weather/location/{addr}")
    Observable<LocationResult> getLocByAddress(@Path("addr") String Address);

    @GET("weather/img/{img}")
    Observable<PhotosResult> getImgByName(@Path("img") String Name);

    @GET("auto/{text}")
    Observable<AutoResult> getAutoByText(@Path("text") String text);
}
