package com.workday;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by fward on 12/12/2017.
 */
public class ParseNations1File extends ParseDataFile
{

    public void getData(List<NationsRecord> nationalData) {
        List<Map<String, String>> parsedData = new ArrayList<>();
        try {
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
            parsedData = readCSVFile3("nations1.csv", fileHeaders);
        }
        catch (Exception ex) {
            System.out.println("Error reading the file.");
            return;
        }

        // Add national records
        System.out.println("nationalData : [" + nationalData + "]");
        for (Map<String, String> record : parsedData) {
            if (record.get("year").equals("2014")) {
                nationalData.add(new NationsRecord(record));
            }
        }
    }

    protected void handleLF(StringBuffer curVal) {
        //ignore LF characters
    }

    protected void handleCF(StringBuffer curVal) {
        curVal.append(",");
    }
}
