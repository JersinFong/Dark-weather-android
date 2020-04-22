package com.example.weathersearch.Model;

import java.util.List;

public class Data2 {
    private List<Result> results ;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status ;
}
