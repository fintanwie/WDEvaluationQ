package com.workday;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by fward on 16/12/2017.
 */
public class NationsRecord implements Comparable<NationsRecord>
{
    private String iso2c = "";
    private String iso3c = "";
    private String country = "";
    private int year = 0;
    private Double life_expect = 0.0;
    private Double population = 0.0;
    private Double birth_rate = 0.0;
    private Double neonat_mortal_rate;
    private String region = "";
    private String income = "";
    private Double gdp_percap = 0.0;

    static Comparator<NationsRecord> getPopulationComparator() {
        return new Comparator<NationsRecord>() {
            public int compare(NationsRecord nr1,NationsRecord nr2)
            {
                return nr1.getPopulation().compareTo(nr2.getPopulation());
            }
        };
    }

    static Comparator<NationsRecord> getGDPComparator() {
        return new Comparator<NationsRecord>() {
            public int compare(NationsRecord nr1,NationsRecord nr2)
            {
                return nr1.getGdp_percap().compareTo(nr2.getGdp_percap());
            }
        };
    }

    static Comparator<NationsRecord> getLifeExpectencyComparator() {
        return new Comparator<NationsRecord>() {
            public int compare(NationsRecord nr1,NationsRecord nr2)
            {
                return nr1.getLife_expect().compareTo(nr2.getLife_expect());
            }
        };
    }

    public NationsRecord(Map<String, String> record)
    {
        this.iso2c = record.get("iso2c");
        this.iso3c = record.get("iso3c");
        this.country = record.get("country");
        this.year = Integer.parseInt(record.get("year"));

        String life_expect = record.get("life_expect");
        if (life_expect != null && life_expect.length() > 0)
            this.life_expect = new Double(record.get("life_expect"));
        else
            this.life_expect = 0.0;

        String population = record.get("population");
        if (population != null && population.length() > 0)
            this.population = new Double(record.get("population"));
        else
            this.population = 0.0;

        String birth_rate = record.get("birth_rate");
        if (birth_rate != null && birth_rate.length() > 0)
            this.birth_rate = new Double(record.get("birth_rate"));
        else
            this.birth_rate = 0.0;

        String neonat_mortal_rate = record.get("neonat_mortal_rate");
        if (neonat_mortal_rate != null && neonat_mortal_rate.length() > 0)
            this.neonat_mortal_rate = new Double(record.get("neonat_mortal_rate"));
        else
            this.neonat_mortal_rate = 0.0;

        this.region = record.get("region");
        this.income = record.get("income");
        this.gdp_percap = 0.0;
    }

    //Implement the natural order for this class
    public int compareTo(NationsRecord nr)
    {
        String left = getRegion();
        left += " " + getCountry();
        String right = nr.getRegion();
        right += " " + nr.getCountry();
        return left.compareTo(right);
    }

    public void setGdp_percap(String gdp_percap)
    {
        this.gdp_percap = (gdp_percap != null && gdp_percap.length() > 0) ? new Double(gdp_percap) : 0.0;
    }

    public Double getGdp_percap()
    {
        return gdp_percap;
    }

    public String getIso2c()
    {
        return iso2c;
    }

    public String getIso3c()
    {
        return iso3c;
    }

    public String getCountry()
    {
        return country;
    }

    public int getYear()
    {
        return year;
    }

    public Double getLife_expect()
    {
        return life_expect;
    }

    public Double getPopulation()
    {
        return population;
    }

    public Double getBirth_rate()
    {
        return birth_rate;
    }

    public Double getNeonat_mortal_rate()
    {
        return neonat_mortal_rate;
    }

    public String getRegion()
    {
        return region;
    }

    public String getIncome()
    {
        return income;
    }
}
