package com.workday;

import java.util.Map;

/**
 * Created by fward on 16/12/2017.
 */
public class Nations2Record
{
    private String iso3c = "";
    private int year = 0;
    private Double gdp_percap = 0.0;

    public Nations2Record(Map<String, String> record)
    {
        this.iso3c = record.get("iso3c");
        this.year = Integer.parseInt(record.get("year"));
        this.gdp_percap = new Double(
                record.get("gdp_percap").length() > 0 ? record.get("gdp_percap") : "0.0");
    }

    public String getIso3c()
    {
        return iso3c;
    }

    public int getYear()
    {
        return year;
    }

    public Double getGdp_percap() { return gdp_percap; }
}
