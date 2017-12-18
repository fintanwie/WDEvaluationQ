package com.workday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by fward on 16/12/2017.
 */
public class DataScienceExcerise
{
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<NationsRecord> nationalData;

    public static void main(String[] args)
    {
        DataScienceExcerise dse = new DataScienceExcerise();
        dse.getNationData();
        dse.displayMenu();
    }

    private void getNationData()
    {
        ParseNations1File nations1Parser = new ParseNations1File();
        nationalData = nations1Parser.getData();

        ParseNations2File nations2Parser = new ParseNations2File();
        nations2Parser.getData(nationalData);
    }

    private void displayMenu()
    {
        int choice;
        boolean poppingMenu;
        boolean quit = false;
        while (!quit) {
            poppingMenu = false;
            printInstructions();
            System.out.println("Enter your choice : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    printInstructions();
                    break;
                case 1:
                    Collections.sort(nationalData);
                    printData(nationalData);
                    break;
                case 2:
                    poppingMenu = displayOutliersMenu();
                    break;
                case 3:
                    findLowerIncomes();
                    break;
                case 4:
                    findingMatchingRegion();
                    break;
                case 5:
                    quit = true;
                    break;
            }

            if (!quit) {
                if (!poppingMenu) {
                    System.out.println("Press Enter To Continue");
                    scanner.nextLine();
                }
            }
        }
    }

    private void printInstructions()
    {
        System.out.println("\nMain Menu\n=========");
        System.out.println("Press ");
        System.out.println("\t0 - To print choice options");
        System.out.println("\t1 - Print Data");
        System.out.println("\t2 - Display Outliers");
        System.out.println("\t3 - Display regions with systematically lower income");
        System.out.println("\t4 - Enter new national data and display relevant country");
        System.out.println("\t5 - To quit the application");
    }

    private boolean displayOutliersMenu()
    {
        int choice;
        boolean quit = false;
        while (!quit) {
            printOutliersInstruction();
            System.out.println("Enter your choice : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    printOutliersInstruction();
                    break;
                case 1:
                    displayPopulationOutlier();
                    break;
                case 2:
                    displayGDPOutlier();
                    break;
                case 3:
                    displayLifeExpectencyOutlier();
                    break;
                case 4:
                    quit = true;
                    break;
            }

            if (!quit) {
                System.out.println("Press Enter To Continue");
                scanner.nextLine();
            }
        }
        return true;
    }

    private void printOutliersInstruction()
    {
        System.out.println("\nOutliners Menu\n==============");
        System.out.println("Press ");
        System.out.println("\t0 - To print choice options");
        System.out.println("\t1 - Display Population Outliers");
        System.out.println("\t2 - Display GDP Outliers");
        System.out.println("\t3 - Display Life Expectency Outliers");
        System.out.println("\t4 - Go back to main menu");
    }


    private void displayLifeExpectencyOutlier()
    {
        Collections.sort(nationalData, NationsRecord.getLifeExpectencyComparator());
        float size = nationalData.size();
        float middle = size / 2;
        float meanPosition = (size / 2) / 2;
        float lowerMean = (0 + meanPosition);
        float higherMean = (middle + meanPosition);

        Double Q1 = nationalData.get((int) lowerMean - 1).getLife_expect() +
                nationalData.get((int) lowerMean + 1).getLife_expect() / 2;
        Double Q3 = nationalData.get((int) higherMean - 1).getLife_expect() +
                nationalData.get((int) higherMean + 1).getLife_expect() / 2;

        Double innerQuartileRange = Q3 - Q1;
        Double HighOutliner = Q3 + (1.5 * innerQuartileRange);
        Double LowOutliner = Q1 - (1.5 * innerQuartileRange);

        ArrayList<NationsRecord> LowOutliers = new ArrayList<>();
        ArrayList<NationsRecord> HighOutliers = new ArrayList<>();
        for (NationsRecord nr : nationalData) {
            if (nr.getLife_expect() < LowOutliner)
                LowOutliers.add(nr);
            if (nr.getLife_expect() > HighOutliner)
                HighOutliers.add(nr);
        }
        System.out.println("HighOutliers  : (" + HighOutliner + ") : " + HighOutliers.size());
        if (HighOutliers.size() > 0) printData(HighOutliers);
        System.out.println("LowOutliers  : (" + LowOutliner + ") : " + LowOutliers.size());
        if (LowOutliers.size() > 0) printData(LowOutliers);
    }

