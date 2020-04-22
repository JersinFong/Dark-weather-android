package com.example.weathersearch.Model;

import java.io.Serializable;

public class WeatherResult implements Serializable {
    private Data data;
    public WeatherResult() {
    }
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
