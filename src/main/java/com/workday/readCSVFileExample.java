package com.workday;

import com.sun.tools.corba.se.idl.constExpr.Not;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import com.sun.tools.javac.comp.Lower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by fward on 12/12/2017.
 */
public class readCSVFileExample
{
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    protected static final List<String> fileHeaders = new ArrayList<>(Arrays.asList(new String[] {"number", "countryCode" , "countryName"}));

    public static void main(String[] args)
    {
        try {
            readCSVFileExample reader = new readCSVFileExample();
            reader.readCSVFile3("countryB.csv", fileHeaders);
        } catch (Exception ex) {
            System.out.println("Error reading the file.");
        }
    }

    public List<Map<String, String>> readCSVFile3(String fileName, List<String> fileHeaders) throws Exception {
        StringBuilder fileData = new StringBuilder();
        fileData.append(parseDataFile(fileName));

        //System.out.println("\nSTART PARSING DATA.");
        List<Map<String, String>> parsedData = parseInputData(fileHeaders, fileData);
        //System.out.println("\nEND PARSING DATA.");
        printParsedData(parsedData);
        return parsedData;
    }

    public List<Map<String, String>> parseInputData(List<String> allHeaders, StringBuilder fileData) {
        boolean debug = false;
        if(debug) System.out.println("parseInputData : Parsing Data");
        ArrayList<Map<String, String>> parsedData = new ArrayList<>();
        Map<String, String> aRecord = new TreeMap<>();
        Set<String> headers = new TreeSet<>();
        headers.addAll(allHeaders);

        int dataPointPosition = -1;
        String partialDataPoint = "";
        boolean processingQoutedWord = false;
        String[] data = fileData.toString().split(",");
        if(debug) System.out.println("parseInputData data size is : " + data.length);
        for (String datapoint : data) {
            String dataPointFound = "";
            boolean processDataPoint = true;
            if (!processingQoutedWord)
                datapoint = datapoint.trim();
            if(debug) System.out.println("parseInputData 1: data : " + datapoint);

            String headerFound = doesDatapointContainHeader(headers, datapoint);
            if (!headerFound.isEmpty()) {
                // Check for embedded dataPoint in headers
                if(debug) System.out.println("parseInputData 2: Check for embedded dataPoint.");
                headers.remove(headerFound);
                datapoint = datapoint.replace(headerFound, "");
                dataPointFound += datapoint;
                if (datapoint.isEmpty())
                    processDataPoint = false;
            }
            else if (datapoint.contains("\"")) {
                if(debug) System.out.println("parseInputData 2a: process qouted datapoint");
                // process qouted datapoint
                if (processingQoutedWord) {
                    if(debug) System.out.println("parseInputData : processingQoutedWord");
                    processingQoutedWord = false;
                    datapoint = partialDataPoint + "," + removeQoutesFromDatapoint(datapoint);
                    partialDataPoint = "";
                }
                // Process datapoint that is wrapped in qoutes
                else if(datapoint.startsWith("\"") && datapoint.endsWith("\"")) {
                    if(debug) System.out.println("parseInputData : wrapping qoutes.");
                    datapoint = removeQoutesFromDatapoint(datapoint);
                }
                // process datapoints with qoute at start of Word.
                else if (datapoint.startsWith("\"")) {
                    if(debug) System.out.println("parseInputData : Qoutes at start of word.");
                    processingQoutedWord = true;
                    partialDataPoint += removeQoutesFromDatapoint(datapoint);
                    continue;
                }
                dataPointFound += datapoint;
            }
            else {
                // Handle correctly formatted datapoint.;
                dataPointFound += datapoint;
                if(debug) System.out.println("This is a correctly formatted  data field [" + datapoint + "]");
            }
            if(debug) System.out.println("parseInputData 3: datapoint : [" + datapoint + "].");

            if (processDataPoint) {
                dataPointPosition++;
                int headerNumber = dataPointPosition % allHeaders.size();
                if(debug) System.out.println("parseInputData 5a: dataPointPosition : [" + dataPointPosition + "].");
                if(debug) System.out.println("parseInputData 5a: headerNumber : [" + headerNumber + "].");

                String fieldName = allHeaders.get(headerNumber);
                if(debug) System.out.println("parseInputData 4: Handle Data Point : ["
                        + headerNumber
                        + "]. datapoint : [" + dataPointFound + "].");
                if(debug) System.out.println("Add to Map : [" + fieldName + "] = [" + dataPointFound + "].");
                //if (headerNumber == 0) {
                if(debug) System.out.println("parseInputData 4a : " + headerNumber + " == " + allHeaders.size());
                //if (headerNumber == allHeaders.size()) {
                if (checkForLastHeader(allHeaders, dataPointPosition)) {
                    // Store record and start a new one
                    aRecord.put(fieldName, dataPointFound);
                    if(debug) System.out.println("parseInputData 5a: Found last header");
                    if(debug) System.out.println("parseInputData 5a: dataPointFound [" + dataPointFound + "].");
                    if(debug) System.out.println("parseInputData 5b : " + aRecord.size() + " == " + allHeaders.size());
                    if (aRecord.size() == allHeaders.size()) {
                        if(debug) System.out.println("parseInputData 5c: Storing Record : [" + aRecord + "].");
                        parsedData.add(aRecord);
                    }
                    if(debug) System.out.println("parseInputData 6: Clearing record.");
                    aRecord = new LinkedHashMap<>();
                    aRecord.put(fieldName, dataPointFound);
                }
                else {
                    // Add to existing record.
                    if(debug) System.out.println("parseInputData 7: Adding Field.");
                    aRecord.put(fieldName, dataPointFound);
                }
                aRecord.put(fieldName, dataPointFound);
            }
        }
        if(debug) System.out.println("parseInputData 8: returning parsed Data: ");
        //printParsedData(parsedData);
        return parsedData;
    }