    private void displayGDPOutlier()
    {
        Collections.sort(nationalData, NationsRecord.getGDPComparator());
        float size = nationalData.size();
        float middle = size / 2;
        float meanPosition = (size / 2) / 2;
        float lowerMean = (0 + meanPosition);
        float higherMean = (middle + meanPosition);

        Double Q1 = nationalData.get((int) lowerMean - 1).getGdp_percap() +
                nationalData.get((int) lowerMean + 1).getGdp_percap() / 2;
        Double Q3 = nationalData.get((int) higherMean - 1).getGdp_percap() +
                nationalData.get((int) higherMean + 1).getGdp_percap() / 2;

        Double innerQuartileRange = Q3 - Q1;
        Double HighOutliner = Q3 + (1.5 * innerQuartileRange);
        Double LowOutliner = Q1 - (1.5 * innerQuartileRange);

        ArrayList<NationsRecord> LowOutliers = new ArrayList<>();
        ArrayList<NationsRecord> HighOutliers = new ArrayList<>();
        for (NationsRecord nr : nationalData) {
            if (nr.getGdp_percap() < LowOutliner)
                LowOutliers.add(nr);
            if (nr.getGdp_percap() > HighOutliner)
                HighOutliers.add(nr);
        }
        System.out.println("HighOutliers  : (" + HighOutliner + ") : " + HighOutliers.size());
        if (HighOutliers.size() > 0) printData(HighOutliers);
        System.out.println("LowOutliers  : (" + LowOutliner + ") : " + LowOutliers.size());
        if (LowOutliers.size() > 0) printData(LowOutliers);
    }

    private void displayPopulationOutlier()
    {
        Collections.sort(nationalData, NationsRecord.getPopulationComparator());
        float size = nationalData.size();
        float middle = size / 2;
        float meanPosition = (size / 2) / 2;
        float lowerMean = (0 + meanPosition);
        float higherMean = (middle + meanPosition);

        double Q1 = nationalData.get((int) lowerMean - 1).getPopulation() +
                nationalData.get((int) lowerMean + 1).getPopulation() / 2;
        double Q3 = nationalData.get((int) higherMean - 1).getPopulation() +
                nationalData.get((int) higherMean + 1).getPopulation() / 2;

        double innerQuartileRange = Q3 - Q1;
        double HighOutliner = Q3 + (1.5 * innerQuartileRange);
        double LowOutliner = Q1 - (1.5 * innerQuartileRange);

        ArrayList<NationsRecord> LowOutliers = new ArrayList<>();
        ArrayList<NationsRecord> HighOutliers = new ArrayList<>();
        for (NationsRecord nr : nationalData) {
            if (nr.getPopulation() < LowOutliner)
                LowOutliers.add(nr);
            if (nr.getPopulation() > HighOutliner)
                HighOutliers.add(nr);
        }
        System.out.println("HighOutliers  : (" + HighOutliner + ") : " + HighOutliers.size());
        if (HighOutliers.size() > 0) printData(HighOutliers);
        System.out.println("LowOutliers  : (" + LowOutliner + ") : " + LowOutliers.size());
        if (LowOutliers.size() > 0) printData(LowOutliers);
    }

    private void findLowerIncomes()
    {
        Map<String, Integer> incomeBrackets = new HashMap<>();
        incomeBrackets.put("Not classified", 0);
        incomeBrackets.put("High income", 40);
        incomeBrackets.put("Upper middle income", 30);
        incomeBrackets.put("Lower middle income", 20);
        incomeBrackets.put("Low income", 10);

        Map<String, Double> regions = new HashMap<>();
        Map<String, Double> regionsCount = new HashMap<>();
        for (NationsRecord nr : nationalData) {
            String region = nr.getRegion();
            String nrIncomeBracket = nr.getIncome();
            double allIncome = 0;
            if (regions.containsKey(region)) {
                allIncome = regions.get(region);
            }
            allIncome += incomeBrackets.get(nrIncomeBracket);
            regions.put(region, allIncome);

            double regionCount = 0;
            if (regionsCount.containsKey(region)) {
                regionCount = regionsCount.get(region);
            }
            regionsCount.put(region, ++regionCount);
        }

        String regionName = "";
        double lowestIncome = 9999;
        for (String region : regions.keySet()) {
            double avgIncome = regions.get(region) / regionsCount.get(region);
            if (lowestIncome > avgIncome) {
                lowestIncome = avgIncome;
                regionName = region;
            }
        }
        System.out.println("The region with systematically lower income is : [" + regionName + "]");
    }

    private void findingMatchingRegion()
    {
        NationsRecord unknownNR = enterNationalData();
        NationsRecord selectedNR = findExactMatch(unknownNR);
        String region = "No Match Found";
        if (selectedNR != null)
            region = selectedNR.getRegion();
        System.out.println("Region Detected : " + region);
        scanner.nextLine();
    }

