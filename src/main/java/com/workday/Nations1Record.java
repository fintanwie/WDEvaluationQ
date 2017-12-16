package com.workday;

/**
 * Created by fward on 16/12/2017.
 */
public class Nations1Record
{
    String iso2c = "";
    String iso3c = "";
    String country = "";
    int year = 0;
    Double life_expect = 0.0;
    Double population = 0.0;
    Double birth_rate = 0.0;
    Double neonat_mortal_rate;
    String region = "";
    Double income = 0.0;

     public Nations1Record(String iso2c, String iso3c, String country, String year, String life_expect,
                           String population, String birth_rate, String neonat_mortal_rate,
                           String region, String income)
    {
        this.iso2c = iso2c;
        this.iso3c = iso3c;
        this.country = country;
        this.year = Integer.parseInt(year);
        this.life_expect = new Double(life_expect);
        this.population = new Double(population);
        this.birth_rate = new Double(birth_rate);
        this.neonat_mortal_rate = new Double(neonat_mortal_rate);
        this.region = region;
        this.income = new Double(income);
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

    public Double getIncome()
    {
        return income;
    }
}