    public void printParsedData(List<Map<String, String>> parsedData) {
        int i=0;
        for (Map<String, String> record : parsedData) {
            System.out.print("\nRecord " + ++i + ": ");
            for (String field : record.keySet()) {
                String text = "[" + record.get(field) + "], ";
                for (int j = record.get(field).length(); j < 14; j++) {
                    text += " ";
                }
                System.out.print(text);
            }
        }
    }

    public String removeQoutesFromDatapoint(String datapoint) {
        if (datapoint == null | datapoint.length() == 0) return "";
        return  datapoint.replace("\"", "");
    }

    public boolean checkForLastHeader(List<String> allHeaders, int dataPointPosition) {
        boolean debug = false;
        int currentPosition = dataPointPosition % allHeaders.size();
        if (currentPosition == allHeaders.size()-1) {
            if (debug) System.out.println("checkForLastHeader 1: At Last Header");
            return true;
        }
        if (debug) System.out.println("checkForLastHeader 1: Not Last Header");
        return false;
    }

    public StringBuilder parseDataFile(String fileName) throws Exception {
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
            while((value = br.read()) != -1) {
                charCount++;
                if(debug) System.out.println("charCount is : " + charCount);
                if (charCount >= 10) {
                    addToCharArray(charArray, charCount, value);
                    if(debug) System.out.println("charArray is : [" + charArray.length + "]. charCount is [" + charCount + "]. Letter is : [" + (char)value + "]." );
                    StringBuffer line = new StringBuffer();
                    for (int i=0; i == charArray.length; i++) {
                        line.append(charArray[i]);
                    }
                    if(debug) printCharArray(charArray);
                    data.append(parseCharArray(charArray));
                    charArray= new char[11];
                    charCount = -1;
                } else {
                    addToCharArray(charArray, charCount, value);
                    if(debug) printCharArray(charArray);
                }
            }
            if(debug) System.out.println("Read Done : ");
            if (charCount > -1 ) {
                if(debug) System.out.println("==>> Test Char charArray   [" + (charCount) + "] : " + charArray[charCount]);
                char[] compactArray = resizeCharArray(charArray, charCount);
                if(debug) System.out.println("==>> Test Char compactArray[" + (compactArray.length-1) + "] : " + compactArray[compactArray.length-1]);
                if(debug) printCharArray(compactArray);
                data.append(compactArray);
            }
            if(debug) System.out.println("dataFile : " + data.toString());
            if(debug) System.out.println("END PARSING FILE.");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // releases resources associated with the streams
            if(is!=null)
                is.close();
            if(isr!=null)
                isr.close();
            if(br!=null)
                br.close();
        }
        return data;
    }

    public char[] addToCharArray(char[] charArray, int charCount, int value ) {
        boolean debug = false;
        char c = (char)value;
        charArray[charCount] = c;
        if (debug) System.out.println("New Letter : " + charCount + " : [" + c + "]."  + value);
        return charArray;
    }

    public void printArrayList(List<String> wordList) {
        System.out.println("Printing ArrayList : ");
        for (String word : wordList)
            System.out.println("Word is : " + word);
    }

    public void printCharArray(char[] array) {
        for(int i=0; i < array.length; i++) {
            array[i] = array[i];
            System.out.print(array[i]);
        }System.out.print("].\n");
    }

    public void printObjectArray(Object[] array) {
        for(int i=0; i < array.length; i++) {
            array[i] = array[i];
            System.out.print(array[i]);
            if (i+1 < array.length) System.out.print(", ");
        }System.out.print("].\n");
    }

    public char[] resizeCharArray(char[] charArray, int charCount) {
        boolean debug = false;
        char[] compactArray = new char[charCount+1];
        if (debug) System.out.println("==>> resizeCharArray charCount : " + charCount);
        if (debug) System.out.println("==>> charArray [" + charCount + "] char : " + charArray[charCount]);

        if (debug) System.out.print("Compacting Array : [");
        for(int i=0; i <= charCount; i++) {
            compactArray[i] = charArray[i];
            if (debug) System.out.print(charArray[i]);
        }
        if (debug) System.out.print("].\n");
        return compactArray;
    }

    public void processData(StringBuilder parsedData, List<String> lineRemaining) {
        System.out.println("Processing Line : [" + lineRemaining + "].");
        for (String word : lineRemaining) {
            System.out.println("processData : Adding Line : [" + word.trim() + "].");
            parsedData.append(word.trim());
        }
    }
    public List<String> handleHeaders(List<String> line, Set<String> headers) {
        System.out.println("handleHeaders : line is ["  + line + "]");
        List<String> lineRemaining = new ArrayList<>();
        List<String> headersFound = new ArrayList<>();

        for (String header : headers) {
            for (String word : line) {
                if (word.indexOf(header) >= 0) {
                    System.out.println("Header found : " + word);
                    headersFound.add(header);

                    String wordRemaining = getRemainingWord(header, word);
                    System.out.println("handleHeaders wordRemaining : [" + wordRemaining + "].");
                    if (wordRemaining.length() > 0) {
                        System.out.println("handleHeaders 1. wordRemaining.add( : " + wordRemaining + ");");
                        lineRemaining.add(wordRemaining);
                    }
                }
            }
        }
        headers.removeAll(headersFound);

        if (headers.isEmpty())
            System.out.println("All Headers Found");
        else
            System.out.println("Headers left : " + headers.toString());
        System.out.println("handleHeaders : XXX lineRemaining [" + lineRemaining + "]");
        return lineRemaining;
    }

    public String doesDatapointContainHeader(Set<String> headers, String word) {
        boolean debug = false;
        String containsHeader = "";
        for (String header : headers)
            if (word.startsWith(header)) {
                if (debug) System.out.println("containsHeader 1. : This is a header");
                containsHeader = header;
            }
        return containsHeader;
    }

    public boolean isHeader(Set<String> headers, String word) {
        boolean debug = false;
        boolean isHeader = false;
        for (String header : headers)
            if (word.equalsIgnoreCase(header)) {
                if (debug) System.out.println("isHeader 1. : This is a header");
                isHeader = true;
            }
        return isHeader;
    }

    public String getRemainingWord(String header, String word) {
        return word.replace(header, "").trim();
    }

    public StringBuilder parseCharArray(char[] charArray) {
        boolean debug = false;
        StringBuilder result = new StringBuilder();
        if(debug) System.out.println("In parseCharArray.");

        //if empty, return!
        if (charArray == null) return result;

        char customQuote = DEFAULT_QUOTE;
        char separator = DEFAULT_SEPARATOR;

        char previousChar = ' ';
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean inWord = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        int charCount = -1;
        if (debug) System.out.println("==>> parseCharArray.length : " + charArray.length);
        if (debug) System.out.println("==>> Test Char [" + (charArray.length-1) + "] : " + charArray[charArray.length-1]);
        for (char ch : charArray) {
            charCount++;
            if (debug) {
                if (debug) System.out.println("parseCharArray PreviousChar : " + previousChar);
                if (debug) System.out.println("parseCharArray Current Char : [" + charArray[charCount] + "] - [+ " +
                        + (int)charArray[charCount] + "].");
            }
            if (inQuotes) {
                if (debug) System.out.println("Collect Char : [" + charArray[charCount] + "].");
                startCollectChar = true;
                if (ch == DEFAULT_QUOTE) {
                    if (debug) System.out.println("parseCharArray inQuotes: DEFAULT_QUOTE");
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                    curVal.append(ch);
                } else {
                    if (debug) System.out.println("parseCharArray inQuotes: NOT_DEFAULT_QUOTE");
                    inWord = true;
                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                            continue;
                        }
                    } else {
                        if (ch != '\n')  {
                            if (debug) System.out.println("Hi there");
                            curVal.append(ch);
                        }
                    }
                }
                if (ch == '\r') {
                    if (debug) System.out.println("In LF");
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    if (debug) System.out.println("parseCharArray inQuotes: In CF");
                    curVal.append(",");
                    if (debug) System.out.println("parseCharArray inQuotes: parseLine : " + curVal);
                    continue;
                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
                    inWord = true;
                    //Fixed : allow "" in empty quote enclosed
                    //if (charArray[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    //}

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separator) {
                    if (debug) System.out.println("parseCharArray : In Seperator curValue is : [" + curVal + "].");
                    inWord = false;
                    //result.add(curVal.toString());
                    result.append(curVal.toString()).append(separator);

                    curVal = new StringBuffer();
                    startCollectChar = false;
                    if (debug) System.out.println("parseCharArray : charCount is : " + charCount);
                    if (debug) System.out.println("parseCharArray : result is now : [" + result + "].");
                    if (debug) System.out.println("parseCharArray : curValue is now : [" + curVal + "].");

                } else if (ch == '\r') {
                    if (debug) System.out.println("In LF");
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    if (debug) System.out.println("parseCharArray : In CF");
                    curVal.append(",");
                    if (debug) System.out.println("parseCharArray X: parseLine : " + curVal);
                    continue;
                } else {
                    inWord = true;
                    curVal.append(ch);
                    if (debug) System.out.println("Appending char [" + charCount + "] : [" + ch + "]. CurValue is [" + curVal.toString() + "].");
                }
            }
            previousChar = ch;
        }

        if (!inWord) {
            if (debug) System.out.println("Adding Word : " + curVal);
            //result.add(curVal.toString() + "|");
            result.append(curVal.toString());
        } else {
            //result.add(curVal.toString());
            result.append(curVal.toString());
            if (debug) System.out.println("Last Word is : " + curVal);
            if (debug) System.out.println("Last result is : " + result);
        }
        return result;
    }
}
