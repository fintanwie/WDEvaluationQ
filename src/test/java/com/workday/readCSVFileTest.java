package com.workday;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

        List<Map<String, String>> expected = new ArrayList<Map<String, String>>();
        Map<String, String> record1 = new LinkedHashMap<String, String>();
        record1.put("countryCode","AU");
        record1.put("countryName","Australia");
        record1.put("number","1");
        Map<String, String> record2 = new LinkedHashMap<String, String>();
        record2.put("countryCode","BH");
        record2.put("countryName","BahamasX");
        record2.put("number","2");
        Map<String, String> record3 = new LinkedHashMap<String, String>();
        record3.put("countryCode","BH");
        record3.put("countryName","Dublin, Town");
        record3.put("number","3");
        Map<String, String> record4 = new LinkedHashMap<String, String>();
        record4.put("countryCode","BH");
        record4.put("countryName","counties");
        record4.put("number","4");
        Map<String, String> record5 = new LinkedHashMap<String, String>();
        record5.put("countryCode","AU");
        record5.put("countryName","Northern Aus\"\"traliabc");
        record5.put("number","5");
        expected.add(record1);
        expected.add(record2);
        expected.add(record3);
        expected.add(record4);
        expected.add(record5);


        readCSVFileExample reader = new readCSVFileExample();
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

        readCSVFileExample reader = new readCSVFileExample();
        reader.readCSVFile3("countryB.csv", fileHeaders);
        Assert.assertTrue("Assertion Failed", true);
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

        readCSVFileExample reader = new readCSVFileExample();
        reader.readCSVFile3("nations1.csv", fileHeaders);
        Assert.assertTrue("Assertion Failed", true);
    }
}