    private NationsRecord enterNationalData()
    {
        System.out.print("Enter country data now.");

        System.out.print("\nEnter Life Expectency : ");
        String lifeExpectency = scanner.next();

        System.out.print("\nEnter Population : ");
        String population = scanner.next();

        System.out.print("\nEnter Birth Rate : ");
        String birthRate = scanner.next();

        System.out.print("\nEnter Neo Natel Mortality Rate : ");
        String neonat_mortal_rate = scanner.next();

        System.out.print("\nEnter GDP Per Capita : ");
        String gdp_percap = scanner.next();

        Map<String, String> newRecord = new HashMap<>();
        newRecord.put("iso2c", "XX");
        newRecord.put("iso3c", "XXX");
        newRecord.put("country", "XXXX");
        newRecord.put("year", "2014");
        newRecord.put("life_expect", lifeExpectency);
        newRecord.put("population", population);
        newRecord.put("birth_rate", birthRate);
        newRecord.put("neonat_mortal_rate", neonat_mortal_rate);
        newRecord.put("region", "XXXX");
        newRecord.put("income", "0.0");
        newRecord.put("gdp_percap", gdp_percap);
        return new NationsRecord(newRecord);
    }

    private NationsRecord findExactMatch(NationsRecord unknownNR)
    {
        Collections.sort(nationalData, NationsRecord.getPopulationComparator());
        ArrayList<NationsRecord> nearMatchingNR = new ArrayList<>();

        // Check for near matches
        for (NationsRecord nr : nationalData) {
            Double unKnownLE = unknownNR.getLife_expect();
            Double nrLE = nr.getLife_expect();
            if ((unKnownLE + 1 > nrLE) && (unKnownLE - 1 < nrLE))
                nearMatchingNR.add(nr);
        }

        // No matches found
        if (nearMatchingNR.size() == 0)
            return null;

        // Narrow to closest match
        Double curDiff;
        Double matchedDiff = 100.0;
        NationsRecord finalMatch = null;
        for (NationsRecord nr : nearMatchingNR) {
            curDiff = Math.abs(unknownNR.getLife_expect() - nr.getLife_expect());
            if (matchedDiff > curDiff) {
                matchedDiff = curDiff;
                finalMatch = nr;
            }
        }
        System.out.println();
        return finalMatch;
    }

    private void printData(ArrayList<NationsRecord> nationalData)
    {
        List<String> allHeaders = new ArrayList<>();
        allHeaders.add("iso2c");
        allHeaders.add("iso3c");
        allHeaders.add("country");
        allHeaders.add("year");
        allHeaders.add("life_expect");
        allHeaders.add("population");
        allHeaders.add("birth_rate");
        allHeaders.add("neonat_mortal_rate");
        allHeaders.add("region");
        allHeaders.add("income");
        allHeaders.add("gdp_percap");

        System.out.print("Headers    : ");
        int charTab = 10;
        for (String field : allHeaders) {
            switch (field) {
                case "iso2c":
                    charTab = 8;
                    break;
                case "iso3c":
                    charTab = 8;
                    break;
                case "country":
                    charTab = 30;
                    break;
                case "year":
                    charTab = 8;
                    break;
                case "life_expect":
                    charTab = 16;
                    break;
                case "population":
                    charTab = 16;
                    break;
                case "birth_rate":
                    charTab = 12;
                    break;
                case "neonat_mortal_rate":
                    charTab = 18;
                    break;
                case "region":
                    charTab = 30;
                    break;
                case "income":
                    charTab = 24;
                    break;
                case "gdp_percap":
                    charTab = 16;
                    break;
            }
            StringBuilder text = new StringBuilder();
            text.append("[").append(field).append("], ");
            for (int j = field.length(); j < charTab; j++) {
                text.append(" ");
            }
            System.out.print(text.toString());
        }

        int i = 0;
        charTab = 10;
        for (NationsRecord record : nationalData) {
            System.out.print(String.format("\nRecord %03d : ", ++i));
            for (String field : allHeaders) {
                String fieldValue = "";

                switch (field) {
                    case "iso2c":
                        fieldValue = record.getIso2c();
                        charTab = 8;
                        break;
                    case "iso3c":
                        fieldValue = record.getIso3c();
                        charTab = 8;
                        break;
                    case "country":
                        fieldValue = record.getCountry();
                        charTab = 30;
                        break;
                    case "year":
                        fieldValue = Integer.toString(record.getYear());
                        charTab = 8;
                        break;
                    case "life_expect":
                        fieldValue = record.getLife_expect().toString();
                        charTab = 16;
                        break;
                    case "population":
                        fieldValue = record.getPopulation().toString();
                        charTab = 16;
                        break;
                    case "birth_rate":
                        fieldValue = record.getBirth_rate().toString();
                        charTab = 12;
                        break;
                    case "neonat_mortal_rate":
                        fieldValue = record.getNeonat_mortal_rate().toString();
                        charTab = 18;
                        break;
                    case "region":
                        fieldValue = record.getRegion();
                        charTab = 30;
                        break;
                    case "income":
                        fieldValue = record.getIncome();
                        charTab = 24;
                        break;
                    case "gdp_percap":
                        fieldValue = record.getGdp_percap().toString();
                        charTab = 16;
                        break;
                }
                StringBuilder text = new StringBuilder();
                text.append("[").append(fieldValue).append("], ");
                for (int j = fieldValue.length(); j < charTab; j++) {
                    text.append(" ");
                }
                System.out.print(text.toString());
            }
        }
        System.out.println("");
    }
}
