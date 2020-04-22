package com.example.weathersearch.Model;

import java.io.Serializable;
import java.util.List;

public class Flags implements Serializable {
    private List<String> sources;
    private double nearest_station;
    private String units;

    public Flags() {
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public double getNearest_station() {
        return nearest_station;
    }

    public void setNearest_station(double nearest_station) {
        this.nearest_station = nearest_station;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
