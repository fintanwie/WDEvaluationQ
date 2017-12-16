package com.workday;

import java.util.ArrayList;
import java.util.Collections;
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

    public void getNationData() {
        ParseNations1File nations1Parser = new ParseNations1File();
        nationalData = nations1Parser.getData();
        //System.out.println("DataScienceExcerise : getNationDataSet : size  : " + nationalData.size());

        ParseNations2File nations2Parser = new ParseNations2File();
        nations2Parser.getData(nationalData);
        //System.out.println("DataScienceExcerise : getNationDataSet : size : " + nationalData.size());
    }

    public void displayMenu()
    {
        int choice = 0;
        boolean quit = false;
        while (!quit) {
            printInstructions();
            System.out.println("Enter your choice : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 0 :
                    printInstructions();
                    break;
                case 1 :
                    printData();
                    break;
                case 2 :
                    //contacts.AddContact();
                    break;
                case 3 :
                    //contacts.UpdateContact();
                    break;
                case 4 :
                    //contacts.RemoveContact();
                    break;
                case 5 :
                    //contacts.SearchContacts();
                    break;
                case 6 :
                    quit = true;
                    break;
            }

            if (!quit) {
                System.out.println("Press Enter To Continue");
                scanner.nextLine();
            }
        }
    }

    public void printInstructions()
    {
        System.out.println("\nPress ");
        System.out.println("\t0 - To print choice options");
        System.out.println("\t1 - Print Data");
        System.out.println("\t2 - Display Outliners");
        System.out.println("\t3 - Display Outlines ignoring population");
        System.out.println("\t4 - Display regions wiht systematically lower income");
        System.out.println("\t5 - Enter new national data and display relevant country");
        System.out.println("\t6 - To quit the application");
    }


    private void printData() {
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
        System.out.println("allHeaders :" + allHeaders);

        System.out.print("Headers   : ");
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
            String text = "[" + field + "], ";
            for (int j = field.length(); j < charTab; j++) {
                text += " ";
            }
            System.out.print(text);
        }

        int i=0;
        charTab = 10;
        Collections.sort(nationalData);
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
                        fieldValue = new Integer(record.getYear()).toString();
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
                String text = "[" + fieldValue + "], ";
                for (int j = fieldValue.length(); j < charTab; j++) {
                    text += " ";
                }
                System.out.print(text);
            }
        }
        System.out.println("");
    }
}
