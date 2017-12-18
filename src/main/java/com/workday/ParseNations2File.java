package com.workday;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by fward on 12/12/2017.
 */
public class ParseNations2File extends ParseDataFile
{
    public void getData(List<NationsRecord> nationalData)
    {
        boolean debug = false;
        List<Map<String, String>> parsedData;
        try {
            List<String> fileHeaders = new ArrayList<>();
            fileHeaders.add("iso3c");
            fileHeaders.add("year");
            fileHeaders.add("gdp_percap");
            if (debug) System.out.println("fileHeaders :" + fileHeaders);
            parsedData = readCSVFile3("nations2.csv", fileHeaders);
        }
        catch (Exception ex) {
            if (debug) System.out.println("Error reading the file.");
            return;
        }

        nationalData = createNationalData((ArrayList)nationalData, parsedData);
        if (debug) System.out.println("nationalData : " + nationalData);
    }

    private ArrayList<NationsRecord> createNationalData(ArrayList<NationsRecord> nationalData,
                                    List<Map<String, String>> parsedData ) {
        boolean debug = false;
        boolean testRun = false;
        for (Map<String, String> record : parsedData) {
            if(debug) System.out.println("ParseNations2File : getData : " + record.toString());
            if (record.get("year").equals("2014")) {
                if(debug) System.out.println("ParseNations2File : Year 2014 Found.");
                if (nationalData.size() > 0 && !testRun) {
                    for (NationsRecord nr : nationalData) {
                        String currentIso3c  = record.get("iso3c");
                        String storedIso3c = nr.getIso3c();
                        if (currentIso3c.equalsIgnoreCase(storedIso3c)) {
                            if(debug) System.out.println("ParseNationsFile : addingData : [" + currentIso3c + "].");
                            nr.setGdp_percap(record.get("gdp_percap"));
                        }
                    }
                }
                else {
                    if(debug) System.out.println("ParseNations2File : Test Run : Adding Record.");
                    NationsRecord nr = new NationsRecord(record);
                    nationalData.add(nr);
                    testRun = true;
                }
            }
        }
        return nationalData;
    }

    protected void handleLF(StringBuffer curVal) {
        curVal.append(',');
    }

    protected void handleCF(StringBuffer curVal) {
        // ignore CF characters
    }
}
