package com.workday;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class readCSVFileTest
{
    @Test
    public void testTrue()
    {
        boolean result = true;
        Assert.assertTrue("Assertion Failed", result);
    }


    @Test
    public void testWithSampleInput() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("number");
        fileHeaders.add("countryCode");
        fileHeaders.add("countryName");

        List<Map<String, String>> expected = new ArrayList<>();
        Map<String, String> record1 = new LinkedHashMap<>();
        record1.put("countryCode","AU");
        record1.put("countryName","Australia");
        record1.put("number","1");
        Map<String, String> record2 = new LinkedHashMap<>();
        record2.put("countryCode","BH");
        record2.put("countryName","BahamasX");
        record2.put("number","2");
        Map<String, String> record3 = new LinkedHashMap<>();
        record3.put("countryCode","BH");
        record3.put("countryName","Dublin, Town");
        record3.put("number","3");
        Map<String, String> record4 = new LinkedHashMap<>();
        record4.put("countryCode","BH");
        record4.put("countryName","counties");
        record4.put("number","4");
        Map<String, String> record5 = new LinkedHashMap<>();
        record5.put("countryCode","AU");
        record5.put("countryName","Northern Aus\"\"traliabc");
        record5.put("number","5");
        expected.add(record1);
        expected.add(record2);
        expected.add(record3);
        expected.add(record4);
        expected.add(record5);

        parseNations1File reader = new parseNations1File();
        List<Map<String, String>> results = reader.readCSVFile3("country.csv", fileHeaders);
        Assert.assertEquals(expected, results);
    }

    @Test
    public void testCountryB() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("iso2c");
        fileHeaders.add("iso3c");
        fileHeaders.add("country");
        fileHeaders.add("year");
        fileHeaders.add("life_expect");
        fileHeaders.add("population");
        fileHeaders.add("birth_rate");
        fileHeaders.add("neonat_mortal_rate");
        fileHeaders.add("region");
        fileHeaders.add("income");
        System.out.println("fileHeaders :" + fileHeaders);

        parseNations1File reader = new parseNations1File();
        List<Map<String, String>> results = reader.readCSVFile3("countryB.csv", fileHeaders);
        Assert.assertEquals(4, results.size());
    }

    @Test
    public void testNations1a() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("iso2c");
        fileHeaders.add("iso3c");
        fileHeaders.add("country");
        fileHeaders.add("year");
        fileHeaders.add("life_expect");
        fileHeaders.add("population");
        fileHeaders.add("birth_rate");
        fileHeaders.add("neonat_mortal_rate");
        fileHeaders.add("region");
        fileHeaders.add("income");
        System.out.println("fileHeaders :" + fileHeaders);

        parseNations1File reader = new parseNations1File();
        List<Map<String, String>> results = reader.readCSVFile3("nations1a.csv", fileHeaders);
        Assert.assertEquals(100, results.size());
    }

    @Test
    public void testNations1() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("iso2c");
        fileHeaders.add("iso3c");
        fileHeaders.add("country");
        fileHeaders.add("year");
        fileHeaders.add("life_expect");
        fileHeaders.add("population");
        fileHeaders.add("birth_rate");
        fileHeaders.add("neonat_mortal_rate");
        fileHeaders.add("region");
        fileHeaders.add("income");
        System.out.println("fileHeaders :" + fileHeaders);

        parseNations1File reader = new parseNations1File();
        List<Map<String, String>> results = reader.readCSVFile3("nations1.csv", fileHeaders);
        Assert.assertEquals(5486, results.size());
    }

    @Test
    public void parseNations2aFile() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("iso3c");
        fileHeaders.add("year");
        fileHeaders.add("gdp_percap");
        System.out.println("fileHeaders :" + fileHeaders);

        parseNations2File reader = new parseNations2File();
        List<Map<String, String>> results = reader.readCSVFile3("nations2a.csv", fileHeaders);
        Assert.assertEquals(18, results.size());
    }

    @Test
    public void parseNations2File() throws Exception
    {
        List<String> fileHeaders = new ArrayList<>();
        fileHeaders.add("iso3c");
        fileHeaders.add("year");
        fileHeaders.add("gdp_percap");
        System.out.println("fileHeaders :" + fileHeaders);

        parseNations2File reader = new parseNations2File();
        List<Map<String, String>> results = reader.readCSVFile3("nations2.csv", fileHeaders);
        Assert.assertEquals(5486, results.size());
    }
}
