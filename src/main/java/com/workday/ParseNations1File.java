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
public class ParseNations1File
{
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public ArrayList<NationsRecord> getData()
    {
        List<Map<String, String>> parsedData;
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
            return null;
        }

        // Create national records
        ArrayList<NationsRecord> national1Data = new ArrayList<>();
        for (Map<String, String> record : parsedData) {
            if (record.get("year").equals("2014")) {
                national1Data.add(new NationsRecord(record));
            }
        }
        return national1Data;
    }

    protected List<Map<String, String>> readCSVFile3(String fileName, List<String> fileHeaders) throws Exception
    {
        StringBuilder fileData = new StringBuilder();
        fileData.append(parseDataFile(fileName));

        List<Map<String, String>> parsedData = parseInputData(fileHeaders, fileData);
        return parsedData;
    }

    private List<Map<String, String>> parseInputData(List<String> allHeaders, StringBuilder fileData)
    {
        boolean debug = false;
        if (debug) System.out.println("parseInputData : Parsing Data");
        ArrayList<Map<String, String>> parsedData = new ArrayList<>();
        Map<String, String> aRecord = new TreeMap<>();
        Set<String> headers = new TreeSet<>();
        headers.addAll(allHeaders);
        ArrayList<String> incomeValues = new ArrayList<String>(Arrays.asList("High income", "Low income",
                "Upper middle income", "Lower middle income", "Not classified"));

        int dataPointPosition = -1;
        String partialDataPoint = "";
        boolean processingQoutedWord = false;
        String[] data = fileData.toString().split(",");
        if (debug) System.out.println("parseInputData data size is : " + data.length);
        for (String datapoint : data) {
            String dataPointFound = "";
            boolean processDataPoint = true;
            if (!processingQoutedWord)
                datapoint = datapoint.trim();
            if (debug) System.out.println("parseInputData 1: data : " + datapoint);

            String headerFound = doesDatapointContainHeader(headers, datapoint);
            if (!headerFound.isEmpty()) {
                // Check for embedded dataPoint in headers
                if (debug) System.out.println("parseInputData 2: Check for embedded dataPoint.");
                headers.remove(headerFound);
                datapoint = datapoint.replace(headerFound, "");
                dataPointFound += datapoint;
                if (datapoint.isEmpty())
                    processDataPoint = false;
            }
            else if (datapoint.contains("\"")) {
                if (debug) System.out.println("parseInputData 2a: process qouted datapoint");
                // process qouted datapoint
                if (processingQoutedWord) {
                    if (debug) System.out.println("parseInputData : processingQoutedWord");
                    processingQoutedWord = false;
                    datapoint = partialDataPoint + "," + removeQoutesFromDatapoint(datapoint);
                    partialDataPoint = "";
                }
                // Process datapoint that is wrapped in qoutes
                else if (datapoint.startsWith("\"") && datapoint.endsWith("\"")) {
                    if (debug) System.out.println("parseInputData : wrapping qoutes.");
                    datapoint = removeQoutesFromDatapoint(datapoint);
                }
                // process datapoints with qoute at start of Word.
                else if (datapoint.startsWith("\"")) {
                    if (debug) System.out.println("parseInputData : Qoutes at start of word.");
                    processingQoutedWord = true;
                    partialDataPoint += removeQoutesFromDatapoint(datapoint);
                    continue;
                }
                dataPointFound += datapoint;
            }
            else {
                // Handle correctly formatted datapoint.;
                dataPointFound += datapoint;
                if (debug)
                    System.out.println("This is a correctly formatted  data field [" + datapoint + "]");
            }
            if (debug) System.out.println("parseInputData 3: datapoint : [" + datapoint + "].");

            if (processDataPoint) {
                dataPointPosition++;
                int headerNumber = dataPointPosition % allHeaders.size();
                if (debug)
                    System.out.println("parseInputData 5a: dataPointPosition : [" + dataPointPosition + "].");
                if (debug) System.out.println("parseInputData 5a: headerNumber : [" + headerNumber + "].");

                String fieldName = allHeaders.get(headerNumber);
                if (debug) System.out.println("parseInputData 4: Handle Data Point : ["
                        + headerNumber
                        + "]. datapoint : [" + dataPointFound + "].");
                if (debug) System.out.println("Add to Map : [" + fieldName + "] = [" + dataPointFound + "].");
                //if (headerNumber == 0) {
                if (debug)
                    System.out.println("parseInputData 4a : " + headerNumber + " == " + allHeaders.size());
                //if (headerNumber == allHeaders.size()) {
                if (checkForLastHeader(allHeaders, dataPointPosition)) {
                    // Store record and start a new one

                    if (debug) System.out.println("parseInputData 5a: Found last header");
                    if (debug)
                        System.out.println("parseInputData 5a: dataPointFound [" + dataPointFound + "].");
                    if (debug) System.out.println(
                            "parseInputData 5b : " + aRecord.size() + " == " + allHeaders.size());

                    String firstFieldOfNextRecord = "";
                    for (String incomeValue : incomeValues) {
                        if (dataPointFound.contains(incomeValue)) {
                            firstFieldOfNextRecord = dataPointFound.replace(incomeValue, "");
                            dataPointFound = incomeValue;
                            if (debug) System.out.println(
                                    "parseInputData 5c: firstFieldOfNextRecord : " + firstFieldOfNextRecord);
                            if (debug)
                                System.out.println("parseInputData 5d: dataPointFound : " + dataPointFound);
                        }
                    }
                    if (debug) System.out.println(
                            "parseInputData 5c2 : Adding : " + fieldName + " , " + dataPointFound);
                    aRecord.put(fieldName, dataPointFound);

                    if (debug) System.out.println(
                            "parseInputData 5e: " + aRecord.size() + " == " + allHeaders.size());
                    if (aRecord.size() == allHeaders.size()) {
                        if (debug)
                            System.out.println("parseInputData 5f: Storing Record : [" + aRecord + "].");
                        parsedData.add(aRecord);
                    }
                    if (debug) System.out.println("parseInputData 6: Clearing record.");
                    aRecord = new LinkedHashMap<>();
                    if (firstFieldOfNextRecord.length() > 0) {
                        if (debug) System.out.println(
                                "parseInputData 6a: Adding Field [" + allHeaders.get(allHeaders.size() - 1) +
                                        " ]: [" + firstFieldOfNextRecord + "].");
                        aRecord.put(allHeaders.get(0), firstFieldOfNextRecord);
                        dataPointPosition++;
                        continue;
                    }
                }
                if (debug) System.out.println("parseInputData 8: Adding Field [" + fieldName + "] : [" +
                        dataPointFound + "].");
                aRecord.put(fieldName, dataPointFound);
            }
        }
        if (debug) System.out.println("parseInputData 8: returning parsed Data: ");
        return parsedData;
    }

    private String removeQoutesFromDatapoint(String datapoint)
    {
        if (datapoint == null | datapoint.length() == 0) return "";
        return datapoint.replace("\"", "");
    }

    private boolean checkForLastHeader(List<String> allHeaders, int dataPointPosition)
    {
        boolean debug = false;
        if (debug) System.out.println("checkForLastHeader.");
        int currentPosition = dataPointPosition % allHeaders.size();
        if (currentPosition == allHeaders.size() - 1) {
            if (debug) System.out.println("checkForLastHeader 1: At Last Header");
            return true;
        }
        if (debug) System.out.println("checkForLastHeader 1: Not Last Header");
        return false;
    }

    private StringBuilder parseDataFile(String fileName) throws Exception
    {
        boolean debug = false;
        StringBuilder data = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            is = new FileInputStream(classLoader.getResource(fileName).getPath());
            new InputStreamReader(is);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            // read to the end of the stream
            int value = 0;
            int charCount = -1;
            char[] charArray = new char[11];
            while ((value = br.read()) != -1) {
                charCount++;
                if (debug) System.out.println("charCount is : " + charCount);
                if (charCount >= 10) {
                    addToCharArray(charArray, charCount, value);
                    if (debug) System.out.println(
                            "charArray is : [" + charArray.length + "]. charCount is [" + charCount + "]. Letter is : [" + (char) value + "].");
                    StringBuffer line = new StringBuffer();
                    for (int i = 0; i == charArray.length; i++) {
                        line.append(charArray[i]);
                    }
                    if (debug) printCharArray(charArray);
                    data.append(parseCharArray(charArray));
                    charArray = new char[11];
                    charCount = -1;
                }
                else {
                    addToCharArray(charArray, charCount, value);
                    if (debug) printCharArray(charArray);
                }
            }
            if (debug) System.out.println("Read Done : ");
            if (charCount > -1) {
                if (debug) System.out.println(
                        "==>> Test Char charArray   [" + (charCount) + "] : " + charArray[charCount]);
                char[] compactArray = resizeCharArray(charArray, charCount);
                if (debug) System.out.println(
                        "==>> Test Char compactArray[" + (compactArray.length - 1) + "] : " + compactArray[compactArray.length - 1]);
                if (debug) printCharArray(compactArray);
                data.append(compactArray);
            }
            if (debug) System.out.println("dataFile : " + data.toString());
            if (debug) System.out.println("END PARSING FILE.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // releases resources associated with the streams
            if (is != null)
                is.close();
            if (isr != null)
                isr.close();
            if (br != null)
                br.close();
        }
        return data;
    }

    private void addToCharArray(char[] charArray, int charCount, int value)
    {
        boolean debug = false;
        char c = (char) value;
        charArray[charCount] = c;
        if (debug) System.out.println("New Letter : " + charCount + " : [" + c + "]." + value);
    }

    private void printCharArray(char[] array)
    {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i];
            System.out.print(array[i]);
        }
        System.out.print("].\n");
    }

    private char[] resizeCharArray(char[] charArray, int charCount)
    {
        boolean debug = false;
        char[] compactArray = new char[charCount + 1];
        if (debug) System.out.println("==>> resizeCharArray charCount : " + charCount);
        if (debug) System.out.println("==>> charArray [" + charCount + "] char : " + charArray[charCount]);

        if (debug) System.out.print("Compacting Array : [");
        for (int i = 0; i <= charCount; i++) {
            compactArray[i] = charArray[i];
            if (debug) System.out.print(charArray[i]);
        }
        if (debug) System.out.print("].\n");
        return compactArray;
    }

    private String doesDatapointContainHeader(Set<String> headers, String word)
    {
        boolean debug = false;
        String containsHeader = "";
        for (String header : headers)
            if (word.startsWith(header)) {
                if (debug) System.out.println("containsHeader 1. : This is a header");
                containsHeader = header;
            }
        return containsHeader;
    }

    private StringBuilder parseCharArray(char[] charArray)
    {
        boolean debug = false;
        StringBuilder result = new StringBuilder();
        if (debug) System.out.println("In parseCharArray.");
        if (charArray == null) return result;

        char previousChar = ' ';
        char customQuote = DEFAULT_QUOTE;
        char separator = DEFAULT_SEPARATOR;
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean inWord = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        int charCount = -1;
        if (debug) System.out.println("==>> parseCharArray.length : " + charArray.length);
        if (debug) System.out.println(
                "==>> Test Char [" + (charArray.length - 1) + "] : " + charArray[charArray.length - 1]);
        for (char ch : charArray) {
            charCount++;
            if (debug) {
                if (debug) System.out.println("parseCharArray PreviousChar : " + previousChar);
                if (debug)
                    System.out.println("parseCharArray Current Char : [" + charArray[charCount] + "] - [+ " +
                            +(int) charArray[charCount] + "].");
            }
            if (inQuotes) {
                if (debug) System.out.println("Collect Char : [" + charArray[charCount] + "].");
                startCollectChar = true;
                if (ch == DEFAULT_QUOTE) {
                    if (debug) System.out.println("parseCharArray inQuotes: DEFAULT_QUOTE");
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                    curVal.append(ch);
                }
                else {
                    if (debug) System.out.println("parseCharArray inQuotes: NOT_DEFAULT_QUOTE");
                    inWord = true;
                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                            continue;
                        }
                    }
                    else {
                        if (ch != '\n') {
                            curVal.append(ch);
                        }
                    }
                }

                if (ch == '\r') {
                    //ignore LF characters
                    if (debug) System.out.println("In LF");
                    continue;
                }
                else if (ch == '\n') {
                    if (debug) System.out.println("parseCharArray inQuotes: In CF");
                    curVal.append(",");
                    if (debug) System.out.println("parseCharArray inQuotes: parseLine : " + curVal);
                    continue;
                }
            }
            else {
                if (ch == customQuote) {
                    inQuotes = true;
                    inWord = true;
                    curVal.append('"');

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                }
                else if (ch == separator) {
                    if (debug)
                        System.out.println("parseCharArray : In Seperator curValue is : [" + curVal + "].");
                    inWord = false;
                    result.append(curVal.toString()).append(separator);

                    curVal = new StringBuffer();
                    startCollectChar = false;
                    if (debug) System.out.println("parseCharArray : charCount is : " + charCount);
                    if (debug) System.out.println("parseCharArray : result is now : [" + result + "].");
                    if (debug) System.out.println("parseCharArray : curValue is now : [" + curVal + "].");

                }
                else if (ch == '\r') {
                    if (debug) System.out.println("In LF");
                    //ignore LF characters
                    continue;
                }
                else if (ch == '\n') {
                    if (debug) System.out.println("parseCharArray : In CF");
                    curVal.append(",");
                    if (debug) System.out.println("parseCharArray X: parseLine : " + curVal);
                    continue;
                }
                else {
                    inWord = true;
                    curVal.append(ch);
                    if (debug) System.out.println(
                            "Appending char [" + charCount + "] : [" + ch + "]. CurValue is [" + curVal.toString() + "].");
                }
            }
            previousChar = ch;
        }

        if (!inWord) {
            if (debug) System.out.println("Adding Word : " + curVal);
            result.append(curVal.toString());
        }
        else {
            result.append(curVal.toString());
            if (debug) System.out.println("Last Word is : " + curVal);
            if (debug) System.out.println("Last result is : " + result);
        }
        return result;
    }
}
