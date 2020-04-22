package com.example.weathersearch.Common;

import android.location.Location;
import android.widget.ImageView;

import com.example.weathersearch.FavFragment;
import com.example.weathersearch.Model.WeatherResult;
import com.example.weathersearch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common {
    public static String search_txt = "N/A";
    public static String search_loc = null;
    public static List<String[]> cityList = new ArrayList<>();

    public static void setMainImage(String icon, ImageView img){
        if(icon.equals("clear-night")){
            img.setImageResource(R.drawable.weather_night);
        }else if (icon.equals("rain")){
            img.setImageResource(R.drawable.weather_rainy);
        }else if(icon.equals("sleet")){
            img.setImageResource(R.drawable.weather_partly_snowy_rainy);
        }else if(icon.equals(("snow"))){
            img.setImageResource(R.drawable.weather_snowy);
        }else if(icon.equals(("wind"))){
            img.setImageResource(R.drawable.weather_windy_variant);
        }else if(icon.equals(("fog"))){
            img.setImageResource(R.drawable.weather_fog_white);
        }else if(icon.equals(("cloudy"))){
            img.setImageResource(R.drawable.weather_cloudy);
        }else if(icon.equals("partly-cloudy-night")){
            img.setImageResource(R.drawable.weather_night_partly_cloudy);
        }else if (icon.equals("partly-cloudy-day")){
            img.setImageResource(R.drawable.weather_partly_cloudy);
        }else{
            img.setImageResource(R.drawable.weather_sunny);
        }
    }
}
