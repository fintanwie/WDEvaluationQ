package com.workday;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by fward on 12/12/2017.
 */
public class ParseNations2File extends ParseCSVFile
{
    private String YEAR_FILTER = "2014";

    @SuppressWarnings("unchecked")
    public void getData(List<NationsRecord> nationalData)
    {
        List<Map<String, String>> parsedData;
        try {
            List<String> fileHeaders = new ArrayList<>();
            fileHeaders.add("iso3c");
            fileHeaders.add("year");
            fileHeaders.add("gdp_percap");

            parsedData = readCSVFile("nations2.csv", fileHeaders);
        }
        catch (Exception ex) {
            System.out.println("Error reading the file.");
            return;
        }
        createNationalData((ArrayList)nationalData, parsedData);
    }

    private void createNationalData(ArrayList<NationsRecord> nationalData,
                                    List<Map<String, String>> parsedData ) {
        for (Map<String, String> record : parsedData) {
            if (record.get("year").equals(YEAR_FILTER)) {
                if (nationalData.size() > 0) {
                    for (NationsRecord nr : nationalData) {
                        String currentIso3c  = record.get("iso3c");
                        String storedIso3c = nr.getIso3c();
                        if (currentIso3c.equalsIgnoreCase(storedIso3c)) {
                            nr.setGdp_percap(record.get("gdp_percap"));
                        }
                    }
                }
                else {
                    NationsRecord nr = new NationsRecord(record);
                    nationalData.add(nr);
                }
            }
        }
    }

    protected void handleLF(StringBuffer curVal) {
        curVal.append(DEFAULT_SEPARATOR);
    }

    protected void handleCF(StringBuffer curVal) {
        // ignore CF characters
    }
}
