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
abstract public class ParseCSVFile
{
    protected static final char DEFAULT_SEPARATOR = ',';
    protected static final char DEFAULT_QUOTE = '"';

    abstract void getData(List<NationsRecord> nationalData);

    protected List<Map<String, String>> readCSVFile(String fileName, List<String> fileHeaders) throws Exception
    {
        StringBuilder fileData = new StringBuilder();
        fileData.append(parseDataFile(fileName));
        return parseInputData(fileHeaders, fileData);
    }

    private List<Map<String, String>> parseInputData(List<String> allHeaders, StringBuilder fileData)
    {
        ArrayList<Map<String, String>> parsedData = new ArrayList<>();
        Map<String, String> aRecord = new TreeMap<>();
        Set<String> headers = new TreeSet<>();
        headers.addAll(allHeaders);
        ArrayList<String> incomeValues = new ArrayList<>(Arrays.asList("High income", "Low income",
                "Upper middle income", "Lower middle income", "Not classified"));

        int dataPointPosition = -1;
        StringBuilder partialDataPoint = new StringBuilder();
        boolean processingQoutedWord = false;
        String[] data = fileData.toString().split(",");
        for (String datapoint : data) {
            String dataPointFound = "";
            boolean processDataPoint = true;
            if (!processingQoutedWord)
                datapoint = datapoint.trim();

            if (datapoint.contains("\"")) {
                if (processingQoutedWord) {
                    processingQoutedWord = false;
                    datapoint = partialDataPoint.append(",").append(removeQoutesFromDatapoint(datapoint)).toString();
                    partialDataPoint = new StringBuilder();
                }
                // Process datapoint that is wrapped in qoutes
                else if (datapoint.startsWith("\"") && datapoint.endsWith("\"")) {
                    datapoint = removeQoutesFromDatapoint(datapoint);
                }
                // process datapoints with qoute at start of Word.
                else if (datapoint.startsWith("\"")) {
                    processingQoutedWord = true;
                    partialDataPoint.append(removeQoutesFromDatapoint(datapoint));
                    continue;
                }
                dataPointFound += datapoint;
            }
            else {
                // Handle correctly formatted datapoint.
                dataPointFound += datapoint;
            }

            if (processDataPoint) {
                dataPointPosition++;
                int headerNumber = dataPointPosition % allHeaders.size();
                String fieldName = allHeaders.get(headerNumber);

                if (checkForLastHeader(allHeaders, dataPointPosition)) {
                    // Store record and start a new one
                    String firstFieldOfNextRecord = "";
                    for (String incomeValue : incomeValues) {
                        if (dataPointFound.contains(incomeValue)) {
                            firstFieldOfNextRecord = dataPointFound.replace(incomeValue, "");
                            dataPointFound = incomeValue;
                        }
                    }
                    aRecord.put(fieldName, dataPointFound);

                    if (aRecord.size() == allHeaders.size()) {
                        parsedData.add(aRecord);
                    }

                    aRecord = new LinkedHashMap<>();
                    if (firstFieldOfNextRecord.length() > 0) {
                        aRecord.put(allHeaders.get(0), firstFieldOfNextRecord);
                        dataPointPosition++;
                        continue;
                    }
                }
                aRecord.put(fieldName, dataPointFound);
            }
        }
        return parsedData;
    }

    private String removeQoutesFromDatapoint(String datapoint)
    {
        if (datapoint == null || datapoint.length() == 0) return "";
        return datapoint.replace("\"", "");
    }

    private boolean checkForLastHeader(List<String> allHeaders, int dataPointPosition)
    {
        int currentPosition = dataPointPosition % allHeaders.size();
        if (currentPosition == allHeaders.size() - 1) {
            return true;
        }
        return false;
    }

    private StringBuilder parseDataFile(String fileName) throws Exception
    {
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
                if (charCount >= 10) {
                    addToCharArray(charArray, charCount, value);
                    StringBuffer line = new StringBuffer();
                    for (int i = 0; i == charArray.length; i++) {
                        line.append(charArray[i]);
                    }
                    data.append(parseCharArray(charArray));
                    charArray = new char[11];
                    charCount = -1;
                }
                else {
                    addToCharArray(charArray, charCount, value);
                }
            }
            if (charCount > -1) {
                char[] compactArray = resizeCharArray(charArray, charCount);
                data.append(compactArray);
            }
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
        char c = (char) value;
        charArray[charCount] = c;
    }

    private char[] resizeCharArray(char[] charArray, int charCount)
    {
        char[] compactArray = new char[charCount + 1];
        for (int i = 0; i <= charCount; i++) {
            compactArray[i] = charArray[i];
        }
        return compactArray;
    }

    private String doesDatapointContainHeader(Set<String> headers, String word)
    {
        String containsHeader = "";
        for (String header : headers) {
            if (word.startsWith(header)) {
                containsHeader = header;
                break;
            }
        }
        return containsHeader;
    }

    private StringBuilder parseCharArray(char[] charArray)
    {
        StringBuilder result = new StringBuilder();
        if (charArray == null) return result;

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean inWord = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        int charCount = -1;
        for (char ch : charArray) {
            charCount++;
            if (inQuotes) {
                startCollectChar = true;
                if (ch == DEFAULT_QUOTE) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                    curVal.append(ch);
                }
                else {
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
                    handleLF(curVal);
                }
                else if (ch == '\n') {
                    handleCF(curVal);
                }
            }
            else {
                if (ch == DEFAULT_QUOTE) {
                    inQuotes = true;
                    inWord = true;
                    curVal.append('"');

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                }
                else if (ch == DEFAULT_SEPARATOR) {
                    inWord = false;
                    result.append(curVal.toString()).append(DEFAULT_SEPARATOR);
                    curVal = new StringBuffer();
                    startCollectChar = false;
                }
                else if (ch == '\r') {
                    handleLF(curVal);
                }
                else if (ch == '\n') {
                    handleCF(curVal);
                }
                else {
                    inWord = true;
                    curVal.append(ch);
                }
            }
        }

        if (!inWord) {
            result.append(curVal.toString());
        }
        else {
            result.append(curVal.toString());
        }
        return result;
    }

    abstract protected void handleLF(StringBuffer curVal);

    abstract protected void handleCF(StringBuffer curVal);
}
